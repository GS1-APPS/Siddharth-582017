package org.gs1us.sgg.gbservice.impl;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import javax.annotation.Resource;
import org.gs1us.fileimport.ImportException;
import org.gs1us.sgg.account.AccountManager;
import org.gs1us.sgg.app.AppManager;
import org.gs1us.sgg.app.GBAppContext;
import org.gs1us.sgg.clockservice.ClockService;
import org.gs1us.sgg.commerce.CommerceManager;
import org.gs1us.sgg.dao.AgentUser;
import org.gs1us.sgg.dao.GBAccountRecord;
import org.gs1us.sgg.dao.InvoiceRecord;
import org.gs1us.sgg.dao.PaymentRecord;
import org.gs1us.sgg.gbservice.api.Amount;
import org.gs1us.sgg.gbservice.api.AppDesc;
import org.gs1us.sgg.gbservice.api.AppSubscription;
import org.gs1us.sgg.gbservice.api.AttributeSchema;
import org.gs1us.sgg.gbservice.api.AttributeSet;
import org.gs1us.sgg.gbservice.api.BillingTransaction;
import org.gs1us.sgg.gbservice.api.BillingTransactionType;
import org.gs1us.sgg.gbservice.api.GBAccount;
import org.gs1us.sgg.gbservice.api.GlobalBrokerException;
import org.gs1us.sgg.gbservice.api.Import;
import org.gs1us.sgg.gbservice.api.ImportPrevalidationSegmentSettings;
import org.gs1us.sgg.gbservice.api.ImportValidationProduct;
import org.gs1us.sgg.gbservice.api.Invoice;
import org.gs1us.sgg.gbservice.api.InvoiceExtra;
import org.gs1us.sgg.gbservice.api.IsoCountryRef;
import org.gs1us.sgg.gbservice.api.ModuleDesc;
import org.gs1us.sgg.gbservice.api.NoSuchAccountException;
import org.gs1us.sgg.gbservice.api.OrderLineItem;
import org.gs1us.sgg.gbservice.api.OrderStatus;
import org.gs1us.sgg.gbservice.api.Payment;
import org.gs1us.sgg.gbservice.api.PaymentReceipt;
import org.gs1us.sgg.gbservice.api.Product;
import org.gs1us.sgg.gbservice.api.ProductState;
import org.gs1us.sgg.gbservice.api.Action;
import org.gs1us.sgg.gbservice.api.ProductStatus;
import org.gs1us.sgg.gbservice.api.ProductValidationError;
import org.gs1us.sgg.gbservice.api.PurchaseOrder;
import org.gs1us.sgg.gbservice.api.SalesOrder;
import org.gs1us.sgg.gbservice.api.UploadValidationProduct;
import org.gs1us.sgg.gbservice.api.AppDesc.Scope;
import org.gs1us.sgg.gbservice.json.InboundProductAttribute;
import org.gs1us.sgg.gbservice.json.InboundProductStatus;
import org.gs1us.sgg.gbservice.test.TestManager;
import org.gs1us.sgg.gbservice.util.PutHandler;
import org.gs1us.sgg.product.ProductManager;
import org.gs1us.sgg.util.UserInputUtil;
import org.gs1us.sgg.util.VersionOracle;
import org.gs1us.sgg.validation.Validator;


public class GlobalBrokerServiceImpl 
{
    @Resource
    private ClockService m_clockService;
    
    @Resource
    private AccountManager m_accountManager;
    
    @Resource
    private ProductOpsManager m_productOpsManager;

    @Resource
    private ProductManager m_productManager;
    
    @Resource
    private AppManager m_appManager;
    
    @Resource
    private CommerceManager m_commerceManager;
    
    @Resource
    private ImportManager m_importManager;
    
    @Resource
    private TestManager m_testManager;
    
    @Resource
    private VersionOracle m_versionOracle;
    
    private String m_defaultAppName;
    
    public GlobalBrokerServiceImpl(String defaultAppName)
    {
        m_defaultAppName = defaultAppName;
    }
    
    public String getVersion(AgentUser principal)
    {
        return m_versionOracle.getVersion();
    }
    
