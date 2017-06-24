package org.gs1us.sgg.gbservice.api;

import java.util.Collection;
import java.util.Date;
import java.util.List;

/**
 * Service interface to the Global Broker.
 * @author kt
 *
 */
public interface GlobalBrokerService
{
    public static final String BASIC_APP_NAME = "basic";
    
    /**
     * Returns the version of the global broker.
     */
    public String getVersion() throws GlobalBrokerException;
    
    /**
     * Returns a list of all applications available for the specified account, subscribed or not.
     * @throws NoSuchAccountException   if the specified gbAccount does not exist.
     * @throws GlobalBrokerServiceException  if the service was unreachable or otherwise encountered an implementation error
     */
    public Collection<? extends AppDesc> getAppDescs(String gln) throws GlobalBrokerException;
    
    /**
     * Returns the account associated with the specified GLN, or null if no such account exists.
     * @throws GlobalBrokerServiceException  if the service was unreachable or otherwise encountered an implementation error
     */
    public GBAccount getAccount(String gln) throws GlobalBrokerException;
    
    
    public GBAccount newGBAccount(String gln, String name, String[] gcps, AttributeSet attributes);
    
    /**
     * Creates a new account having the specified GLN and other parameters. The new account will be associated
     * to the requestor as its agent. 
     * @throws DuplicateAccountException   if there is already an account having this GLN.
     * @throws GlobalBrokerServiceException  if the service was unreachable or otherwise encountered an implementation error
     */
    //public void createAccount(GBAccount gbAccount) throws GlobalBrokerException;
    
    /**
     * Updates the specified account and other parameters. 
     * @throws NoSuchAccountException   if there no account having this GLN.
     * @throws GlobalBrokerServiceException  if the service was unreachable or otherwise encountered an implementation error
     */
    //public void updateAccount(GBAccount gbAccount) throws GlobalBrokerException;
    
    public void putAccount(String username, GBAccount gbAccount, Action action) throws GlobalBrokerException;
    
    /**
     * Returns a list of the applications that are subscribed by the specified account.
     * @throws NoSuchAccountException   if the specified gbAccount does not exist.
     * @throws GlobalBrokerServiceException  if the service was unreachable or otherwise encountered an implementation error
     */
    public Collection<? extends AppSubscription> getAppSubscriptions(String gln, boolean includeAppDescs) throws GlobalBrokerException;
    
    public AppSubscription newAppSubscription(String appName, Object appArgs);
    /**
     * Subscribes the specified account to the specified app. No effect if already subscribed. The <code>AppSubscription</code> invoice
     * need not include the app desc; it will be ignored anyway.
     * @throws NoSuchAccountException   if the specified gbAccount does not exist.
     * @throws NoSuchAppException   if the specified app does not exist.
     * @throws GlobalBrokerServiceException  if the service was unreachable or otherwise encountered an implementation error
     */
    public void createAppSubscription(String username, String gln, AppSubscription appSubscription) throws GlobalBrokerException;
    
    /**
     * Returns a collection of the products that are registered by the specified account.
     * @throws NoSuchAccountException   if the specified gbAccount does not exist.
     * @throws GlobalBrokerServiceException  if the service was unreachable or otherwise encountered an implementation error
     */
    public Collection<? extends Product> getProducts(String gln) throws GlobalBrokerException; // TODO: searchProducts()

    /**
     * Returns the product having the specified gtin registered by the specified account, or null if this account does not have this GTIN registered.
     * @throws NoSuchAccountException   if the specified gbAccount does not exist.
     * @throws GlobalBrokerServiceException  if the service was unreachable or otherwise encountered an implementation error
     */
    public Product getProductByGtin(String gln, String gtin) throws GlobalBrokerException;
    
    /**
     * Creates a new <code>Product</code> instance for use by other methods.
     */
    public Product newProduct(String gtin);
    
    /**
     * Creates a new <code>PurchaseOrder</code> instance for use by other methods.
     */
    public PurchaseOrder newPurchaseOrder(String id, Date date, Collection<? extends OrderLineItem> lineItems);

