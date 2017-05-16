package org.gs1us.sgg.dao.jpa;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Order;
import javax.persistence.criteria.ParameterExpression;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.transaction.Transactional;

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
import org.gs1us.sgg.gbservice.api.OrderLineItem;
import org.gs1us.sgg.gbservice.api.OrderStatus;

@Transactional
public class JpaGBDao implements GBDao
{
    @PersistenceContext
    private EntityManager m_entityManager;
    
    @Resource
    private JpaNextIdDao m_nextIdDao;
    /*
    private NextIdManager m_salesOrderIdManager = new NextIdManager("salesOrder", 101);
    private NextIdManager m_invoiceIdManager = new NextIdManager("invoice", 1001);
    private NextIdManager m_paymentIdManager = new NextIdManager("payment", 10001);
    */
    @Override
    public GBAccountRecord newGBAccountRecord()
    {
        JpaGBAccountRecord result = new JpaGBAccountRecord();
        result.setAttributes(new JpaAttributeSet());
        return result;
    }

    @Override
    public GBAccountRecord getGBAccountByGln(String gln)
    {
        QueryBuilder<JpaGBAccountRecord> qb = new QueryBuilder<>(JpaGBAccountRecord.class);
        qb.whereEq("m_gbAccountGln", gln);
        return qb.queryOne();
    }

    @Override
    public Collection<? extends GBAccountRecord> getAllGBAccounts()
    {
        QueryBuilder<JpaGBAccountRecord> qb = new QueryBuilder<>(JpaGBAccountRecord.class);
        return qb.queryAll();
    }

    @Override
    public void updateGBAccount(GBAccountRecord gbAccount)
    {
        // For JPA, don't need to do anything if item is already persisted.
        // If it's not persisted, the id will be null
        if (((JpaGBAccountRecord)gbAccount).getId() == null)
        {
            m_entityManager.persist(gbAccount);
        }
    }

    @Override
    public void deleteGBAccount(GBAccountRecord gbAccount)
    {
        m_entityManager.remove(gbAccount);
    }
 
    @Override
    public GcpRecord newGcpRecord(String gcp, String gln)
    {
        JpaGcpRecord result = new JpaGcpRecord();
        result.setGcp(gcp);
        result.setGln(gln);
        return result;
    }

    @Override
    public Collection<? extends GcpRecord> getAllGcpRecords()
    {
        QueryBuilder<JpaGcpRecord> qb = new QueryBuilder<>(JpaGcpRecord.class);
        return qb.queryAll();
    }

    @Override
    public Collection<? extends GcpRecord> getMatchingGcpRecords(String gcp)
    {
        Collection<String> substrings = new ArrayList<>(gcp.length());
        // i <= length-1 because the "like" query will pick up an exact match, too.
        for (int i = 1; i <= gcp.length()-1; i++)
            substrings.add(gcp.substring(0, i));
        
        CriteriaBuilder cb = m_entityManager.getCriteriaBuilder();
        CriteriaQuery<JpaGcpRecord> query =  cb.createQuery(JpaGcpRecord.class);
        Root<JpaGcpRecord> root = query.from(JpaGcpRecord.class);
        query.where(cb.or(root.get("m_gcp").in(substrings), cb.like(root.get("m_gcp").as(String.class), gcp + "%")));

        return m_entityManager.createQuery(query).getResultList();
    }

    @Override
    public Collection<? extends GcpRecord> getGcpRecordsByGln(String gln)
    {
        QueryBuilder<JpaGcpRecord> qb = new QueryBuilder<>(JpaGcpRecord.class);
        qb.whereEq("m_gln", gln);
        return qb.queryAll();
    }

    @Override
    public void updateGcpRecord(GcpRecord gcpRecord)
    {
        // For JPA, don't need to do anything if item is already persisted.
        // If it's not persisted, the id will be null
        if (gcpRecord.getId() == null)
        {
            m_entityManager.persist(gcpRecord);
        }    
    }

    @Override
    public void deleteGcpRecord(GcpRecord gcpRecord)
    {
        m_entityManager.remove(gcpRecord);        
    }

    @Override
    public AppSubscriptionRecord newAppSubscription()
    {
        return new JpaAppSubscriptionRecord();
    }

