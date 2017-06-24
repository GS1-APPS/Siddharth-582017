package org.gs1us.sgg.gbservice.api;

import java.util.Date;

public interface AuditEvent extends GBAccountData
{
    public AuditEventType getType();
    public Date getDate();
    public String getAgentUsername();
    public String getUsername();
    public String getGBAccountGln();
    public String getGtin();
    public String getInvoiceId();
    public String getDetails();
}
