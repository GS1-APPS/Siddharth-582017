package org.gs1us.sgg.gbservice.client;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.gs1us.sgg.gbservice.api.*;
import org.gs1us.sgg.gbservice.json.*;
import org.gs1us.sgg.json.JsonHttpClient;
import org.gs1us.sgg.transport.HttpTransport;
import org.gs1us.sgg.transport.HttpTransport.HttpMethod;

import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

public class GlobalBrokerServiceClient implements GlobalBrokerService
{

    private String m_prefix;
    
    private HttpTransport m_transport;
    
    private ObjectMapper m_objectMapper;
    
    private JsonHttpClient m_jsonHttpClient;
    
    //private GlobalBrokerServiceImpl m_globalBrokerServiceImpl;
    
    private String m_username;
    private String m_password;
    private String m_authObjectClassName;
    private String m_authUseridMethodName;
    private String m_authApikeyMethodName;
    private boolean m_configurationBasedAuthEnabled; // Keep disabled to use Logged in Users identity
   
    public GlobalBrokerServiceClient(String prefix, String username, String password, HttpTransport transport, String authObjectClassName, String authUseridMethodName, String authApikeyMethodName, boolean configurationBasedAuthEnabled)
    {
        super();
        m_prefix = prefix;
        m_objectMapper = (new ObjectMapperFactoryBean()).getObject();
        m_transport = transport;
        m_username = username;
        m_password = password;
        m_authObjectClassName = authObjectClassName;
        m_authUseridMethodName = authUseridMethodName;
        m_authApikeyMethodName  = authApikeyMethodName;
        m_configurationBasedAuthEnabled = configurationBasedAuthEnabled;
        
        m_jsonHttpClient = new JsonHttpClient(m_prefix, m_username, m_password, m_transport, m_authObjectClassName, m_authUseridMethodName, m_authApikeyMethodName, m_configurationBasedAuthEnabled,  m_objectMapper);

    }
    
    
    public String getVersion() throws GlobalBrokerException
    {
        return m_jsonHttpClient.doRequest(String.class, HttpMethod.GET, null, "/api/version");
    }
    
    public Collection<? extends AppDesc> getAppDescs(String gln) throws GlobalBrokerException
    {
        //return m_globalBrokerServiceImpl.getAppDescs(m_principal, gln);
        return m_jsonHttpClient.doRequest(Collection.class, InboundAppDesc.class, HttpMethod.GET, null, "/api/account/%s/apps", gln);
    }

    public GBAccount getAccount(String gln)
        throws GlobalBrokerException
    {
        //return m_globalBrokerServiceImpl.getAccount(m_principal, gln);
        try
        {
            return m_jsonHttpClient.doRequest(InboundGBAccount.class, HttpMethod.GET, null, "/api/account/%s", gln);
        }
        catch (NoSuchAccountException e)
        {
            return null;
        }
    }

    public GBAccount newGBAccount(String gln, String name, String[] gcps, AttributeSet attributes)
    {
        InboundGBAccount result = new InboundGBAccount();
        result.setGln(gln);
        result.setName(name);
        result.setGcps(gcps);
        InboundAttributeSet aset = new InboundAttributeSet();
        if (attributes != null)
            aset.setAttributes(new HashMap<String,String>(attributes.getAttributes()));
        result.setAttributes(aset);
        return result;
    }
/*
    public void createAccount(GBAccount gbAccount)
        throws GlobalBrokerException
    {
        //m_globalBrokerServiceImpl.createAccount(m_principal, gbAccount);
        m_jsonHttpClient.doRequest(HttpMethod.PUT, gbAccount, "/api/account/%s", gbAccount.getGln());
    }

    public void updateAccount(GBAccount gbAccount)
            throws GlobalBrokerException
    {
        m_jsonHttpClient.doRequest(HttpMethod.PUT, gbAccount, "/api/account/%s", gbAccount.getGln());
    }
*/
    public void putAccount(String username, GBAccount gbAccount, Action action) throws GlobalBrokerException
    {
        if (action == null)
            m_jsonHttpClient.doRequest(HttpMethod.PUT, gbAccount, "/api/account/%s?username=%s", gbAccount.getGln(), username);
        else
            m_jsonHttpClient.doRequest(HttpMethod.PUT, gbAccount, "/api/account/%s?username=%s&action=%s", gbAccount.getGln(), username, action);
    }

