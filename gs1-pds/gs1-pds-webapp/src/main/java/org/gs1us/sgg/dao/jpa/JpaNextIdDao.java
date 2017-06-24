package org.gs1us.sgg.dao.jpa;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Order;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Transactional(propagation=Propagation.REQUIRES_NEW, isolation=Isolation.REPEATABLE_READ)
public class JpaNextIdDao
{
	@PersistenceContext(unitName="gs1ussggPersistenceUnit")
    private EntityManager m_entityManager;
    
    private NextIdManager m_salesOrderIdManager = new NextIdManager("salesOrder", 101);
    private NextIdManager m_invoiceIdManager = new NextIdManager("invoice", 1001);
    private NextIdManager m_paymentIdManager = new NextIdManager("payment", 10001);


    public String newSalesOrderId()
    {
        return String.valueOf(m_salesOrderIdManager.nextId());
    }

    public String newInvoiceId()
    {
        return String.valueOf(m_invoiceIdManager.nextId());
    }

    public String newPaymentId()
    {
        return String.valueOf(m_paymentIdManager.nextId());
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
            record.setNextId(result + 1);
            
            updateRecord(record);
            
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