    @Override
    public Collection<? extends AppSubscriptionRecord> getAppSubscriptionsByGln(String gln)
    {
        QueryBuilder<JpaAppSubscriptionRecord> qb = new QueryBuilder<>(JpaAppSubscriptionRecord.class);
        qb.whereEq("m_gbAccountGln", gln);
        qb.orderByAsc("m_appId");
        return qb.queryAll();
    }

    @Override
    public AppSubscriptionRecord getAppSubscription(String gln, String appId)
    {
        QueryBuilder<JpaAppSubscriptionRecord> qb = new QueryBuilder<>(JpaAppSubscriptionRecord.class);
        qb.whereEq("m_gbAccountGln", gln);
        qb.whereEq("m_appId", appId);
        return qb.queryOne();
    }

    @Override
    public Collection<? extends AppSubscriptionRecord> getAllAppSubscriptions()
    {
        QueryBuilder<JpaAppSubscriptionRecord> qb = new QueryBuilder<>(JpaAppSubscriptionRecord.class);
        return qb.queryAll();
    }

    @Override
    public void updateAppSubscription(AppSubscriptionRecord appSubscription)
    {
        // For JPA, don't need to do anything if item is already persisted.
        // If it's not persisted, the id will be null
        if (appSubscription.getId() == null)
        {
            m_entityManager.persist(appSubscription);
        }
    }

    @Override
    public void deleteAppSubscription(AppSubscriptionRecord appSubscription)
    {
        m_entityManager.remove(appSubscription);
    }

    @Override
    public ProductRecord newProduct()
    {
        JpaProductRecord result = new JpaProductRecord();
        result.setAttributes(new JpaAttributeSet());
        return result;
    }

    @Override
    public Collection<? extends ProductRecord> getProductsByGln(String gln)
    {
        QueryBuilder<JpaProductRecord> qb = new QueryBuilder<>(JpaProductRecord.class);
        qb.whereEq("m_gbAccountGln", gln);
        qb.orderByDesc("m_modifiedDate");
        return qb.queryAll();
    }

    
    @Override
    public Long getProductsForReportByDate()
    {    	    	
    	Calendar calendar = Calendar.getInstance();
    	calendar.add(Calendar.DAY_OF_MONTH, -60);
    	Date queryDate = calendar.getTime();    	
    	final DateFormat fmt = new SimpleDateFormat("dd-MMM-yyyy");
    	String qDate = fmt.format(queryDate);
    	Query query = m_entityManager.createNativeQuery("select count(*) from product where modified_date <= '" + qDate + "'");
    	java.math.BigInteger obj = (java.math.BigInteger) query.getSingleResult();
    	return (Long) obj.longValue();
    }

    public Integer getTargetMarketNotBasedOnId(String value)
    {
    	String sqlQuery = "select id from iso_country_ref where ( country_name = '" + value + "' or country_code_txt2 = '" + value + "' ";
    	sqlQuery = sqlQuery + " or country_code_txt3 = '" + value + "' )" ; 
    	Query query = m_entityManager.createNativeQuery(sqlQuery);
    	List results = query.getResultList();
    	Integer retval = new Integer(0);
    	
    	if (results.isEmpty())
    	{
    		try 
    		{
    		    return Integer.parseInt(value);
    		} 
    		catch (NumberFormatException e) 
    		{
    		    return retval;
    		}    		
    	}
    	else
    	{
    		return (Integer) results.get(0);
    	}
    }
    
    @Override
    public Long getProductsForReport()
    {    	    	
    	CriteriaBuilder qb = m_entityManager.getCriteriaBuilder();
    	CriteriaQuery<Long> cq = qb.createQuery(Long.class);
    	cq.select(qb.count(cq.from(JpaProductRecord.class)));
    	return m_entityManager.createQuery(cq).getSingleResult(); 
    }
        
    @Override
    public Collection<? extends ProductRecord> getProductsBasedOnGpcAndTargetMarket(String gpc, String marketCode)
    {
        QueryBuilder<JpaProductRecord> qb = new QueryBuilder<>(JpaProductRecord.class);
        qb.whereEq("m_GpcCategoryCode", gpc);
        qb.whereEq("m_TargetCountryCodeId", Integer.parseInt(marketCode));
        qb.orderByDesc("m_modifiedDate");
        return qb.queryAll();
    }
    
