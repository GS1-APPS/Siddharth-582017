package org.gs1us.sgg.gbservice.json;

import java.util.Collection;
import java.util.Date;

import org.gs1us.sgg.gbservice.api.OrderLineItem;
import org.gs1us.sgg.gbservice.api.PurchaseOrder;

public class InboundPurchaseOrder implements PurchaseOrder
{
    private String m_poId;
    private Date m_date;
    private Collection<InboundOrderLineItem> m_lineItems;
    
    public String getPOId()
    {
        return m_poId;
    }
    public void setPOId(String poId)
    {
        m_poId = poId;
    }
    public Date getDate()
    {
        return m_date;
    }
    public void setDate(Date date)
    {
        m_date = date;
    }
    public Collection<? extends OrderLineItem> getLineItems()
    {
        return m_lineItems;
    }
    public void setLineItems(Collection<InboundOrderLineItem> lineItems)
    {
        m_lineItems = lineItems;
    }
    
    

}
