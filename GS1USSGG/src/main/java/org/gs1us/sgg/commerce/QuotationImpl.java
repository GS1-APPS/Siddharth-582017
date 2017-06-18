package org.gs1us.sgg.commerce;

import java.util.Collection;

import org.gs1us.sgg.gbservice.api.OrderLineItem;
import org.gs1us.sgg.gbservice.api.Quotation;

public class QuotationImpl implements Quotation
{
    private Collection<OrderLineItem> m_lineItems;

    public QuotationImpl(Collection<OrderLineItem> lineItems)
    {
        super();
        m_lineItems = lineItems;
    }

    public Collection<OrderLineItem> getLineItems()
    {
        return m_lineItems;
    }


}
