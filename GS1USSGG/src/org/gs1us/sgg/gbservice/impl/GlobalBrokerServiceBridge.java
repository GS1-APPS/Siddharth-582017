package org.gs1us.sgg.gbservice.impl;

import java.util.Collection;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.gs1us.sgg.dao.AgentUser;
import org.gs1us.sgg.gbservice.api.Action;
import org.gs1us.sgg.gbservice.api.Amount;
import org.gs1us.sgg.gbservice.api.AppDesc;
import org.gs1us.sgg.gbservice.api.AppSubscription;
import org.gs1us.sgg.gbservice.api.AttributeSet;
import org.gs1us.sgg.gbservice.api.BillingTransaction;
import org.gs1us.sgg.gbservice.api.GBAccount;
import org.gs1us.sgg.gbservice.api.GlobalBrokerException;
import org.gs1us.sgg.gbservice.api.GlobalBrokerService;
import org.gs1us.sgg.gbservice.api.Import;
import org.gs1us.sgg.gbservice.api.ImportPrevalidationSegmentSettings;
import org.gs1us.sgg.gbservice.api.Invoice;
import org.gs1us.sgg.gbservice.api.InvoiceExtra;
import org.gs1us.sgg.gbservice.api.OrderLineItem;
import org.gs1us.sgg.gbservice.api.OrderStatus;
import org.gs1us.sgg.gbservice.api.Payment;
import org.gs1us.sgg.gbservice.api.PaymentReceipt;
import org.gs1us.sgg.gbservice.api.Product;
import org.gs1us.sgg.gbservice.api.ProductStatus;
import org.gs1us.sgg.gbservice.api.PurchaseOrder;
import org.gs1us.sgg.gbservice.api.SalesOrder;
import org.gs1us.sgg.gbservice.api.AttributeEnumValue;

public class GlobalBrokerServiceBridge implements GlobalBrokerService
{
    @Resource
    private GlobalBrokerServiceImpl m_globalBrokerServiceImpl;
    
    private AgentUser m_principal;
    
    public GlobalBrokerServiceBridge()
    {
        m_principal = null;
    }
    
    public GlobalBrokerServiceBridge(AgentUser principal)
    {
        m_principal = principal;
    }
    
    public String getVersion() throws GlobalBrokerException
    {
        return m_globalBrokerServiceImpl.getVersion(m_principal);
    }
    
    public Collection<? extends AppDesc> getAppDescs(String gln) throws GlobalBrokerException
    {
        return m_globalBrokerServiceImpl.getAppDescs(m_principal, gln);
    }

    public GBAccount getAccount(String gln)
        throws GlobalBrokerException
    {
        return m_globalBrokerServiceImpl.getAccount(m_principal, gln);
    }

    public GBAccount newGBAccount(String gln, String name, String[] gcps, AttributeSet attributes)
    {
        return m_globalBrokerServiceImpl.newGBAccount(gln, name, gcps, attributes);
    }
/*
    public void createAccount(String username, GBAccount gbAccount)
        throws GlobalBrokerException
    {
        m_globalBrokerServiceImpl.createAccount(m_principal, username, gbAccount);
    }

    public void updateAccount(String username, GBAccount gbAccount)
            throws GlobalBrokerException
    {
        m_globalBrokerServiceImpl.updateAccount(m_principal, username, gbAccount);
    }
    */
    public void putAccount(String username, GBAccount gbAccount, Action action) throws GlobalBrokerException
    {
        m_globalBrokerServiceImpl.putAccount(m_principal, username, gbAccount, action);
    }

    public Collection<? extends AppSubscription> getAppSubscriptions(String gln, boolean includeAppDescs)
        throws GlobalBrokerException
    {
        return m_globalBrokerServiceImpl.getAppSubscriptions(m_principal, gln,
                                                             includeAppDescs);
    }