    @Override
    public ProductRecord getProductByGtin(String gtin)
    {
        QueryBuilder<JpaProductRecord> qb = new QueryBuilder<>(JpaProductRecord.class);
        qb.whereEq("m_gtin", gtin);
        qb.orderByDesc("m_modifiedDate");
        return qb.queryOne();
    }

    @Override
    public Collection<? extends IsoCountryRefRecord> getAllIsoCountryRef()
    {
        QueryBuilder<JpaIsoCountryRefRecord> qb = new QueryBuilder<>(JpaIsoCountryRefRecord.class);
        qb.orderByAsc("m_country_name");
        return qb.queryAll();
    }
        
    @Override
    public ProductRecord getProductByGlnAndGtin(String gln, String gtin)
    {
        QueryBuilder<JpaProductRecord> qb = new QueryBuilder<>(JpaProductRecord.class);
        qb.whereEq("m_gbAccountGln", gln);
        qb.whereEq("m_gtin", gtin);
        qb.orderByDesc("m_modifiedDate");
        return qb.queryOne();
    }

    @Override
    public Collection<? extends ProductRecord> getAllProducts()
    {
        QueryBuilder<JpaProductRecord> qb = new QueryBuilder<>(JpaProductRecord.class);
        qb.orderByDesc("m_modifiedDate");
        return qb.queryAll();
    }

    @Override
    public void updateProduct(ProductRecord product)
    {
        // For JPA, don't need to do anything if item is already persisted.
        // If it's not persisted, the id will be null
        if (product.getId() == null)
        {
            m_entityManager.persist(product);
        }
    }

    @Override
    public void deleteProduct(ProductRecord product)
    {
        m_entityManager.remove(product);
    }
    
    @Override
    public ImportFile newImportFile()
    {
        return new JpaImportFile();
    }

    @Override
    public ImportFile getImportFile(String id)
    {
        return m_entityManager.find(JpaImportFile.class, JpaImportFile.KEY_MAPPER.idToKey(id));
    }

    @Override
    public void updateImportFile(ImportFile importFile)
    {
        // For JPA, don't need to do anything if item is already persisted.
        // If it's not persisted, the id will be null
        if (importFile.getId() == null)
        {
            m_entityManager.persist(importFile);
        }
    }

    @Override
    public void deleteImportFile(ImportFile importFile)
    {
        m_entityManager.remove(importFile);
    }

    @Override
    public ImportRecord newImportRecord()
    {
        return new JpaImportRecord();
    }

    @Override
    public ImportRecord getImportRecord(String id)
    {
        return m_entityManager.find(JpaImportRecord.class, JpaImportRecord.KEY_MAPPER.idToKey(id));
    }
    
    

    @Override
    public ImportRecord getImportRecordByGlnAndId(String gln, String importId)
    {
        QueryBuilder<JpaImportRecord> qb = new QueryBuilder<>(JpaImportRecord.class);
        qb.whereEq("m_id", JpaImportRecord.KEY_MAPPER.idToKey(importId));
        qb.whereEq("m_gbAccountGln", gln);
        return qb.queryOne();
    }

    @Override
    public Collection<? extends ImportRecord> getImportRecordsByGln(String gln)
    {
        QueryBuilder<JpaImportRecord> qb = new QueryBuilder<>(JpaImportRecord.class);
        qb.whereEq("m_gbAccountGln", gln);
        qb.orderByDesc("m_uploadDate");
        return qb.queryAll();
    }

    @Override
    public void updateImportRecord(ImportRecord importRecord)
    {
        // For JPA, don't need to do anything if item is already persisted.
        // If it's not persisted, the id will be null
        if (importRecord.getId() == null)
        {
            m_entityManager.persist(importRecord);
        }
    }

    @Override
    public void deleteImportRecord(ImportRecord importRecord)
    {
        m_entityManager.remove(importRecord);
    }