    public Collection<? extends AppDesc> getAppDescs(AgentUser principal, String gln)
        throws GlobalBrokerException
    {
        return m_appManager.getAppDescs();
    }

    public GBAccount getAccount(AgentUser principal, String gln) throws GlobalBrokerException
    {
        return m_accountManager.getAccount(gln);
    }
    
    public GBAccount newGBAccount(String gln, String name, String[] gcps, AttributeSet attributes)
    {
        GBAccountRecord result = m_accountManager.newGBAccountRecord();
        result.setGln(gln);
        result.setName(name);
        result.setGcps(gcps);
        result.setAttributes(attributes);
        return result;
    }
/*
    public void createAccount(AgentUser principal, String username, GBAccount gbAccount)
        throws GlobalBrokerException
    {
        m_accountManager.createAccount(gbAccount);
        m_appManager.createAppSubscription(principal, username, gbAccount, new AppSubscriptionImpl(GlobalBrokerService.BASIC_APP_NAME, null));
        return;
    }

    public void updateAccount(AgentUser principal, String username, GBAccount gbAccount)
            throws GlobalBrokerException
    {
        GBAccount oldAccount = m_accountManager.getAccount(gbAccount.getGln());
        nullsToOldValues(oldAccount, gbAccount);
        m_accountManager.updateAccount(gbAccount);
        return;
    }
*/
    public void putAccount(AgentUser principal, String username, GBAccount gbAccount, Action action) throws GlobalBrokerException
    {
        AccountPutHandler handler = new AccountPutHandler(principal, username);
        Action actualAction = handler.put(gbAccount, action);
        if (actualAction == Action.CREATE)
            m_appManager.createAppSubscription(principal, username, gbAccount, new AppSubscriptionImpl(m_defaultAppName, null));
    }
 
    
    private class AccountPutHandler extends PutHandler<GBAccount>
    {
        private AgentUser m_agentUser;
        private String m_username;
        private AttributeSchema m_attributeSchema;
        

        public AccountPutHandler(AgentUser agentUser, String username)
        {
            super();
            m_agentUser = agentUser;
            m_username = username;
            m_attributeSchema = attributeSchemaForAllApps(Scope.ACCOUNT);
        }
        
        private AttributeSchema attributeSchemaForAllApps(Scope scope)
        {
            AttributeSchema result = new AttributeSchema();
            for (AppDesc appDesc : m_appManager.getAppDescs())
            {
                ModuleDesc moduleDesc = appDesc.getModuleDesc(scope);
                if (moduleDesc != null)
                    result.addModule(moduleDesc.toAttributeSchemaModule());
            }
            return result;
        }

        @Override
        protected List<ProductValidationError> validate(GBAccount gbAccount)
        {
            List<ProductValidationError> validationErrors = new ArrayList<>();
            Validator validator = new Validator();
            validator.validateUserAttributes(m_attributeSchema, 
                                                Action.UPDATE, // TODO
                                                gbAccount, 
                                                validationErrors);
            m_accountManager.validateGcps(gbAccount, validationErrors);
            return validationErrors;
        }

        @Override
        protected GBAccount get(GBAccount gbAccount) throws GlobalBrokerException
        {
            return m_accountManager.getAccount(gbAccount.getGln());
        }

        @Override
        protected void create(GBAccount gbAccount) throws GlobalBrokerException
        {
            nullsToOldValues(null, gbAccount);
            m_accountManager.createAccount(m_agentUser, m_username, gbAccount);
            
        }
        
        private void nullsToOldValues(GBAccount oldAccount, GBAccount newAccount)
        {
            AttributeSet newAttributes = newAccount.getAttributes();
            AttributeSet oldAttributes = oldAccount == null ? null : oldAccount.getAttributes();
            
            newAttributes.nullsToOldValues(oldAttributes);
        }


        @Override
        protected void update(GBAccount oldResource, GBAccount newResource)
            throws GlobalBrokerException
        {
            nullsToOldValues(oldResource, newResource);
            m_accountManager.updateAccount(m_agentUser, m_username, newResource);
        }