    /**
     * Validates the specified product data.
     * @throws NoSuchAccountException   if the specified gbAccount does not exist.
     * @throws GlobalBrokerServiceException  if the service was unreachable or otherwise encountered an implementation error
     */
    public ProductStatus validateProduct(String gln, Product product, boolean renew) throws GlobalBrokerException;
    public ProductStatus createProduct(String username, String gln, Product product, PurchaseOrder po) throws GlobalBrokerException;
    public ProductStatus updateProduct(String username, String gln, Product product, boolean renew, PurchaseOrder po) throws GlobalBrokerException;
    public ProductStatus putProduct(String username, String gln, Product product, boolean renew) throws GlobalBrokerException;
    public ProductStatus deleteProduct(String username, String gln, String gtin, PurchaseOrder po) throws GlobalBrokerException;
    
    /**
     * Uploads a file of product data for import.
     * Following this operation, the import is in the UPLOADED state, with the prevalidation set but the validation still null.
     * The prevalidation will contain an initial guess at import settings. The client must next call <code>importChangeSettings</code>
     * to update the settings and proceed to content validation. (If the initial guess was acceptable to the client, the call
     * to <code>importChangeSettings</code> can include a null argument to keep the initial guess.) Exception: if the
     * file was unparseable the import will be in the UPLOAD_FAILED state.
     */
    public Import importUpload(String username, String gln, String filename, String format, byte[] content) throws GlobalBrokerException;
    
    /**
     * Changes the import settings for an import, and proceeds to content validation. If <code>settings</code> is null, keeps the 
     * original settings and proceeds directly to content validation. If the settings (whether explicitly provided or implicitly via a null
     * argument) have errors, the import remains in the UPLOADED state and the client must change settings to clear the errors, and
     * validation is not performed. Otherwise, the import transitions to the VALIDATED state and the returned import includes the
     * content validation.
     * @param settings -- a list of segment settings, one for each segment in the prevalidation
     */
    public Import importChangeSettings(String username, String gln, String importId, List<? extends ImportPrevalidationSegmentSettings> settings) throws GlobalBrokerException;
    
    /**
     * Confirms an import, causing products to be created/updated. The import must be in the VALIDATED state.
     */
    public Import importConfirm(String username, String gln, String importId) throws GlobalBrokerException;
    
    /**
     * Returns information about a prior import.
     */
    public Import getImport(String gln, String importId) throws GlobalBrokerException;
    
    /**
     * Returns information about all prior imports for a specified account.
     */
    public Collection<? extends Import> getImports(String gln) throws GlobalBrokerException;
    
    /**
     * Deletes a prior import.
     */
    public void deleteImport(String gln, String importId) throws GlobalBrokerException;
    
    /**
     * Returns a new <code>InvoiceExtra</code> instance for use by other methods.
     */
    public InvoiceExtra newInvoiceExtra(String itemCode, String itemDescription, String[] itemParameters, Amount total);
    
    /**
     * Returns all orders for the specified account not yet invoiced.
     * @throws NoSuchAccountException   if the specified gbAccount does not exist.
     * @throws GlobalBrokerServiceException  if the service was unreachable or otherwise encountered an implementation error
     */
    public Collection<? extends SalesOrder> getUninvoicedOrders(String gln) throws GlobalBrokerException;

    /**
     * Returns all orders not yet invoiced.
     * @throws GlobalBrokerServiceException  if the service was unreachable or otherwise encountered an implementation error
     */
    public Collection<? extends SalesOrder> getUninvoicedOrders() throws GlobalBrokerException;
    
    /**
     * Returns all orders that are part of the specified invoice.
     * @throws NoSuchInvoiceException  if the specified invoice does not exist.
     * @throws GlobalBrokerServiceException  if the service was unreachable or otherwise encountered an implementation error
     */
    public Collection<? extends SalesOrder> getInvoiceOrders(String invoiceId) throws GlobalBrokerException;
    
    /**
     * Creates an invoice from the specified uninvoiced orders and (optionally) any extras (sales tax, shipping, etc).
     * @throws NoSuchAccountException   if the specified gbAccount does not exist.
     * @throws InvoiceException   if any of the specified orders does not exist or is not in an uninvoiced state
     * @throws GlobalBrokerServiceException  if the service was unreachable or otherwise encountered an implementation error
     */
    public Invoice invoiceOrders(String username, String gln, List<String> orderIds, List<? extends InvoiceExtra> extras) throws GlobalBrokerException;

    /**
     * Returns the invoice having the specified invoice ID, or null if no such invoice exists.
     * @throws GlobalBrokerServiceException  if the service was unreachable or otherwise encountered an implementation error
     */
    public Invoice getInvoice(String invoiceId) throws GlobalBrokerException;
    
