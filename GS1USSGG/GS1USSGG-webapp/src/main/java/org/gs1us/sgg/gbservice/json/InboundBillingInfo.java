package org.gs1us.sgg.gbservice.json;

import java.util.Date;

public class InboundBillingInfo
{
    private Date m_date;
    private String m_billingReference;
    public Date getDate()
    {
        return m_date;
    }
    public void setDate(Date date)
    {
        m_date = date;
    }
    public String getBillingReference()
    {
        return m_billingReference;
    }
    public void setBillingReference(String billingReference)
    {
        m_billingReference = billingReference;
    }
    
    
}