        @Override
        protected boolean equals(GBAccount oldResource, GBAccount newResource)
        {
            if (!oldResource.getGln().equals(newResource.getGln()))
                return false;
            
            if (!oldResource.getName().equals(newResource.getName()))
                return false;
            
            if (oldResource.getGcps() == null)
            {
                if (newResource.getGcps() != null && newResource.getGcps().length > 0)
                    return false;
            }
            else
            {
                if (newResource.getGcps() == null)
                    return false;
                else
                {
                    if (!Arrays.equals(oldResource.getGcps(), newResource.getGcps()))
                        return false;
                }
            }
            
            if (oldResource.getAttributes() == null)
            {
                if (newResource.getAttributes() != null && newResource.getAttributes().getAttributes().size() > 0)
                    return false;
            }
            else
            {
                if (newResource.getAttributes() == null)
                    return false;
                else
                {
                    if (!oldResource.getAttributes().getAttributes().equals(newResource.getAttributes().getAttributes()))
                        return false;
                }
            }
            return true;
        }

        @Override
        protected GlobalBrokerException noSuchResourceException()
        {
            return new NoSuchAccountException();
        }
        
    }


    public Collection<? extends AppSubscription> getAppSubscriptions(AgentUser principal, String gln, boolean includeAppDescs) throws GlobalBrokerException
    {
        GBAccount gbAccount = findGbAccount(gln);
        return m_appManager.getAppSubscriptions(gbAccount, includeAppDescs);
    }
    
    private GBAppContext findGbAppContext(AgentUser agentUser, String username, String gln) throws GlobalBrokerException
    {
        GBAccount gbAccount = findGbAccount(gln);
        Collection<? extends AppSubscription> subs = m_appManager.getAppSubscriptions(gbAccount, true);
        return new GBAppContext(agentUser, username, gbAccount, subs);
    }

    private GBAccount findGbAccount(String gln) throws NoSuchAccountException
    {
        GBAccount gbAccount = m_accountManager.getAccount(gln);
        if (gbAccount == null)
            throw new NoSuchAccountException();
        else
            return gbAccount;
    }
    
    
    

    public AppSubscription newAppSubscription(String appName, Object appArgs)
    {
        return new AppSubscriptionImpl(appName, appArgs);
    }

    public void createAppSubscription(AgentUser principal, String username, String gln, AppSubscription subscription) throws GlobalBrokerException
    {
        GBAccount gbAccount = findGbAccount(gln);
        m_appManager.createAppSubscription(principal, username, gbAccount, subscription);
        return;
    }
    
    
    public Collection<? extends Product> getProducts(AgentUser principal, String gln)
        throws GlobalBrokerException
    {
        GBAccount gbAccount = findGbAccount(gln);
        return m_productManager.getProducts(gbAccount);
    }

    public Product getProductByGtin(AgentUser principal, String gln, String gtin)
        throws GlobalBrokerException
    {
        GBAccount gbAccount = findGbAccount(gln);
        return m_productOpsManager.getProductByGtin(gbAccount, m_productOpsManager.padGtin(gtin));
    }

    public Product newProduct(String gtin)
    {
        return m_productManager.newProduct(gtin);
    }

    public PurchaseOrder newPurchaseOrder(String id, Date date, Collection<? extends OrderLineItem> lineItems)
    {
        return new PurchaseOrderImpl(id, date, lineItems);
    }

    public ProductStatus validateProduct(AgentUser principal, String gln, Product unpaddedNewProduct, boolean renew)
        throws GlobalBrokerException
    {
        GBAppContext appContext = findGbAppContext(principal, null, gln);
        return m_productOpsManager.validateProduct(appContext, unpaddedNewProduct, renew); 

    }


    public ProductStatus createProduct(final AgentUser principal, final String username, String gln, Product unpaddedProduct,
            final PurchaseOrder po) throws GlobalBrokerException
    {
        final GBAppContext appContext = findGbAppContext(principal, username, gln);
        return m_productOpsManager.createProduct(appContext, po, unpaddedProduct);
    }