    /**
     * Returns all invoices for the specified account having the specified status.
     * @throws NoSuchAccountException   if the specified gbAccount does not exist.
     * @throws GlobalBrokerServiceException  if the service was unreachable or otherwise encountered an implementation error
     */
    public Collection<? extends Invoice> getAllInvoices(String gln, OrderStatus status) throws GlobalBrokerException;

    /**
     * Returns all invoices  having the specified status.
     * @throws GlobalBrokerServiceException  if the service was unreachable or otherwise encountered an implementation error
     */
    public Collection<? extends Invoice> getAllInvoices(OrderStatus status) throws GlobalBrokerException;
    
    /**
     * Sets the specified invoice to the billed state.
     * @throws NoSuchAccountException   if the specified gbAccount does not exist.
     * @throws InvoiceException  if the specified invoice does not exist, is not for the specified account, or is not in the invoiced state
     * @throws GlobalBrokerServiceException  if the service was unreachable or otherwise encountered an implementation error
     */
    public void setInvoiceBilled(String username, String gln, String invoiceId, Date date, String billingReference) throws GlobalBrokerException;

    /**
     * Returns all billing transactions for the specified account, in reverse chronological order.
     * @throws NoSuchAccountException   if the specified gbAccount does not exist.
     * @throws GlobalBrokerServiceException  if the service was unreachable or otherwise encountered an implementation error
     */
    public Collection<? extends BillingTransaction> getAllBillingTransactions(String gln) throws GlobalBrokerException;
    
    /**
     * Creates a new <code>PaymentReceipt</code> instance for use by other methods.
     */
    public PaymentReceipt newPaymentReceipt(String paymentReference, Date date, Amount amount);

    /**
     * Records a payment for the specified invoices.
     * @throws NoSuchAccountException   if the specified gbAccount does not exist.
     * @throws NoSuchInvoiceException   if one or more of the specified invoices does not exist.
     * @throws PaymentException   if the amount of the receipt does not match the invoice totals.
     * @throws GlobalBrokerServiceException  if the service was unreachable or otherwise encountered an implementation error
     */
    public Payment payInvoices(String username, String gln, PaymentReceipt receipt, List<String> invoiceIds) throws GlobalBrokerException;
    
    /**
     * Returns the payment having the specified payment ID, or null if no such payment exists.
     * @throws GlobalBrokerServiceException  if the service was unreachable or otherwise encountered an implementation error
     */
    public Payment getPayment(String paymentId) throws GlobalBrokerException;
    
    /**
     * Returns all payments having the specified status.
     * @throws GlobalBrokerServiceException  if the service was unreachable or otherwise encountered an implementation error
     */
    public Collection<? extends Payment> getAllPayments(OrderStatus status) throws GlobalBrokerException;

    /**
     * Sets the specified payment into the paid state.
     * @throws NoSuchAccountException   if the specified gbAccount does not exist.
     * @throws PaymentException   if the payment does not exist or is already in the paid state.
     * @throws GlobalBrokerServiceException  if the service was unreachable or otherwise encountered an implementation error
     */
    public void setPaymentPaid(String username, String gln, String paymentId, Date date, String paidReference) throws GlobalBrokerException;
    
    /**
     * Freezes and sets the real-time clock used for all operations, to support testing. This is disabled in
     * production environments.
     * @throws GlobalBrokerException
     */
    public void setTime(String username, Date time) throws GlobalBrokerException;
    
    /**
     * Performs a test. This is disabled in production environments.
     * @throws GlobalBrokerException
     */
    public String test(String username, String testName, String testParam) throws GlobalBrokerException;
        
    public Product getProductByGtinOnly(String gtin) throws GlobalBrokerException;
    
    //public Collection<? extends Product> getProductsForReport() throws GlobalBrokerException;
    
    public Long getProductsForReport() throws GlobalBrokerException;
    
    public Long getProductsForReportByDate() throws GlobalBrokerException;
    
    public Collection<? extends IsoCountryRef> getAllIsoCountryRef() throws GlobalBrokerException;
    
    public Collection<? extends Product> getProductsBasedOnGpcAndTargetMarket(String gpc, String marketCode, String startIndex, String maxSize) throws GlobalBrokerException;
    
    public Collection<? extends Product> getProductsForPagination(String gln, String startIndex, String maxSize) throws GlobalBrokerException;
    
    public Long getProductsCountBasedOnGpcAndTargetMarket(String gpc, String marketCode) throws GlobalBrokerException;
    
    public Long getRegisteredProductsCount(String gln) throws GlobalBrokerException;
}