    public AppSubscription newAppSubscription(String appName, Object appArgs)
    {
        return m_globalBrokerServiceImpl.newAppSubscription(appName, appArgs);
    }

    public void createAppSubscription(String username, String gln,
            AppSubscription subscription) throws GlobalBrokerException
    {
        m_globalBrokerServiceImpl.createAppSubscription(m_principal, username, gln,
                                                        subscription);
    }

    public Collection<? extends Product> getProducts(String gln) throws GlobalBrokerException
    {
        return m_globalBrokerServiceImpl.getProducts(m_principal, gln);
    }

    public Product getProductByGtin(String gln, String gtin)
        throws GlobalBrokerException
    {
        return m_globalBrokerServiceImpl.getProductByGtin(m_principal, gln, gtin);
    }

    public Product newProduct(String gtin)
    {
        return m_globalBrokerServiceImpl.newProduct(gtin);
    }

    public PurchaseOrder newPurchaseOrder(String id, Date date,
            Collection<? extends OrderLineItem> lineItems)
    {
        return m_globalBrokerServiceImpl.newPurchaseOrder(id, date, lineItems);
    }

    public ProductStatus validateProduct(String gln,
            Product newProduct, boolean renew) throws GlobalBrokerException
    {
        return m_globalBrokerServiceImpl.validateProduct(m_principal, gln,
                                                         newProduct, renew);
    }

    public ProductStatus createProduct(String username, String gln,
            Product product, PurchaseOrder po) throws GlobalBrokerException
    {
        return m_globalBrokerServiceImpl.createProduct(m_principal, username, gln, product,
                                                       po);
    }

    public ProductStatus updateProduct(String username, String gln,
            Product product, boolean renew, PurchaseOrder po)
        throws GlobalBrokerException
    {
        return m_globalBrokerServiceImpl.updateProduct(m_principal, username, gln, product,
                                                       renew, po);
    }

    public ProductStatus putProduct(String username, String gln,
            Product product, boolean renew)
        throws GlobalBrokerException
    {
        return m_globalBrokerServiceImpl.putProduct(m_principal, username, gln, product,
                                                       renew);
    }

    public ProductStatus deleteProduct(String username, String gln, String gtin, PurchaseOrder po) throws GlobalBrokerException
    {
        return m_globalBrokerServiceImpl.deleteProduct(m_principal, username, gln, gtin, po);
    }
    
    
    
    @Override
    public Import importUpload(String username, String gln, String filename, String format, byte[] content) throws GlobalBrokerException
    {
        return m_globalBrokerServiceImpl.importUpload(m_principal, username, gln, filename, format, content);
    }

    
    
    @Override
    public Import importChangeSettings(String username, String gln, String importId, List<? extends ImportPrevalidationSegmentSettings> settings)
        throws GlobalBrokerException
    {
        return m_globalBrokerServiceImpl.importChangeSettings(m_principal, username, gln, importId, settings);
    }

    @Override
    public Import importConfirm(String username, String gln, String importId) throws GlobalBrokerException
    {
        return m_globalBrokerServiceImpl.importConfirm(m_principal, username, gln, importId);
    }

    @Override
    public Import getImport(String gln, String importId) throws GlobalBrokerException
    {
        return m_globalBrokerServiceImpl.getImport(m_principal, gln, importId);
    }
    
    @Override
    public Collection<? extends Import> getImports(String gln) throws GlobalBrokerException
    {
        return m_globalBrokerServiceImpl.getImports(m_principal, gln);
    }

    @Override
    public void deleteImport(String gln, String importId) throws GlobalBrokerException
    {
        m_globalBrokerServiceImpl.deleteImport(m_principal, gln, importId);
    }

    @Override
    public InvoiceExtra newInvoiceExtra(String itemCode, String itemDescription, String[] itemParameters, Amount total)
    {
        return m_globalBrokerServiceImpl.newInvoiceExtra(itemCode, itemDescription, itemParameters, total);
    }