    public Collection<? extends AppSubscription> getAppSubscriptions(String gln, boolean includeAppDescs)
        throws GlobalBrokerException
    {
        //return m_globalBrokerServiceImpl.getAppSubscriptions(m_principal, gln, includeAppDescs);
        Collection<? extends AppSubscription> subs = m_jsonHttpClient.doRequest(Collection.class, InboundAppSubscription.class, HttpMethod.GET, null, "/api/account/%s/subscription?includeAppDescs=%s", gln, includeAppDescs);
        return subs;
    }

    public AppSubscription newAppSubscription(String appName, Object appArgs)
    {
        InboundAppSubscription result = new InboundAppSubscription();
        result.setAppName(appName);
        result.setAppArgs(appArgs);
        return result;
    }

    public void createAppSubscription(String username, String gln, AppSubscription subscription) throws GlobalBrokerException
    {
        //m_globalBrokerServiceImpl.createAppSubscription(m_principal, gln, subscription);
        m_jsonHttpClient.doRequest(HttpMethod.PUT, subscription, "/api/account/%s/subscription/%s?username=%s", gln, subscription.getAppName(), username);
    }

    public Collection<? extends Product> getProducts(String gln) throws GlobalBrokerException
    {
        //return m_globalBrokerServiceImpl.getProducts(m_principal, gln);
        return m_jsonHttpClient.doRequest(Collection.class, InboundProduct.class, HttpMethod.GET, null, "/api/product?gln=%s", gln);
    }

    public Product getProductByGtin(String gln, String gtin)
        throws GlobalBrokerException
    {
        //return m_globalBrokerServiceImpl.getProductByGtin(m_principal, gln, gtin);
        return m_jsonHttpClient.doRequest(InboundProduct.class, HttpMethod.GET, null, "/api/product/%s?gln=%s", gtin, gln);
    }

    public Product newProduct(String gtin)
    {
        InboundProduct result = new InboundProduct();
        result.setGtin(gtin);
        InboundAttributeSet attributes = new InboundAttributeSet();
        attributes.setAttributes(new HashMap<String,String>());
        result.setAttributes(attributes);
        return result;
    }

    public PurchaseOrder newPurchaseOrder(String poId, Date date,
            Collection<? extends OrderLineItem> lineItems)
    {
        InboundPurchaseOrder result = new InboundPurchaseOrder();
        result.setPOId(poId);
        result.setDate(date);
        result.setLineItems((Collection<InboundOrderLineItem>) lineItems);
        return result;
    }

    public ProductStatus validateProduct(String gln, Product product, boolean renew) throws GlobalBrokerException
    {
        //return m_globalBrokerServiceImpl.validateProduct(m_principal, gln, newProduct, renew);
        String gtin = product.getGtin();
    
        return m_jsonHttpClient.doRequest(InboundProductStatus.class, HttpMethod.POST, product, "/api/product/%s/validate?gln=%s&renew=%s", gtin, gln, renew);
    }

    public ProductStatus createProduct(String username, String gln, Product product, PurchaseOrder po) throws GlobalBrokerException
    {
        // return m_globalBrokerServiceImpl.createProduct(m_principal, gln, product, po);
        String gtin = product.getGtin();
        InboundProductAndPo productAndPo = new InboundProductAndPo();
        productAndPo.setProduct((InboundProduct)product);
        productAndPo.setPo((InboundPurchaseOrder)po);

        return m_jsonHttpClient.doRequest(InboundProductStatus.class, HttpMethod.POST, productAndPo, "/api/product/%s/create?gln=%s&username=%s", gtin, gln, username);

    }

    public ProductStatus updateProduct(String username, String gln, Product product, boolean renew, PurchaseOrder po)
        throws GlobalBrokerException
    {
        // return m_globalBrokerServiceImpl.updateProduct(m_principal, gln, product, renew, po);
        String gtin = product.getGtin();
        InboundProductAndPo productAndPo = new InboundProductAndPo();
        productAndPo.setProduct((InboundProduct)product);
        productAndPo.setPo((InboundPurchaseOrder)po);

        return m_jsonHttpClient.doRequest(InboundProductStatus.class, HttpMethod.POST, productAndPo, "/api/product/%s/update?gln=%s&renew=%s&username=%s", gtin, gln, renew, username);
    }
    
