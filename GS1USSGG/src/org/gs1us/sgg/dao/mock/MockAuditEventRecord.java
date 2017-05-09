package org.gs1us.sgg.dao.mock;

import java.util.Date;

import org.gs1us.sgg.dao.AuditEventRecord;
import org.gs1us.sgg.gbservice.api.AuditEventType;

public class MockAuditEventRecord extends AuditEventRecord implements DaoElement<MockAuditEventRecord>, HasGln
{
    private String m_id;
    private AuditEventType m_type;
    private Date m_date;
    private String m_agentUsername;
    private String m_username;
    private String m_gbAccountGln;
    private String m_gtin;
    private String m_invoiceId;
    private String m_details;
    public String getId()
    {
        return m_id;
    }
    public void setId(String id)
    {
        m_id = id;
    }
    public AuditEventType getType()
    {
        return m_type;
    }
    public void setType(AuditEventType type)
    {
        m_type = type;
    }
    public Date getDate()
    {
        return m_date;
    }
    public void setDate(Date date)
    {
        m_date = date;
    }
    public String getAgentUsername()
    {
        return m_agentUsername;
    }
    public void setAgentUsername(String agentUsername)
    {
        m_agentUsername = agentUsername;
    }
    public String getUsername()
    {
        return m_username;
    }
    public void setUsername(String username)
    {
        m_username = username;
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
    public String getInvoiceId()
    {
        return m_invoiceId;
    }
    public void setInvoiceId(String invoiceId)
    {
        m_invoiceId = invoiceId;
    }
    public String getDetails()
    {
        return m_details;
    }
    public void setDetails(String details)
    {
        m_details = details;
    }
    @Override
    public MockAuditEventRecord newInstance()
    {
        return new MockAuditEventRecord();
    }
    @Override
    public MockAuditEventRecord updateFrom(MockAuditEventRecord from)
    {
        // WE'll never use this
        throw new UnsupportedOperationException();
    }
    

}
