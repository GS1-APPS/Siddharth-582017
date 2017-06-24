package org.gs1us.sgg.gbservice.json;

import java.util.Collection;

import org.gs1us.sgg.gbservice.api.OrderLineItem;
import org.gs1us.sgg.gbservice.api.Quotation;

public class InboundQuotation implements Quotation
{
    private Collection<InboundOrderLineItem> m_lineItems;

    public Collection<? extends OrderLineItem> getLineItems()
    {
        return m_lineItems;
    }

    public void setLineItems(Collection<InboundOrderLineItem> lineItems)
    {
        m_lineItems = lineItems;
    }
    
    


}