    @Override
    public ProductStatus putProduct(String username, String gln, Product product, boolean renew) throws GlobalBrokerException
    {
        String gtin = product.getGtin();

        return m_jsonHttpClient.doRequest(InboundProductStatus.class, HttpMethod.PUT, product, "/api/product/%s?gln=%s&renew=%s&username=%s", gtin, gln, renew, username);
    }

    public ProductStatus deleteProduct(String username, String gln,
            String gtin, PurchaseOrder po) throws GlobalBrokerException
    {
        return m_jsonHttpClient.doRequest(InboundProductStatus.class, HttpMethod.DELETE, null, "/api/nonprod/product/%s?gln=%s&username=%s", gtin, gln, username);
    }
    
    @Override
    public Import importUpload(String username, String gln, String filename, String format, byte[] content) throws GlobalBrokerException
    {
        return m_jsonHttpClient.doRequest(InboundImport.class, HttpMethod.POST, format, content, "/api/import/upload?gln=%s&username=%s&filename=%s", gln, username, filename);
    }
    
    
 
    @Override
    public Import importChangeSettings(String username, String gln, String importId, List<? extends ImportPrevalidationSegmentSettings> settings)
        throws GlobalBrokerException
    {
        return m_jsonHttpClient.doRequest(InboundImport.class, HttpMethod.POST, settings, "/api/import/%s/settings?gln=%s&username=%s", importId, gln, username);
    }

    @Override
    public Import importConfirm(String username, String gln, String importId) throws GlobalBrokerException
    {
        return m_jsonHttpClient.doRequest(InboundImport.class, HttpMethod.POST, null, "/api/import/%s/confirm?gln=%s&username=%s", importId, gln, username);
    }

    @Override
    public Import getImport(String gln, String importId) throws GlobalBrokerException
    {
        return m_jsonHttpClient.doRequest(InboundImport.class, HttpMethod.GET, null, "/api/import/%s?gln=%s", importId, gln);
    }
    
    @Override
    public Collection<? extends Import> getImports(String gln) throws GlobalBrokerException
    {
        return m_jsonHttpClient.doRequest(Collection.class, InboundImport.class, HttpMethod.GET, null, "/api/import?gln=%s", gln);
    }

    @Override
    public void deleteImport(String gln, String importId) throws GlobalBrokerException
    {
        m_jsonHttpClient.doRequest(HttpMethod.DELETE, null, "/api/import/%s?gln=%s", importId, gln);
    }

    @Override
    public InvoiceExtra newInvoiceExtra(String itemCode, String itemDescription, String[] itemParameters, Amount total)
    {
        InboundInvoiceExtra result = new InboundInvoiceExtra();
        result.setItemCode(itemCode);
        result.setItemDescription(itemDescription);
        result.setItemParameters(itemParameters);
        result.setTotal(total);
        return result;
    }

    public Collection<? extends SalesOrder> getUninvoicedOrders(String gln) throws GlobalBrokerException
    {
        //return m_globalBrokerServiceImpl.getUninvoicedOrders(m_principal, gln);
        return m_jsonHttpClient.doRequest(Collection.class, InboundSalesOrder.class, HttpMethod.GET, null, "/api/order?gln=%s&status=uninvoiced", gln);
    }

    public Collection<? extends SalesOrder> getUninvoicedOrders() throws GlobalBrokerException
    {
        // return m_globalBrokerServiceImpl.getUninvoicedOrders(m_principal);
        return m_jsonHttpClient.doRequest(Collection.class, InboundSalesOrder.class, HttpMethod.GET, null, "/api/order?status=uninvoiced");
    }
    
    @Override
    public Collection<? extends SalesOrder> getInvoiceOrders(String invoiceId)
        throws GlobalBrokerException
    {
        return m_jsonHttpClient.doRequest(Collection.class, InboundSalesOrder.class, HttpMethod.GET, null, "/api/invoice/%s/orders", invoiceId);
    }

