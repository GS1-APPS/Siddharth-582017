package org.gs1us.sgl.billingservice.mock;

import org.gs1us.sgg.gbservice.api.Amount;
import org.gs1us.sgg.gbservice.api.OrderLineItem;

class MockOrderLineItem implements OrderLineItem
{
    private int m_quantity;
    private String m_itemCode;
    private String m_itemDescription;
    private String[] m_itemParameters;
    private Amount m_price;
    private Amount m_total;
    public MockOrderLineItem(int quantity, String itemCode,
            String itemDescription, String[] itemParameters, Amount price, Amount total)
    {
        super();
        m_quantity = quantity;
        m_itemCode = itemCode;
        m_itemDescription = itemDescription;
        m_itemParameters = itemParameters;
        m_price = price;
        m_total = total;
    }
    public int getQuantity()
    {
        return m_quantity;
    }
    public String getItemCode()
    {
        return m_itemCode;
    }
    public String getItemDescription()
    {
        return m_itemDescription;
    }
    
    public String[] getItemParameters()
    {
        return m_itemParameters;
    }
    public Amount getPrice()
    {
        return m_price;
    }
    public Amount getTotal()
    {
        return m_total;
    }

    

}