    @Override
    public SalesOrderRecord newSalesOrder()
    {
        return new JpaSalesOrderRecord();
    }

    @Override
    public SalesOrderRecord getSalesOrder(String id)
    {
        return m_entityManager.find(JpaSalesOrderRecord.class, JpaSalesOrderRecord.KEY_MAPPER.idToKey(id));
    }

    @Override
    public Collection<? extends SalesOrderRecord> getUninvoicedSalesOrders()
    {
        QueryBuilder<JpaSalesOrderRecord> qb = new QueryBuilder<>(JpaSalesOrderRecord.class);
        qb.whereIsNull("m_invoiceId");
        qb.orderByDesc("m_date");
        return qb.queryAll();
    }

    @Override
    public Collection<? extends SalesOrderRecord> getUninvoicedSalesOrdersByGln(String gln)
    {
        QueryBuilder<JpaSalesOrderRecord> qb = new QueryBuilder<>(JpaSalesOrderRecord.class);
        qb.whereEq("m_gbAccountGln", gln);
        qb.whereIsNull("m_invoiceId");
        qb.orderByDesc("m_date");
        return qb.queryAll();
    }

    @Override
    public Collection<? extends SalesOrderRecord> getSalesOrdersByInvoiceId(String invoiceId)
    {
        QueryBuilder<JpaSalesOrderRecord> qb = new QueryBuilder<>(JpaSalesOrderRecord.class);
        qb.whereEq("m_invoiceId", invoiceId);
        return qb.queryAll();
    }

    @Override
    public SalesOrderRecord getSalesOrderBySalesOrderId(String salesOrderId)
    {
        QueryBuilder<JpaSalesOrderRecord> qb = new QueryBuilder<>(JpaSalesOrderRecord.class);
        qb.whereEq("m_orderId", salesOrderId);
        return qb.queryOne();
    }

    @Override
    public void updateSalesOrder(SalesOrderRecord salesOrder)
    {
        // For JPA, don't need to do anything if item is already persisted.
        // If it's not persisted, the id will be null
        if (salesOrder.getId() == null)
        {
            for (OrderLineItem lineItem : salesOrder.getLineItems())
                m_entityManager.persist(lineItem);
            m_entityManager.persist(salesOrder);
        }
    }

    @Override
    public void deleteSalesOrder(SalesOrderRecord salesOrder)
    {
        m_entityManager.remove(salesOrder);
    }

    @Override
    public String newSalesOrderId()
    {
        //return String.valueOf(m_salesOrderIdManager.nextId());
        return m_nextIdDao.newSalesOrderId();
    }
    
    @Override
    public InvoiceExtra newInvoiceExtra(String itemCode, String itemDescription, String[] itemParameters, Amount total)
    {
        JpaInvoiceExtra result = new JpaInvoiceExtra();
        result.setItemCode(itemCode);
        result.setItemDescription(itemDescription);
        result.setItemParameters(itemParameters);
        result.setTotal(total);
        return result;
    }


    @Override
    public InvoiceRecord newInvoiceRecord()
    {
        return new JpaInvoiceRecord();
    }

    @Override
    public InvoiceRecord getInvoiceRecord(String id)
    {
        return m_entityManager.find(JpaInvoiceRecord.class, JpaInvoiceRecord.KEY_MAPPER.idToKey(id));
    }

    @Override
    public InvoiceRecord getInvoiceRecordByInvoiceId(String invoiceId)
    {
        QueryBuilder<JpaInvoiceRecord> qb = new QueryBuilder<>(JpaInvoiceRecord.class);
        qb.whereEq("m_invoiceId", invoiceId);
        return qb.queryOne();
    }

    @Override
    public Collection<? extends InvoiceRecord> getAllInvoiceRecords()
    {
        QueryBuilder<JpaInvoiceRecord> qb = new QueryBuilder<>(JpaInvoiceRecord.class);
        qb.orderByDesc("m_date");
        return qb.queryAll();
    }

    @Override
    public Collection<? extends InvoiceRecord> getInvoiceRecordsByGln(String gln)
    {
        QueryBuilder<JpaInvoiceRecord> qb = new QueryBuilder<>(JpaInvoiceRecord.class);
        qb.whereEq("m_gbAccountGln", gln);
        qb.orderByDesc("m_date");
        return qb.queryAll();
    }