    public Invoice invoiceOrders(String username, String gln, List<String> orderIds, List<? extends InvoiceExtra> extras) throws GlobalBrokerException
    {
        InboundOrderIdsAndExtras orderIdsAndExtras = new InboundOrderIdsAndExtras();
        orderIdsAndExtras.setOrderIds(orderIds);
        orderIdsAndExtras.setExtras((List<InboundInvoiceExtra>)extras);
        return m_jsonHttpClient.doRequest(InboundInvoice.class, HttpMethod.POST, orderIdsAndExtras, "/api/invoice/invoiceOrders?gln=%s&username=%s", gln, username);
    }

    public Invoice getInvoice(String invoiceId)
        throws GlobalBrokerException
    {
        return m_jsonHttpClient.doRequest(InboundInvoice.class, HttpMethod.GET, null, "/api/invoice/%s", invoiceId);
    }

    public Collection<? extends Invoice> getAllInvoices(String gln, OrderStatus status) throws GlobalBrokerException
    {
        // return m_globalBrokerServiceImpl.getAllInvoices(m_principal, gln, status);
        return m_jsonHttpClient.doRequest(Collection.class, InboundInvoice.class, HttpMethod.GET, null, "/api/invoice?gln=%s&status=%s", gln, status);
    }

    public Collection<? extends Invoice> getAllInvoices(OrderStatus status) throws GlobalBrokerException
    {
        // return m_globalBrokerServiceImpl.getAllInvoices(m_principal, status);
        return m_jsonHttpClient.doRequest(Collection.class, InboundInvoice.class, HttpMethod.GET, null, "/api/invoice?status=%s", status);
    }

    public void setInvoiceBilled(String username, String gln,
            String invoiceId, Date date, String billingReference)
        throws GlobalBrokerException
    {
        // m_globalBrokerServiceImpl.setInvoiceBilled(m_principal, gln, invoiceId, date, billingReference);
        InboundBillingInfo billingInfo = new InboundBillingInfo();
        billingInfo.setDate(date);
        billingInfo.setBillingReference(billingReference);
        m_jsonHttpClient.doRequest(HttpMethod.PUT, billingInfo, "/api/invoice/%s/billinginfo?gln=%s&username=%s", invoiceId, gln, username);
    }

    public Collection<? extends BillingTransaction> getAllBillingTransactions(String gln) throws GlobalBrokerException
    {
        // return m_globalBrokerServiceImpl.getAllBillingTransactions(m_principal, gln);
        return m_jsonHttpClient.doRequest(Collection.class, InboundBillingTransaction.class, HttpMethod.GET, null, "/api/account/%s/transactions", gln);
    }

    public PaymentReceipt newPaymentReceipt(String id, Date date, Amount amount)
    {
        InboundPaymentReceipt paymentReceipt = new InboundPaymentReceipt();
        paymentReceipt.setPaymentId(id);
        paymentReceipt.setDate(date);
        paymentReceipt.setAmount(amount);
        return paymentReceipt;
    }

    public Payment payInvoices(String username, String gln, PaymentReceipt receipt, List<String> invoiceIds)
        throws GlobalBrokerException
    {
        // return m_globalBrokerServiceImpl.payInvoices(m_principal, gln, receipt, invoiceIds);
        InboundPayInvoicesInfo payInvoicesInfo = new InboundPayInvoicesInfo();
        payInvoicesInfo.setPaymentReceipt((InboundPaymentReceipt)receipt);
        payInvoicesInfo.setInvoiceIds(invoiceIds);
        return m_jsonHttpClient.doRequest(InboundPayment.class, HttpMethod.POST, payInvoicesInfo, "/api/payment/payinvoices?gln=%s&username=%s", gln, username);
    }

    public Payment getPayment(String paymentId)
        throws GlobalBrokerException
    {
        // return m_globalBrokerServiceImpl.getPayment(m_principal, paymentId);
        return m_jsonHttpClient.doRequest(InboundPayment.class, HttpMethod.GET, null, "/api/payment/%s", paymentId);
    }

