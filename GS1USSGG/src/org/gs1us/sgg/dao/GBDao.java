package org.gs1us.sgg.dao;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;

import javax.persistence.Query;

import org.gs1us.sgg.gbservice.api.Amount;
import org.gs1us.sgg.gbservice.api.GBAccount;
import org.gs1us.sgg.gbservice.api.InvoiceExtra;
import org.gs1us.sgg.gbservice.api.OrderStatus;

public interface GBDao
{
    GBAccountRecord newGBAccountRecord();
    GBAccountRecord getGBAccountByGln(String gln);
    Collection<? extends GBAccountRecord> getAllGBAccounts();
    void updateGBAccount(GBAccountRecord gbAccount);
    void deleteGBAccount(GBAccountRecord gbAccount);
    
    GcpRecord newGcpRecord(String gcp, String gln);
    Collection<? extends GcpRecord> getAllGcpRecords();
    Collection<? extends GcpRecord> getMatchingGcpRecords(String gcp);
    Collection<? extends GcpRecord> getGcpRecordsByGln(String gln);
    void updateGcpRecord(GcpRecord gcpRecord);
    void deleteGcpRecord(GcpRecord gcpRecord);
    

    AppSubscriptionRecord newAppSubscription();
    Collection<? extends AppSubscriptionRecord>  getAppSubscriptionsByGln(String gln);
    AppSubscriptionRecord getAppSubscription(String gln, String appId);
    Collection<? extends AppSubscriptionRecord> getAllAppSubscriptions();
    void updateAppSubscription(AppSubscriptionRecord appSubscription);
    void deleteAppSubscription(AppSubscriptionRecord appSubscription);

    ProductRecord newProduct();
    Collection<? extends ProductRecord> getProductsByGln(String gln);
    ProductRecord getProductByGtin(String gtin);
    ProductRecord getProductByGlnAndGtin(String gln, String gtin);
    Collection<? extends ProductRecord> getAllProducts();
    void updateProduct(ProductRecord product);
    void deleteProduct(ProductRecord product);
    
    ImportFile newImportFile();
    ImportFile getImportFile(String id);
    void updateImportFile(ImportFile importFile);
    void deleteImportFile(ImportFile importFile);

    ImportRecord newImportRecord();
    ImportRecord getImportRecord(String id);
    ImportRecord getImportRecordByGlnAndId(String gln, String importId);

    Collection<? extends ImportRecord> getImportRecordsByGln(String gln);
    void updateImportRecord(ImportRecord importRecord);
    void deleteImportRecord(ImportRecord importRecord);

    SalesOrderRecord newSalesOrder();
    SalesOrderRecord getSalesOrder(String id);
    Collection<? extends SalesOrderRecord> getUninvoicedSalesOrders();
    Collection<? extends SalesOrderRecord> getUninvoicedSalesOrdersByGln(String gln);
    Collection<? extends SalesOrderRecord> getSalesOrdersByInvoiceId(String invoiceId);
    SalesOrderRecord getSalesOrderBySalesOrderId(String salesOrderId);
    void updateSalesOrder(SalesOrderRecord salesOrder);
    void deleteSalesOrder(SalesOrderRecord salesOrder);
    String newSalesOrderId();

    InvoiceExtra newInvoiceExtra(String itemCode, String itemDescription, String[] itemParameters, Amount total);
    InvoiceRecord newInvoiceRecord();
    InvoiceRecord getInvoiceRecord(String id);
    InvoiceRecord getInvoiceRecordByInvoiceId(String invoiceId);
    Collection<? extends InvoiceRecord> getAllInvoiceRecords();
    Collection<? extends InvoiceRecord> getInvoiceRecordsByGln(String gln);
    Collection<? extends InvoiceRecord> getAllInvoiceRecordsByOrderStatus(OrderStatus orderStatus);
    Collection<? extends InvoiceRecord> getInvoiceRecordsByGlnAndOrderStatus(String gln, OrderStatus orderStatus);
    Collection<? extends InvoiceRecord> getInvoiceRecordsByPaymentId(String paymentId);
    void updateInvoiceRecord(InvoiceRecord invoiceRecord);
    void deleteInvoiceRecord(InvoiceRecord invoiceRecord);
    String newInvoiceId();

    PaymentRecord newPayment();
    PaymentRecord getPaymentByPaymentId(String paymentId);
    Collection<? extends PaymentRecord> getAllPaymentsByStatus(OrderStatus status);
    Collection<? extends PaymentRecord> getPaymentsByGln(String gln);
    void updatePayment(PaymentRecord payment);
    void deletePayment(PaymentRecord payment);
    String newPaymentId();
    
    AuditEventRecord newAuditEvent();
    void updateAuditEvent(AuditEventRecord event);
    
    //Collection<? extends ProductRecord> getProductsForReport();
    
    Long getProductsForReport();
    
    Long getProductsForReportByDate();
    
    Collection<? extends IsoCountryRefRecord> getAllIsoCountryRef();

    Collection<? extends ProductRecord> getProductsBasedOnGpcAndTargetMarket(String gpc, String marketCode);   
    
    Integer getTargetMarketNotBasedOnId(String value);       
}
