package org.gs1us.sgg.dao.jpa;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import javax.persistence.Basic;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OrderColumn;
import javax.persistence.Table;

import org.gs1us.sgg.dao.AuditEventRecord;
import org.gs1us.sgg.dao.InvoiceRecord;
import org.gs1us.sgg.daoutil.KeyMapper;
import org.gs1us.sgg.daoutil.jpa.LongKeyMapper;
import org.gs1us.sgg.gbservice.api.Amount;
import org.gs1us.sgg.gbservice.api.AuditEventType;
import org.gs1us.sgg.gbservice.api.InvoiceExtra;
import org.gs1us.sgg.gbservice.api.OrderLineItem;
import org.gs1us.sgg.gbservice.api.OrderStatus;

@Entity
@Table(name="audit_event")
class JpaAuditEventRecord extends AuditEventRecord 
{
    protected static final KeyMapper<Long> KEY_MAPPER = new LongKeyMapper();
    
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private long m_id;
    
    @Basic 
    private AuditEventType m_type;

    @Basic
    private Date m_date;
    
    @Basic
    private String m_agentUsername;
    
    @Basic
    private String m_username;

    @Basic
    private String m_gbAccountGln;
    
    @Basic
    private String m_gtin;

    @Basic
    private String m_invoiceId;

    @Basic
    private String m_details;
    
    public String getId()
    {
        return KEY_MAPPER.keyToId(m_id);
    }

    public void setId(String id)
    {
        m_id = KEY_MAPPER.idToKey(id);
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


}