    public Collection<? extends SalesOrder> getUninvoicedOrders(String gln) throws GlobalBrokerException
    {
        return m_globalBrokerServiceImpl.getUninvoicedOrders(m_principal, gln);
    }

    public Collection<? extends SalesOrder> getUninvoicedOrders() throws GlobalBrokerException
    {
        return m_globalBrokerServiceImpl.getUninvoicedOrders(m_principal);
    }
    
    @Override
    public Collection<? extends SalesOrder> getInvoiceOrders(String invoiceId)
        throws GlobalBrokerException
    {
        return m_globalBrokerServiceImpl.getInvoiceOrders(m_principal, invoiceId);
    }

    public Invoice invoiceOrders(String username, String gln, List<String> orderIds, List<? extends InvoiceExtra> extras) throws GlobalBrokerException
    {
        return m_globalBrokerServiceImpl.invoiceOrders(m_principal, username, gln, orderIds, extras);
    }

    public Invoice getInvoice(String invoiceId)
        throws GlobalBrokerException
    {
        return m_globalBrokerServiceImpl.getInvoice(m_principal, invoiceId);
    }

    public Collection<? extends Invoice> getAllInvoices(String gln, OrderStatus status) throws GlobalBrokerException
    {
        return m_globalBrokerServiceImpl.getAllInvoices(m_principal, gln, status);
    }

    public Collection<? extends Invoice> getAllInvoices(OrderStatus status) throws GlobalBrokerException
    {
        return m_globalBrokerServiceImpl.getAllInvoices(m_principal, status);
    }

    public void setInvoiceBilled(String username, String gln,
            String invoiceId, Date date, String billingReference)
        throws GlobalBrokerException
    {
        m_globalBrokerServiceImpl.setInvoiceBilled(m_principal, username, gln, invoiceId,
                                                   date, billingReference);
    }

    public Collection<? extends BillingTransaction> getAllBillingTransactions(
            String gln) throws GlobalBrokerException
    {
        return m_globalBrokerServiceImpl.getAllBillingTransactions(m_principal,
                                                                   gln);
    }

    public PaymentReceipt newPaymentReceipt(String id,
            Date date, Amount amount)
    {
        return m_globalBrokerServiceImpl.newPaymentReceipt(m_principal, id, date,
                                                           amount);
    }

    public Payment payInvoices(String username, String gln,
            PaymentReceipt receipt, List<String> invoiceIds)
        throws GlobalBrokerException
    {
        return m_globalBrokerServiceImpl.payInvoices(m_principal, username, gln, receipt,
                                                     invoiceIds);
    }

    public Payment getPayment(String paymentId)
        throws GlobalBrokerException
    {
        return m_globalBrokerServiceImpl.getPayment(m_principal, paymentId);
    }

    public Collection<? extends Payment> getAllPayments(OrderStatus status) throws GlobalBrokerException
    {
        return m_globalBrokerServiceImpl.getAllPayments(m_principal, status);
    }

    public void setPaymentPaid(String username, String gln,
            String paymentId, Date date, String paidReference) throws GlobalBrokerException
    {
        m_globalBrokerServiceImpl.setPaymentPaid(m_principal, username, gln, paymentId,
                                                 date, paidReference);
    }
    
    public void setTime(String username, Date time)
    {
        m_globalBrokerServiceImpl.setTime(m_principal, username, time);
    }

    @Override
    public String test(String username, String testName, String testParam)
        throws GlobalBrokerException
    {
        return m_globalBrokerServiceImpl.test(m_principal, username, testName, testParam);
    }

    public Product getProductByGtinOnly(String gtin)
            throws GlobalBrokerException
    {
    	return null;
    }
    
    public Collection<? extends Product> getProductsForReport() throws GlobalBrokerException
    {
        return m_globalBrokerServiceImpl.getProductsForReport();
    }
    

}