    @Override
    public Collection<? extends InvoiceRecord> getAllInvoiceRecordsByOrderStatus(OrderStatus orderStatus)
    {
        QueryBuilder<JpaInvoiceRecord> qb = new QueryBuilder<>(JpaInvoiceRecord.class);
        qb.whereEq("m_orderStatus", orderStatus);
        qb.orderByDesc("m_date");
        qb.orderByDesc("m_invoiceId");  // TODO: hack for testing!
        return qb.queryAll();
    }

    @Override
    public Collection<? extends InvoiceRecord> getInvoiceRecordsByGlnAndOrderStatus(
            String gln, OrderStatus orderStatus)
    {
        QueryBuilder<JpaInvoiceRecord> qb = new QueryBuilder<>(JpaInvoiceRecord.class);
        qb.whereEq("m_gbAccountGln", gln);
        qb.whereEq("m_orderStatus", orderStatus);
        qb.orderByDesc("m_date");
        return qb.queryAll();
    }

    @Override
    public Collection<? extends InvoiceRecord> getInvoiceRecordsByPaymentId(String paymentId)
    {
        QueryBuilder<JpaInvoiceRecord> qb = new QueryBuilder<>(JpaInvoiceRecord.class);
        qb.whereEq("m_paymentId", paymentId);
        qb.orderByDesc("m_date");
        return qb.queryAll();
    }

    @Override
    public void updateInvoiceRecord(InvoiceRecord invoiceRecord)
    {
        // For JPA, don't need to do anything if item is already persisted.
        // If it's not persisted, the id will be null
        if (invoiceRecord.getId() == null)
        {
            for (InvoiceExtra extra : invoiceRecord.getExtras())
                m_entityManager.persist(extra);
            m_entityManager.persist(invoiceRecord);
        }
    }

    @Override
    public void deleteInvoiceRecord(InvoiceRecord invoiceRecord)
    {
        m_entityManager.remove(invoiceRecord);

    }

    @Override
    public String newInvoiceId()
    {
        //return String.valueOf(m_invoiceIdManager.nextId());
        return m_nextIdDao.newInvoiceId();
    }

    @Override
    public PaymentRecord newPayment()
    {
        return new JpaPaymentRecord();
    }

    @Override
    public PaymentRecord getPaymentByPaymentId(String paymentId)
    {
        QueryBuilder<JpaPaymentRecord> qb = new QueryBuilder<>(JpaPaymentRecord.class);
        qb.whereEq("m_paymentId", paymentId);
        return qb.queryOne();    }

    @Override
    public Collection<? extends PaymentRecord> getAllPaymentsByStatus(OrderStatus status)
    {
        QueryBuilder<JpaPaymentRecord> qb = new QueryBuilder<>(JpaPaymentRecord.class);
        qb.whereEq("m_status", status);
        qb.orderByDesc("m_date");
        return qb.queryAll();
    }

    @Override
    public Collection<? extends PaymentRecord> getPaymentsByGln(String gln)
    {
        QueryBuilder<JpaPaymentRecord> qb = new QueryBuilder<>(JpaPaymentRecord.class);
        qb.whereEq("m_gbAccountGln", gln);
        qb.orderByDesc("m_date");
        return qb.queryAll();
    }

    @Override
    public void updatePayment(PaymentRecord payment)
    {
        // For JPA, don't need to do anything if item is already persisted.
        // If it's not persisted, the id will be null
        if (payment.getId() == null)
        {
            m_entityManager.persist(payment);
        }
    }

    @Override
    public void deletePayment(PaymentRecord payment)
    {
        m_entityManager.remove(payment);
    }

    @Override
    public String newPaymentId()
    {
        //return String.valueOf(m_paymentIdManager.nextId());
        return m_nextIdDao.newPaymentId();
    }
    
    
    
    @Override
    public AuditEventRecord newAuditEvent()
    {
        return new JpaAuditEventRecord();
    }

    @Override
    public void updateAuditEvent(AuditEventRecord event)
    {
        // For JPA, don't need to do anything if item is already persisted.
        // If it's not persisted, the id will be null
        if (event.getId() == null)
        {
            m_entityManager.persist(event);
        }
    }



