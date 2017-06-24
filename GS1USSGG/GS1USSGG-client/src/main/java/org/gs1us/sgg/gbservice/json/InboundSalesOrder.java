package org.gs1us.sgg.gbservice.json;

import java.util.Collection;
import java.util.Date;

import org.gs1us.sgg.gbservice.api.Amount;
import org.gs1us.sgg.gbservice.api.OrderLineItem;
import org.gs1us.sgg.gbservice.api.SalesOrder;

import com.fasterxml.jackson.annotation.JsonProperty;

public class InboundSalesOrder implements SalesOrder
{
    private String m_orderId;
    private String m_gbAccountGln;
    private Date m_date;
    private String m_poId;
    private Collection<InboundOrderLineItem> m_lineItems;
    private String m_summary;
    private Amount m_total;
    private String m_invoiceId;
    
    public String getOrderId()
    {
        return m_orderId;
    }
    public void setOrderId(String orderId)
    {
        m_orderId = orderId;
    }
    @JsonProperty("gln")
    public String getGBAccountGln()
    {
        return m_gbAccountGln;
    }
    @JsonProperty("gln")
    public void setGbAccountGln(String gbAccountGln)
    {
        m_gbAccountGln = gbAccountGln;
    }
    public Date getDate()
    {
        return m_date;
    }
    public void setDate(Date date)
    {
        m_date = date;
    }
    public String getPOId()
    {
        return m_poId;
    }
    public void setPOId(String poId)
    {
        m_poId = poId;
    }
    public Collection<InboundOrderLineItem> getLineItems()
    {
        return m_lineItems;
    }
    public void setLineItems(Collection<InboundOrderLineItem> lineItems)
    {
        m_lineItems = lineItems;
    }
    public String getSummary()
    {
        return m_summary;
    }
    public void setSummary(String summary)
    {
        m_summary = summary;
    }
    public Amount getTotal()
    {
        return m_total;
    }
    public void setTotal(Amount total)
    {
        m_total = total;
    }
    public String getInvoiceId()
    {
        return m_invoiceId;
    }
    public void setInvoiceId(String invoiceId)
    {
        m_invoiceId = invoiceId;
    }
    

}
