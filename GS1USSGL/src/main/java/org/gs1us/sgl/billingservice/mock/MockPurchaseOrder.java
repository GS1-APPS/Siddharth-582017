package org.gs1us.sgl.billingservice.mock;

import java.util.Collection;
import java.util.Date;

import org.gs1us.sgg.gbservice.api.OrderLineItem;
import org.gs1us.sgg.gbservice.api.PurchaseOrder;

class MockPurchaseOrder implements PurchaseOrder
{
    private String m_poId;
    private Date m_date;
    private Collection<OrderLineItem> m_lineItems;
    public MockPurchaseOrder(String poId, Date date,
            Collection<OrderLineItem> lineItems)
    {
        super();
        m_poId = poId;
        m_date = date;
        m_lineItems = lineItems;
    }
    public String getPOId()
    {
        return m_poId;
    }
    public Date getDate()
    {
        return m_date;
    }
    public Collection<OrderLineItem> getLineItems()
    {
        return m_lineItems;
    }
    
    

}
