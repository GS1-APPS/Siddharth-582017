package org.gs1us.sgg.dao.mock;

import java.util.Collection;
import java.util.Comparator;

import org.gs1us.sgg.dao.AppSubscriptionRecord;
import org.gs1us.sgg.dao.AuditEventRecord;
import org.gs1us.sgg.dao.GBAccountRecord;
import org.gs1us.sgg.dao.GBDao;
import org.gs1us.sgg.dao.GcpRecord;
import org.gs1us.sgg.dao.ImportFile;
import org.gs1us.sgg.dao.ImportRecord;
import org.gs1us.sgg.dao.InvoiceRecord;
import org.gs1us.sgg.dao.IsoCountryRefRecord;
import org.gs1us.sgg.dao.PaymentRecord;
import org.gs1us.sgg.dao.ProductRecord;
import org.gs1us.sgg.dao.SalesOrderRecord;
import org.gs1us.sgg.gbservice.api.Amount;
import org.gs1us.sgg.gbservice.api.GBAccount;
import org.gs1us.sgg.gbservice.api.InvoiceExtra;
import org.gs1us.sgg.gbservice.api.OrderStatus;

public class MockGBDao implements GBDao
{
    private MockDao<MockGBAccountRecord> m_gbAccountRecordDao = new MockDao<MockGBAccountRecord>();
    private GlnAwareMockDao<MockAppSubscriptionRecord> m_appSubscriptionRecordDao = new GlnAwareMockDao<MockAppSubscriptionRecord>();
    private GlnAwareMockDao<MockProductRecord> m_productRecordDao = new GlnAwareMockDao<MockProductRecord>();
    private GlnAwareMockDao<MockSalesOrderRecord> m_salesOrderDao = new GlnAwareMockDao<MockSalesOrderRecord>();
    private GlnAwareMockDao<MockInvoiceRecord> m_invoiceRecordDao = new GlnAwareMockDao<MockInvoiceRecord>();
    private GlnAwareMockDao<MockPaymentRecord> m_paymentDao = new GlnAwareMockDao<MockPaymentRecord>();
    private GlnAwareMockDao<MockAuditEventRecord> m_auditEventDao = new GlnAwareMockDao<MockAuditEventRecord>();
    
    private int m_nextInvoiceId = 1001;
    private int m_nextSalesOrderId = 101;
    private int m_nextPaymentId = 10001;
    
    @Override
    public GBAccountRecord newGBAccountRecord()
    {
        return new MockGBAccountRecord();
    }

    @Override
    public GBAccountRecord getGBAccountByGln(String gln)
    {
        for (GBAccountRecord record : m_gbAccountRecordDao.getAll())
        {
            if (record.getGln().equals(gln))
                return record;
        }
        return null;
    }

    @Override
    public Collection<? extends GBAccountRecord> getAllGBAccounts()
    {
        return m_gbAccountRecordDao.getAll();
    }

    @Override
    public void updateGBAccount(GBAccountRecord gbAccount)
    {
        m_gbAccountRecordDao.update((MockGBAccountRecord)gbAccount);
    }

    @Override
    public void deleteGBAccount(GBAccountRecord gbAccount)
    {
        m_gbAccountRecordDao.delete((MockGBAccountRecord)gbAccount);
    }
    
    

    @Override
    public GcpRecord newGcpRecord(String gcp, String gln)
    {
        // TODO Auto-generated method stub
        return null;
    }

    
    @Override
    public Collection<? extends IsoCountryRefRecord> getAllIsoCountryRef()
    {
        // TODO Auto-generated method stub
        return null;
    }
        