    public ProductStatus updateProduct(final AgentUser principal, final String username, String gln, Product unpaddedProduct, boolean renew,
            final PurchaseOrder po) throws GlobalBrokerException
    {
        final GBAppContext appContext = findGbAppContext(principal, username, gln);
        return m_productOpsManager.updateProduct(appContext, unpaddedProduct, renew, po);
    }

    
    public ProductStatus putProduct(final AgentUser principal, final String username, String gln, Product unpaddedProduct, boolean renew) throws GlobalBrokerException
    {
        GBAppContext appContext = findGbAppContext(principal, username, gln);
        return m_productOpsManager.putProduct(appContext, unpaddedProduct, renew);
        
    }

    public ProductStatus deleteProduct(AgentUser principal, String username, String gln, String gtin,
            PurchaseOrder po) throws GlobalBrokerException
    {    	
        GBAccount gbAccount = findGbAccount(gln);
        return m_productOpsManager.deleteProduct(principal, username, gtin, gbAccount);
    }

    
   public List<? extends UploadValidationProduct> bulkUpload(AgentUser principal, String validatedUsername, String gbAccountGln, List<? extends InboundProductAttribute> productAttributeList) throws GlobalBrokerException
   {
	   
	   //TODO: find username, gln based on principal object.

       //Collection<? extends AppSubscription> subs = getGbService().getAppSubscriptions(gbAccountGln, true);
       //Collection<? extends AppSubscription> subs = getAppSubscriptions(agentUser, gbAccountGln, true);
	   
	   GBAppContext appContext = findGbAppContext(principal, validatedUsername, gbAccountGln);
	   
	   return m_productOpsManager.bulkUploadProducts(appContext, productAttributeList);
       	   
	   
   }


    public Import importUpload(AgentUser principal, String username, String gln, String filename, String format, byte[] content) throws GlobalBrokerException
    {
        GBAppContext appContext = findGbAppContext(principal, username, gln);

        try
        {
            return m_importManager.importUpload(appContext, filename, format, content);
        }
        catch (IOException e)
        {
            throw new GlobalBrokerException(e);
        }
        catch (ImportException e)
        {
            throw new GlobalBrokerException(e);
        }
    }
    
    public Import importChangeSettings(AgentUser principal, String username, String gln, String importId, List<? extends ImportPrevalidationSegmentSettings> settings) throws GlobalBrokerException
    {
        GBAppContext appContext = findGbAppContext(principal, username, gln);

        try
        {
            return m_importManager.importChangeSettings(appContext, importId, settings);
        }
        catch (IOException e)
        {
            throw new GlobalBrokerException(e);
        }
        catch (ImportException e)
        {
            throw new GlobalBrokerException(e);
        }
    }


    public Import importConfirm(AgentUser principal, String username, String gln, String importId) throws GlobalBrokerException
    {
        GBAppContext appContext = findGbAppContext(principal, username, gln);

        try
        {
            return m_importManager.importConfirm(appContext, importId);
        }
        catch (IOException e)
        {
            throw new GlobalBrokerException(e);
        }
        catch (ImportException e)
        {
            throw new GlobalBrokerException(e);
        }
    }

    public Import getImport(AgentUser principal, String gln, String importId) throws GlobalBrokerException
    {
        GBAccount gbAccount = findGbAccount(gln);
        return m_importManager.getImport(principal, gbAccount, importId);
    }

    public Collection<? extends Import> getImports(AgentUser principal, String gln) throws GlobalBrokerException
    {
        GBAccount gbAccount = findGbAccount(gln);
        return m_importManager.getImports(principal, gbAccount);
    }

    public void deleteImport(AgentUser principal, String gln, String importId) throws GlobalBrokerException
    {
        GBAccount gbAccount = findGbAccount(gln);
        m_importManager.deleteImport(principal, gbAccount, importId);
    }

    public InvoiceExtra newInvoiceExtra(String itemCode, String itemDescription, String[] itemParameters, Amount total)
    {
        return m_commerceManager.newInvoiceExtra(itemCode, itemDescription, itemParameters, total);
    }
    


