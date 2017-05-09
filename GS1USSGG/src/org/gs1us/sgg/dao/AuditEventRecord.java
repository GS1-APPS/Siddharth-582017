package org.gs1us.sgg.dao;

import java.util.Date;

import org.gs1us.sgg.gbservice.api.AuditEvent;
import org.gs1us.sgg.gbservice.api.AuditEventType;

public abstract class AuditEventRecord implements AuditEvent
{
    public abstract String getId();
   
    public abstract AuditEventType getType();
    public abstract void setType(AuditEventType type);
    
    public abstract Date getDate();
    public abstract void setDate(Date date);

    public abstract String getAgentUsername();
    public abstract void setAgentUsername(String username);
    
    public abstract String getUsername();
    public abstract void setUsername(String username);
    
    public abstract String getGBAccountGln();
    public abstract void setGBAccountGln(String gln);
    
    public abstract String getGtin();
    public abstract void setGtin(String gtin);
    
    public abstract String getInvoiceId();
    public abstract void setInvoiceId(String invoiceId);

    public abstract String getDetails();
    public abstract void setDetails(String details);


}
