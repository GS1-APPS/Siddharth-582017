package org.gs1us.sgg.dao.jpa;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.Basic;
import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.gs1us.sgg.dao.ProductRecord;
import org.gs1us.sgg.daoutil.KeyMapper;
import org.gs1us.sgg.daoutil.jpa.LongKeyMapper;
import org.gs1us.sgg.gbservice.api.AttributeSet;
import org.hibernate.annotations.Type;

@Entity
@Table(name="product")
public class JpaProductRecord  extends ProductRecord
{
    protected static final KeyMapper<Long> KEY_MAPPER = new LongKeyMapper();
    
    public JpaProductRecord()
    {
    }
    
    // TODO: Remove this after API is set up
    public JpaProductRecord(JpaProductRecord o)
    {
        m_id = o.m_id;
        m_gbAccountGln = o.m_gbAccountGln;
        m_gtin = o.m_gtin;
        m_dataAccuracyAckUser = o.m_dataAccuracyAckUser;
        m_productAttributes = new JpaAttributeSet(o.m_productAttributes);
        m_registeredDate = o.m_registeredDate;
        m_modifiedDate = o.m_modifiedDate;
        m_nextActionDate = o.m_nextActionDate;
        m_pendingNextActionDate = o.m_pendingNextActionDate;
        m_TargetCountryCodeId = o.m_TargetCountryCodeId;
        m_GpcCategoryCode = o.m_GpcCategoryCode;        
        //m_pendingOrderIds = o.m_pendingOrderIds;
        if (o.m_pendingOrderIdSet != null)
            m_pendingOrderIdSet = new StringSet(o.m_pendingOrderIdSet);                
    }
    
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private long m_id;

    @Basic
    private String m_gbAccountGln;

    @Basic
    private String m_gtin;

    @Basic
    private String m_dataAccuracyAckUser;

    @Basic
    private JpaAttributeSet m_productAttributes;

    @Basic
    private Date m_registeredDate;

    @Basic
    private Date m_modifiedDate;

    @Basic
    private Date m_nextActionDate;

    @Basic
    private Date m_pendingNextActionDate;
    
    @Basic
    @Convert(converter = StringSetConverter.class)
    private StringSet m_pendingOrderIdSet;

    @Basic
    private Integer m_TargetCountryCodeId;

    @Basic
    private String m_GpcCategoryCode;
    
    public Integer getTargetCountryCode()
    {
        return m_TargetCountryCodeId;
    }
    public void setTargetCountryCode(Integer targetCountryCode)
    {
    	m_TargetCountryCodeId = targetCountryCode;
    }

    public String getGpcCategoryCode()
    {
        return m_GpcCategoryCode;
    }
    public void setGpcCategoryCode(String gpcCategoryCode)
    {
    	m_GpcCategoryCode = gpcCategoryCode;
    }
        
    public String getId()
    {
        return KEY_MAPPER.keyToId(m_id);
    }

    public void setId(String id)
    {
        m_id = KEY_MAPPER.idToKey(id);
    }

    public String getGBAccountGln()
    {
        return m_gbAccountGln;
    }
    public void setGBAccountGln(String gbAccountGln)
    {
        m_gbAccountGln = gbAccountGln;
    }
    public String getGtin()
    {
        return m_gtin;
    }
    public void setGtin(String gtin)
    {
        m_gtin = gtin;
    }
    public String getDataAccuracyAckUser()
    {
        return m_dataAccuracyAckUser;
    }
    public void setDataAccuracyAckUser(String dataAccuracyAckUser)
    {
        m_dataAccuracyAckUser = dataAccuracyAckUser;
    }

    public AttributeSet getAttributes()
    {
        return m_productAttributes;
    }

    public void setAttributes(AttributeSet productAttributes)
    {
        m_productAttributes = new JpaAttributeSet(productAttributes);
    }

    public Date getRegisteredDate()
    {
        return m_registeredDate;
    }
    public void setRegisteredDate(Date registeredDate)
    {
        m_registeredDate = registeredDate;
    }
    public Date getModifiedDate()
    {
        return m_modifiedDate;
    }
    public void setModifiedDate(Date modifiedDate)
    {
        m_modifiedDate = modifiedDate;
    }
    public Date getNextActionDate()
    {
        return m_nextActionDate;
    }
    public void setNextActionDate(Date nextActionDate)
    {
        m_nextActionDate = nextActionDate;
    }
    public Date getPendingNextActionDate()
    {
        return m_pendingNextActionDate;
    }
    public void setPendingNextActionDate(Date pendingNextActionDate)
    {
        m_pendingNextActionDate = pendingNextActionDate;
    }

    @Override
    public String[] getPendingOrderIds()
    {
        if (m_pendingOrderIdSet == null || m_pendingOrderIdSet.size() == 0)
            return null;
        else
            return m_pendingOrderIdSet.toArray(new String[0]);
    }

    @Override
    public void addPendingOrderId(String invoiceId)
    {
        if (m_pendingOrderIdSet == null)
            m_pendingOrderIdSet = new StringSet();
        m_pendingOrderIdSet.add(invoiceId);        
    }

    @Override
    public void removePendingOrderId(String invoiceId)
    {
        if (m_pendingOrderIdSet == null)
            return;
        m_pendingOrderIdSet.remove(invoiceId);        
    }
    
    

}