    public Collection<? extends SalesOrder> getUninvoicedOrders(AgentUser principal, String gln) throws GlobalBrokerException
    {
        GBAccount gbAccount = findGbAccount(gln);
        return m_commerceManager.getUninvoicedSalesOrderRecords(gbAccount);
    }
    
    public Collection<? extends SalesOrder> getUninvoicedOrders(AgentUser principal) throws GlobalBrokerException
    {
        return m_commerceManager.getUninvoicedSalesOrderRecords(null);
    }

    public Collection<? extends SalesOrder> getInvoiceOrders(AgentUser principal, String invoiceId) throws GlobalBrokerException
    {
        Collection<? extends SalesOrder> salesOrders = m_commerceManager.getInvoiceSalesOrderRecords(invoiceId);
        
        // Make sure line items are lazily fetched
        for (SalesOrder salesOrder : salesOrders)
        {
            for (OrderLineItem lineItem : salesOrder.getLineItems());
        }
        return salesOrders;
    }

    public Invoice invoiceOrders(AgentUser principal, String username, String gln, List<String> orderIds, List<? extends InvoiceExtra> extras)
        throws GlobalBrokerException
    {
        GBAccount gbAccount = findGbAccount(gln);
        return m_commerceManager.createInvoice(principal, username, gbAccount, m_clockService.now(), orderIds.toArray(new String[0]), extras);
    }
    
    

    public Invoice getInvoice(AgentUser principal, String invoiceId) throws GlobalBrokerException
    {
        InvoiceRecord invoiceRecord = m_commerceManager.getInvoiceRecordByInvoiceId(invoiceId);
        if (invoiceRecord != null)
        {
            // Make sure extras are lazily fetched
            for (InvoiceExtra extra : invoiceRecord.getExtras());
        }
        return invoiceRecord;
    }

    public Collection<? extends Invoice> getAllInvoices(AgentUser principal, String gln, OrderStatus status) throws GlobalBrokerException
    {
        GBAccount gbAccount = findGbAccount(gln);
        Collection<? extends InvoiceRecord> invoiceRecords = m_commerceManager.getAllInvoiceRecords(gbAccount, status);
        for (InvoiceRecord invoiceRecord : invoiceRecords)
            // Make sure extras are lazily fetched
            for (InvoiceExtra extra : invoiceRecord.getExtras());
        return invoiceRecords;
    }

    public Collection<? extends Invoice> getAllInvoices(AgentUser principal, OrderStatus status)
        throws GlobalBrokerException
    {
        Collection<? extends InvoiceRecord> invoiceRecords = m_commerceManager.getAllInvoiceRecords(null, status);
        for (InvoiceRecord invoiceRecord : invoiceRecords)
            // Make sure extras are lazily fetched
            for (InvoiceExtra extra : invoiceRecord.getExtras());
        return invoiceRecords;
    }


    public void setInvoiceBilled(AgentUser principal, String username, String gln, String invoiceId, Date date, String billingReference) throws GlobalBrokerException
    {
        GBAccount gbAccount = findGbAccount(gln);
        m_commerceManager.setInvoiceBilled(principal, username, gbAccount, invoiceId, date, billingReference);
    }

    public Collection<? extends BillingTransaction> getAllBillingTransactions(AgentUser principal, String gln) throws GlobalBrokerException
    {
        GBAccount gbAccount = findGbAccount(gln);
        // TODO: update to new model
        List<BillingTransactionImpl> result = new ArrayList<>();
        
        Collection<? extends InvoiceRecord> invoiceRecords = m_commerceManager.getAllInvoiceRecords(gbAccount, null);
        for (InvoiceRecord invoiceRecord : invoiceRecords)
        {
            BillingTransactionImpl billingTransaction = 
                    new BillingTransactionImpl(BillingTransactionType.INVOICE, 
                                               invoiceRecord.getDate(), 
                                               invoiceRecord.getInvoiceId(),
                                               invoiceRecord.getSummary(), 
                                               invoiceRecord.getTotal());
            result.add(billingTransaction);
        }
        
        Collection<? extends PaymentRecord> payments = m_commerceManager.getAllPayments(gbAccount);
        for (PaymentRecord payment : payments)
        {
            BillingTransactionImpl billingTransaction = 
                    new BillingTransactionImpl(BillingTransactionType.PAYMENT, 
                                               payment.getDate(),
                                               payment.getPaymentReceiptId(),
                                               payment.getDescription(),
                                               payment.getAmount().negate());      
            result.add(billingTransaction);
        }
        
        Collections.sort(result, new Comparator<BillingTransactionImpl>()
        {
            @Override
            public int compare(BillingTransactionImpl o1, BillingTransactionImpl o2)
            {
                return -(o1.getDate().compareTo(o2.getDate()));
            }
        });
        
        Amount balance = Amount.ZERO;
        for (int i = result.size() - 1; i >= 0; i--)
        {
            BillingTransactionImpl bt = result.get(i);
            balance = balance.add(bt.getAmount());
            bt.setBalance(balance);
        }
        
        return result;
    }