    public Collection<? extends Payment> getAllPayments(OrderStatus status) throws GlobalBrokerException
    {
        // return m_globalBrokerServiceImpl.getAllPayments(m_principal, status);
        return m_jsonHttpClient.doRequest(Collection.class, InboundPayment.class, HttpMethod.GET, null, "/api/payment?status=%s", status);
    }

    public void setPaymentPaid(String username, String gln, String paymentId, Date date, String paidReference) throws GlobalBrokerException
    {
        // m_globalBrokerServiceImpl.setPaymentPaid(m_principal, gln, paymentId, date);
        if (paidReference == null)
            m_jsonHttpClient.doRequest(HttpMethod.POST, date, "/api/payment/%s/paid?gln=%s&username=%s", paymentId, gln, username);
        else
            m_jsonHttpClient.doRequest(HttpMethod.POST, date, "/api/payment/%s/paid?paidReference=%s&gln=%s&username=%s", paymentId, paidReference, gln, username);
    }

    @Override
    public void setTime(String username, Date time) throws GlobalBrokerException 
    {
        m_jsonHttpClient.doRequest(HttpMethod.PUT, time, "/api/time?username=%s", username);
    }

    @Override
    public String test(String username, String testName, String testParam) throws GlobalBrokerException
    {
        if (testParam == null)
            return m_jsonHttpClient.doRequest(String.class, HttpMethod.GET, null, "/api/test/%s", testName);
        else
            return m_jsonHttpClient.doRequest(String.class, HttpMethod.GET, null, "/api/test/%s?param=%s", testName, testParam);
    }
 
    public Product getProductByGtinOnly(String gtin)
            throws GlobalBrokerException
    {        	        
    	return m_jsonHttpClient.doRequest(InboundProduct.class, HttpMethod.GET, null, "/api/search/productById/%s", gtin);    	
    }
    
/*    
    public Collection<? extends Product> getProductsForReport() throws GlobalBrokerException
    {
        return m_jsonHttpClient.doRequest(Collection.class, InboundProduct.class, HttpMethod.GET, null, "/api/productList");
    }
*/
    
    public Long getProductsForReport() throws GlobalBrokerException
    {
        return m_jsonHttpClient.doRequest(Long.class, HttpMethod.GET, null, "/api/productList");
    }
    
    public Long getProductsForReportByDate() throws GlobalBrokerException
    {
        return m_jsonHttpClient.doRequest(Long.class, HttpMethod.GET, null, "/api/productListByDate");
    }
    
    public Collection<? extends IsoCountryRef> getAllIsoCountryRef() throws GlobalBrokerException
    {
    	return m_jsonHttpClient.doRequest(Collection.class, InboundIsoCountryRef.class, HttpMethod.GET, null, "/api/search/isoCountryList");
    }
        
    public Collection<? extends Product> getProductsBasedOnGpcAndTargetMarket(String gpc, String marketCode, String startIndex, String maxSize) throws GlobalBrokerException
    {
        return m_jsonHttpClient.doRequest(Collection.class, InboundProduct.class, HttpMethod.GET, null, "/api/search/productBasedOnGpcAndTargetMarket/%s?param=%s&startIndex=%s&size=%s", gpc, marketCode, startIndex, maxSize);
    }
    
    public Collection<? extends Product> getProductsForPagination(String gln, String startIndex, String maxSize) throws GlobalBrokerException
    {
    	return m_jsonHttpClient.doRequest(Collection.class, InboundProduct.class, HttpMethod.GET, null, "/api/search/productsForPagination/%s?startIndex=%s&size=%s", gln, startIndex, maxSize);
    }
    
    public Long getProductsCountBasedOnGpcAndTargetMarket(String gpc, String marketCode) throws GlobalBrokerException
    {
        return m_jsonHttpClient.doRequest(Long.class, HttpMethod.GET, null, "/api/search/productGpcTmList/%s?param=%s", gpc, marketCode);
    }

    public Long getRegisteredProductsCount(String gln) throws GlobalBrokerException
    {
    	return m_jsonHttpClient.doRequest(Long.class, HttpMethod.GET, null, "/api/registeredProductsCount/%s", gln);
    }
    
}