package org.gs1us.sgg.commerce;

import java.text.MessageFormat;

import org.gs1us.sgg.gbservice.api.Amount;
import org.gs1us.sgg.gbservice.api.OrderLineItem;

public class OrderableItemDesc
{
    private String m_itemCode;
    private String m_itemDescriptionFormat;
    private Amount m_defaultPrice;
    
    public OrderableItemDesc(String itemCode, String itemDescriptionFormat, Amount price)
    {
        super();
        m_itemCode = itemCode;
        m_itemDescriptionFormat = itemDescriptionFormat;
        m_defaultPrice = price;
    }
    public String getItemCode()
    {
        return m_itemCode;
    }
    public String getItemDescriptionFormat()
    {
        return m_itemDescriptionFormat;
    }
    public Amount getDefaultPrice()
    {
        return m_defaultPrice;
    }
    
    public OrderLineItem createLineItem(int quantity, String... itemParameters)
    {
        return createLineItem(quantity, null, itemParameters);
    }
    public OrderLineItem createLineItem(int quantity, Amount price, String... itemParameters)
    {
        String itemDescription = MessageFormat.format(m_itemDescriptionFormat, (Object[])itemParameters);
        return new OrderLineItemImpl(quantity, m_itemCode, itemDescription, itemParameters, price == null ? m_defaultPrice : price);
    }
}