    public PaymentReceipt newPaymentReceipt(AgentUser principal, String id, Date date, Amount amount)
    {
        return new PaymentReceiptImpl(id, date, amount);
    }

    public Payment payInvoices(AgentUser principal, String username, String gln, PaymentReceipt receipt, List<String> invoiceIds) throws GlobalBrokerException
    {
        GBAccount gbAccount = findGbAccount(gln);
        return m_commerceManager.pay(principal, username, gbAccount, receipt, invoiceIds, m_clockService.now());
    }
    
    

    public Payment getPayment(AgentUser principal, String paymentId) throws GlobalBrokerException
    {
        return m_commerceManager.getPaymentRecordByPaymentId(paymentId);
    }

    public Collection<? extends Payment> getAllPayments(AgentUser principal, OrderStatus status)
        throws GlobalBrokerException
    {
        return m_commerceManager.getAllPaymentsByStatus(status);
    }

    public void setPaymentPaid(AgentUser principal, String username, String gln, String paymentId, Date date, String paidReference) throws GlobalBrokerException
    {
        GBAccount gbAccount = findGbAccount(gln);
        m_commerceManager.setPaymentPaid(principal, username, gbAccount, paymentId, date, paidReference);
    }

    public void setTime(AgentUser principal, String username, Date time)
    {
        m_clockService.setNow(time);
    }
    
    public String test(AgentUser principal, String username, String testName, String testParam) throws GlobalBrokerException
    {
        return m_testManager.test(testName, testParam);
    }
        
    public Product getProductByGtinOnly(String gtin)
            throws GlobalBrokerException
    {
    	return m_productManager.getProductByGtin(null, gtin);
    }
/*    
    public Collection<? extends Product> getProductsForReport()
            throws GlobalBrokerException
    {
        return m_productManager.getProductsForReport();
    }
*/
    
    public Long getProductsForReport()
            throws GlobalBrokerException
    {
        return m_productManager.getProductsForReport();
    }
    
    public Long getProductsForReportByDate()
            throws GlobalBrokerException
    {
        return m_productManager.getProductsForReportByDate();
    }
    
    public Collection<? extends IsoCountryRef> getAllIsoCountryRef() throws GlobalBrokerException
    {
        return m_productManager.getAllIsoCountryRef();
    }
    
    public Collection<? extends Product> getProductsBasedOnGpcAndTargetMarket(String gpc, String marketCode, String startIndex, String maxSize) throws GlobalBrokerException
    {
        return m_productManager.getProductsBasedOnGpcAndTargetMarket(gpc, marketCode, startIndex, maxSize);
    }
    
    public Collection<? extends Product> getProductsForPagination(String gln, String startIndex, String maxSize) throws GlobalBrokerException
    {
    	return m_productManager.getProductsForPagination(gln, startIndex, maxSize);
    }
    
    public Long getProductsCountBasedOnGpcAndTargetMarket(String gpc, String marketCode) throws GlobalBrokerException
    {
        return m_productManager.getProductsCountBasedOnGpcAndTargetMarket(gpc, marketCode);
    }

    public Long getRegisteredProductsCount(String gln) throws GlobalBrokerException
    {
        return m_productManager.getRegisteredProductsCount(gln);
    }
    
}
