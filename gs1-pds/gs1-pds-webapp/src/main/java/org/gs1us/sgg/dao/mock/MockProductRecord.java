package org.gs1us.sgg.dao.mock;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import org.gs1us.sgg.dao.ProductRecord;
import org.gs1us.sgg.gbservice.api.AttributeDesc;
import org.gs1us.sgg.gbservice.api.AttributeSet;

class MockProductRecord extends ProductRecord implements DaoElement<MockProductRecord>, HasGln
{
    private String m_id;
    private String m_gtin;
    private String m_dataAccuracyAckUser;
    private String m_gbAccountGln;
    private MockAttributeSet m_attributes = new MockAttributeSet();
    private Date m_registeredDate;
    private Date m_modifiedDate;
    private Date m_nextActionDate;
    private Date m_pendingNextActionDate;
    private Set<String> m_pendingOrderIds = new HashSet<>();
    private Integer m_TargetCountryCode;
    private String m_GpcCategoryCode;        

    
    public Integer getTargetCountryCode()
    {
        return m_TargetCountryCode;
    }
    public void setTargetCountryCode(Integer countryCode)
    {
    	m_TargetCountryCode = countryCode;
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
        return m_id;
    }
    public void setId(String id)
    {
        m_id = id;
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
    public String getGBAccountGln()
    {
        return m_gbAccountGln;
    }
    public void setGBAccountGln(String gbAccountGln)
    {
        m_gbAccountGln = gbAccountGln;
    }
    public AttributeSet getAttributes()
    {
        return m_attributes;
    }

    public void setAttributes(AttributeSet attributes)
    {
        m_attributes = (MockAttributeSet)attributes;
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
        return m_pendingOrderIds.toArray(new String[0]);
    }
    @Override
    public void addPendingOrderId(String pendingOrderId)
    {
        if (pendingOrderId != null)
            m_pendingOrderIds.add(pendingOrderId);
    }
    @Override
    public void removePendingOrderId(String pendingOrderId)
    {
        if (pendingOrderId != null)
            m_pendingOrderIds.remove(pendingOrderId);
    }
    @Override
    public MockProductRecord newInstance()
    {
        return new MockProductRecord();
    }
    @Override
    public MockProductRecord updateFrom(MockProductRecord from)
    {
        setId(from.getId());
        setGtin(from.getGtin());
        setDataAccuracyAckUser(from.getDataAccuracyAckUser());
        setGBAccountGln(from.getGBAccountGln());
        m_attributes = new MockAttributeSet(from.m_attributes);
        setRegisteredDate(from.getRegisteredDate());
        setModifiedDate(from.getModifiedDate());
        setNextActionDate(from.getNextActionDate());
        setPendingNextActionDate(from.getPendingNextActionDate());
        setGpcCategoryCode(from.getGpcCategoryCode());
        setTargetCountryCode(from.getTargetCountryCode());
        m_pendingOrderIds = from.m_pendingOrderIds; // TODO: not quite right, but should be OK
        return this;
    }
    


}
