package org.gs1us.sgg.gbservice.impl;

import java.util.Collection;
import java.util.Date;

import org.gs1us.sgg.gbservice.api.OrderLineItem;
import org.gs1us.sgg.gbservice.api.PurchaseOrder;

class PurchaseOrderImpl implements PurchaseOrder
{
    private String m_poId;
    private Date m_date;
    private Collection<? extends OrderLineItem> m_lineItems;
    public PurchaseOrderImpl(String poId, Date date,
            Collection<? extends OrderLineItem> lineItems)
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
    public Collection<? extends OrderLineItem> getLineItems()
    {
        return m_lineItems;
    }


}
