package org.gs1us.sgg.gbservice.json;

import java.util.Date;

import org.gs1us.sgg.gbservice.api.Product;
import org.gs1us.sgg.gbservice.api.AttributeSet;

public class InboundProduct implements Product
{
    private String m_gtin;
    private String m_dataAccuracyAckUser;
    private InboundAttributeSet m_attributes;
    private Date m_registeredDate;
    private Date m_modifiedDate;
    private Date m_nextActionDate;
    private Date m_pendingNextActionDate;
    private String[] m_pendingOrderIds;
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
        return m_attributes;
    }
    public void setAttributes(InboundAttributeSet attributes)
    {
        m_attributes = attributes;
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
    public String[] getPendingOrderIds()
    {
        return m_pendingOrderIds;
    }
    public void setPendingOrderIds(String[] pendingOrderIds)
    {
        m_pendingOrderIds = pendingOrderIds;
    }
    
    
}