    @Override
    public Collection<? extends GcpRecord> getAllGcpRecords()
    {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Collection<? extends GcpRecord> getMatchingGcpRecords(String gcp)
    {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Collection<? extends GcpRecord> getGcpRecordsByGln(String gln)
    {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void updateGcpRecord(GcpRecord gcpRecord)
    {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void deleteGcpRecord(GcpRecord gcpRecord)
    {
        // TODO Auto-generated method stub
        
    }

    @Override
    public AppSubscriptionRecord newAppSubscription()
    {
        return new MockAppSubscriptionRecord();
    }

    @Override
    public Collection<? extends AppSubscriptionRecord> getAppSubscriptionsByGln(String gln)
    {
        return m_appSubscriptionRecordDao.getAllByGlnAndFilter(gln, null, null);
    }

    @Override
    public AppSubscriptionRecord getAppSubscription(String gln, final String appId)
    {
        return m_appSubscriptionRecordDao.getByGlnAndFilter(gln, new MockDao.Filter<MockAppSubscriptionRecord>()
        {
            public boolean include(MockAppSubscriptionRecord elt)
            {
                return elt.getAppId().equals(appId);
            }
        });
    }

    @Override
    public Collection<? extends AppSubscriptionRecord> getAllAppSubscriptions()
    {
        return m_appSubscriptionRecordDao.getAll();
    }

    @Override
    public void updateAppSubscription(AppSubscriptionRecord appSubscription)
    {
        m_appSubscriptionRecordDao.update((MockAppSubscriptionRecord)appSubscription);
    }

    @Override
    public void deleteAppSubscription(AppSubscriptionRecord appSubscription)
    {
        m_appSubscriptionRecordDao.delete((MockAppSubscriptionRecord)appSubscription);
    }

    @Override
    public ProductRecord newProduct()
    {
        return new MockProductRecord();
    }

    @Override
    public Collection<? extends ProductRecord> getProductsByGln(String gln)
    {
        return m_productRecordDao.getAllByGlnAndFilter(gln, null, null);
    }
    
/*    
    @Override
    public Collection<? extends ProductRecord> getProductsForReport()
    {
        return null;
    }    
*/
    
    @Override
    public Long getProductsForReport()
    {
        return null;
    }        

    @Override
    public Long getProductsForReportByDate()
    {
        return null;
    }        
    
    @Override
    public ProductRecord getProductByGtin(final String gtin)
    {
        return m_productRecordDao.getByFilter(new MockDao.Filter<MockProductRecord>()
        {

            @Override
            public boolean include(MockProductRecord elt)
            {
                return elt.getGtin().equals(gtin);
            }
        });
    }

    @Override
    public ProductRecord getProductByGlnAndGtin(String gln, final String gtin)
    {
        return m_productRecordDao.getByGlnAndFilter(gln, new MockDao.Filter<MockProductRecord>()
        {

            @Override
            public boolean include(MockProductRecord elt)
            {
                return elt.getGtin().equals(gtin);
            }
        });
    }

    @Override
    public Collection<? extends ProductRecord> getAllProducts()
    {
        return m_productRecordDao.getAll();
    }
    
    @Override
    public Collection<? extends ProductRecord> getProductsBasedOnGpcAndTargetMarket(String gpc, String marketCode, String startIndex, String maxSize)
    {
        return null;
    }
    
    @Override
    public void updateProduct(ProductRecord product)
    {
        m_productRecordDao.update((MockProductRecord)product);
    }

    @Override
    public void deleteProduct(ProductRecord product)
    {
        m_productRecordDao.delete((MockProductRecord)product);
    }
    

   
    @Override
    public ImportFile newImportFile()
    {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public ImportFile getImportFile(String id)
    {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void updateImportFile(ImportFile importFile)
    {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void deleteImportFile(ImportFile importFile)
    {
        // TODO Auto-generated method stub
        
    }

    @Override
    public ImportRecord newImportRecord()
    {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public ImportRecord getImportRecord(String id)
    {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public ImportRecord getImportRecordByGlnAndId(String gln, String importId)
    {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Collection<? extends ImportRecord> getImportRecordsByGln(String gln)
    {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void updateImportRecord(ImportRecord importRecord)
    {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void deleteImportRecord(ImportRecord importRecord)
    {
        // TODO Auto-generated method stub
        
    }

    @Override
    public SalesOrderRecord newSalesOrder()
    {
        return new MockSalesOrderRecord();
    }


    @Override
    public SalesOrderRecord getSalesOrder(String salesOrderId)
    {
        return m_salesOrderDao.getById(salesOrderId);
    }
    
    

    @Override
    public SalesOrderRecord getSalesOrderBySalesOrderId(final String salesOrderId)
    {
        return m_salesOrderDao.getByFilter(new MockDao.Filter<MockSalesOrderRecord>()
        {

            @Override
            public boolean include(MockSalesOrderRecord elt)
            {
                return salesOrderId.equals(elt.getOrderId());
            }
        });
    }
    
    @Override
    public Collection<? extends SalesOrderRecord> getUninvoicedSalesOrdersByGln(final String gln)
    {
        return m_salesOrderDao.getAllByFilter(new MockDao.Filter<MockSalesOrderRecord>()
        {
            @Override
            public boolean include(MockSalesOrderRecord elt)
            {
                return elt.getInvoiceId() == null && gln.equals(elt.getGBAccountGln());
            }
        },
        new Comparator<MockSalesOrderRecord>()
        {
            
            @Override
            public int compare(MockSalesOrderRecord o1, MockSalesOrderRecord o2)
            {
                return -(o1.getDate().compareTo(o2.getDate()));
            }
        });
    }
    @Override
    public Collection<? extends SalesOrderRecord> getUninvoicedSalesOrders()
    {
        return m_salesOrderDao.getAllByFilter(new MockDao.Filter<MockSalesOrderRecord>()
        {
            @Override
            public boolean include(MockSalesOrderRecord elt)
            {
                return elt.getInvoiceId() == null;
            }
        },
        new Comparator<MockSalesOrderRecord>()
        {
            
            @Override
            public int compare(MockSalesOrderRecord o1, MockSalesOrderRecord o2)
            {
                return -(o1.getDate().compareTo(o2.getDate()));
            }
        });
    }

    @Override
    public Collection<? extends SalesOrderRecord> getSalesOrdersByInvoiceId(final String invoiceId)
    {
        return m_salesOrderDao.getAllByFilter(new MockDao.Filter<MockSalesOrderRecord>()
        {
            @Override
            public boolean include(MockSalesOrderRecord elt)
            {
                return invoiceId.equals(elt.getInvoiceId());
            }
        }, null);
    }

    @Override
    public void updateSalesOrder(SalesOrderRecord salesOrder)
    {
        m_salesOrderDao.update((MockSalesOrderRecord)salesOrder);
    }

    @Override
    public void deleteSalesOrder(SalesOrderRecord salesOrder)
    {
        m_salesOrderDao.delete((MockSalesOrderRecord)salesOrder);
    }
    
    
    @Override
    public synchronized String newSalesOrderId()
    {
        String id = String.valueOf(m_nextSalesOrderId);
        m_nextSalesOrderId++;
        return id;
    }

    @Override
    public InvoiceExtra newInvoiceExtra(String itemCode, String itemDescription, String[] itemParameters, Amount total)
    {
        return new MockInvoiceExtra(itemCode, itemDescription, itemParameters, total);
    }

    @Override
    public InvoiceRecord newInvoiceRecord()
    {
        return new MockInvoiceRecord();
    }

    @Override
    public InvoiceRecord getInvoiceRecord(String id)
    {
        return m_invoiceRecordDao.getById(id);
    }
    
    @Override
    public InvoiceRecord getInvoiceRecordByInvoiceId(final String invoiceId)
    {
        return m_invoiceRecordDao.getByFilter(new MockDao.Filter<MockInvoiceRecord>()
        {

            @Override
            public boolean include(MockInvoiceRecord elt)
            {
                return invoiceId.equals(elt.getInvoiceId());
            }
        });
    }

    @Override
    public Collection<? extends InvoiceRecord> getAllInvoiceRecords()
    {
        return m_invoiceRecordDao.getAllByFilter(null, new Comparator<MockInvoiceRecord>()
        {

            @Override
            public int compare(MockInvoiceRecord o1, MockInvoiceRecord o2)
            {
                return -o1.getDate().compareTo(o2.getDate());
            }
        });
    }

    @Override
    public Collection<? extends InvoiceRecord> getInvoiceRecordsByGln(String gln)
    {
        return m_invoiceRecordDao.getAllByGlnAndFilter(gln, null, new Comparator<MockInvoiceRecord>()
        {

            @Override
            public int compare(MockInvoiceRecord o1, MockInvoiceRecord o2)
            {
                return -o1.getDate().compareTo(o2.getDate());
            }
        });
    }

    @Override
    public Collection<? extends InvoiceRecord> getAllInvoiceRecordsByOrderStatus(final OrderStatus orderStatus)
    {
        return m_invoiceRecordDao.getAllByFilter(new MockDao.Filter<MockInvoiceRecord>()
        {
            @Override
            public boolean include(MockInvoiceRecord elt)
            {
                return elt.getOrderStatus() == orderStatus;
            }
        }, 
        new Comparator<MockInvoiceRecord>()
        {

            @Override
            public int compare(MockInvoiceRecord o1, MockInvoiceRecord o2)
            {
                return -o1.getDate().compareTo(o2.getDate());
            }
        });
    }

    @Override
    public Collection<? extends InvoiceRecord> getInvoiceRecordsByGlnAndOrderStatus(String gln, final OrderStatus orderStatus)
    {
        return m_invoiceRecordDao.getAllByGlnAndFilter(gln, new MockDao.Filter<MockInvoiceRecord>()
        {
            @Override
            public boolean include(MockInvoiceRecord elt)
            {
                return elt.getOrderStatus() == orderStatus;
            }
        }, 
        new Comparator<MockInvoiceRecord>()
        {

            @Override
            public int compare(MockInvoiceRecord o1, MockInvoiceRecord o2)
            {
                return -o1.getDate().compareTo(o2.getDate());
            }
        });
    }
    
    @Override
    public Collection<? extends InvoiceRecord> getInvoiceRecordsByPaymentId(final String paymentId)
    {
        return m_invoiceRecordDao.getAllByFilter(new MockDao.Filter<MockInvoiceRecord>()
        {

            @Override
            public boolean include(MockInvoiceRecord elt)
            {
                return paymentId.equals(elt.getPaymentId());
            }
        }, 
        null);
    }

    @Override
    public void updateInvoiceRecord(InvoiceRecord invoiceRecord)
    {
        m_invoiceRecordDao.update((MockInvoiceRecord)invoiceRecord);
    }

    @Override
    public void deleteInvoiceRecord(InvoiceRecord invoiceRecord)
    {
        m_invoiceRecordDao.delete((MockInvoiceRecord)invoiceRecord);
    }

    
    @Override
    public synchronized String newInvoiceId()
    {
        String id = String.valueOf(m_nextInvoiceId);
        m_nextInvoiceId++;
        return id;
    }

    @Override
    public PaymentRecord newPayment()
    {
        return new MockPaymentRecord();
    }
    
    @Override
    public PaymentRecord getPaymentByPaymentId(final String paymentId)
    {
        return m_paymentDao.getByFilter(new MockDao.Filter<MockPaymentRecord>()
        {

            @Override
            public boolean include(MockPaymentRecord elt)
            {
                return paymentId.equals(elt.getPaymentId());
            }
        });
    }
    
    
    @Override
    public Collection<? extends PaymentRecord> getAllPaymentsByStatus(final OrderStatus status)
    {
        return m_paymentDao.getAllByFilter(new MockDao.Filter<MockPaymentRecord>()
        {

            @Override
            public boolean include(MockPaymentRecord elt)
            {
                return elt.getStatus().equals(status);
            }
        }, null);
    }


    @Override
    public Collection<? extends PaymentRecord> getPaymentsByGln(String gln)
    {
        return m_paymentDao.getAllByGlnAndFilter(gln, null, null);
    }

    @Override
    public void updatePayment(PaymentRecord payment)
    {
        m_paymentDao.update((MockPaymentRecord)payment);
    }

    @Override
    public void deletePayment(PaymentRecord payment)
    {
        m_paymentDao.delete((MockPaymentRecord)payment);
    }

    
    
    @Override
    public synchronized String newPaymentId()
    {
        String id = String.valueOf(m_nextPaymentId);
        m_nextPaymentId++;
        return id;
    }

    @Override
    public AuditEventRecord newAuditEvent()
    {
        return new MockAuditEventRecord();
    }

    @Override
    public void updateAuditEvent(AuditEventRecord event)
    {
        m_auditEventDao.update((MockAuditEventRecord)event);
        
    }
    
    public Integer getTargetMarketNotBasedOnId(String value)
    {
    	return null;
    }

    @Override
    public Collection<? extends ProductRecord> getProductsForPagination(String gln, String startIndex, String maxSize)
    {
        return null;
    }    
    
    @Override
    public Long getProductsCountBasedOnGpcAndTargetMarket(String gpc, String marketCode)
    {
        return null;
    }
    
    @Override
    public Long getRegisteredProductsCount(String gln)
    {
        return null;
    }            
    
}