    private class QueryBuilder<T>
    {
        private CriteriaBuilder m_cb = m_entityManager.getCriteriaBuilder();
        private CriteriaQuery<T> m_query;
        private Root<T> m_root;
        private List<Order> m_orderBys = new ArrayList<>();
        private List<Predicate> m_predicates = new ArrayList<>();
        
        public QueryBuilder(Class<T> instanceClass)
        {
            m_query = m_cb.createQuery(instanceClass);
            m_root = m_query.from(instanceClass);
        }
        
        public QueryBuilder<T> whereEq(String fieldName, Object value)
        {
            m_predicates.add(m_cb.equal(m_root.get(fieldName), value));
            return this;
        }
        
        public QueryBuilder<T> whereLike(String fieldName, String value)
        {
            m_predicates.add(m_cb.like(m_root.get(fieldName).as(String.class), value));
            return this;
        }
        
        public QueryBuilder<T> whereIn(String fieldName, List<String> values)
        {
            m_predicates.add(m_root.get(fieldName).in(values));
            return this;
        }
        
        public QueryBuilder<T> whereIsNull(String fieldName)
        {
            m_predicates.add(m_cb.isNull(m_root.get(fieldName)));
            return this;
        }
        
        public QueryBuilder<T> orderByAsc(String fieldName)
        {
            m_orderBys.add(m_cb.asc(m_root.get(fieldName)));
            return this;
        }
        
        public QueryBuilder<T> orderByDesc(String fieldName)
        {
            m_orderBys.add(m_cb.desc(m_root.get(fieldName)));
            return this;
        }
        
        public T queryOne()
        {
            List<T> results = doQuery();
            if (results.size() == 0)
                return null;
            else
                return results.get(0);
        }

        public List<T> queryAll()
        {
            return doQuery();
        }
        
        private List<T> doQuery()
        {
            if (m_predicates.size() > 0)
                m_query.where(m_predicates.toArray(new Predicate[0]));
            if (m_orderBys.size() > 0)
                m_query.orderBy(m_orderBys);
            return m_entityManager.createQuery(m_query).getResultList();
        }
        


    }

    private class NextIdManager
    {
        private String m_name;
        private long m_initialId;
        private String m_recordId = null;
        
        public NextIdManager(String name, long initialId)
        {
            m_name = name;
            m_initialId = initialId;
        }
        
        public long nextId()
        {
            JpaNextIdRecord record;
            if (m_recordId == null)
                record = getRecordByName();
            else
                record = getRecordById();
            long result = record.getNextId();
            System.out.format("%s: Pausing while incrementing %s %d\n", new Date(), m_name, result);
            if (true)
            {
            try
            {
                Thread.sleep(15000);
            }
            catch (InterruptedException e)
            {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            }
            record.setNextId(result + 1);
            
            updateRecord(record);
            System.out.format("%s: Incremented and updated: %s %d\n", new Date(), m_name, result);
            
            return result;
        }        
        
        private JpaNextIdRecord getRecordByName()
        {
            QueryBuilder<JpaNextIdRecord> qb = new QueryBuilder<>(JpaNextIdRecord.class);
            qb.whereEq("m_name", m_name);
            JpaNextIdRecord record = qb.queryOne();
            
            if (record == null)
                record = newRecord();
            else
                m_recordId = record.getId();
            
            return record;
        }
        
        private JpaNextIdRecord getRecordById()
        {
            JpaNextIdRecord record = m_entityManager.find(JpaNextIdRecord.class, JpaNextIdRecord.KEY_MAPPER.idToKey(m_recordId));
            if (record == null)
                record = newRecord();
            return record;
        }
        
        private JpaNextIdRecord newRecord()
        {
            JpaNextIdRecord record = new JpaNextIdRecord();
            record.setName(m_name);
            record.setNextId(m_initialId);
            return record;
        }
        
        private void updateRecord(JpaNextIdRecord record)
        {
            if (record.getId() == null)
            {
                m_entityManager.persist(record);
                m_recordId = record.getId();
            }
        }
    }
}