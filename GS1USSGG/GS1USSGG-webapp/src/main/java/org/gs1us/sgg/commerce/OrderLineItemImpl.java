package org.gs1us.sgg.commerce;

import java.math.BigDecimal;

import org.gs1us.sgg.gbservice.api.Amount;
import org.gs1us.sgg.gbservice.api.OrderLineItem;

public class OrderLineItemImpl implements OrderLineItem
{
    private int m_quantity;
    private String m_itemCode;
    private String m_itemDescription;
    private String[] m_itemParameters;
    private Amount m_price;
    private Amount m_total;
    
    
    public OrderLineItemImpl(int quantity, String itemCode,
            String itemDescription, String[] itemParameters, Amount price)
    {
        super();
        m_quantity = quantity;
        m_itemCode = itemCode;
        m_itemDescription = itemDescription;
        m_itemParameters = itemParameters;
        m_price = price;
        m_total = new Amount(price.getValue().multiply(new BigDecimal(quantity)), price.getCurrency());
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

    @Override
    public int hashCode()
    {
        final int prime = 31;
        int result = 1;
        result = prime * result
                 + ((getItemCode() == null) ? 0 : getItemCode().hashCode());
        result = prime
                 * result
                 + ((getItemDescription() == null) ? 0 : getItemDescription()
                         .hashCode());
        result = prime * result + ((getPrice() == null) ? 0 : getPrice().hashCode());
        result = prime * result + getQuantity();
        result = prime * result + ((getTotal() == null) ? 0 : getTotal().hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj)
    {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (!(obj instanceof OrderLineItem))
            return false;
        OrderLineItem other = (OrderLineItem) obj;
        if (getItemCode() == null)
        {
            if (other.getItemCode() != null)
                return false;
        }
        else if (!getItemCode().equals(other.getItemCode()))
            return false;
        if (getItemDescription() == null)
        {
            if (other.getItemDescription() != null)
                return false;
        }
        else if (!getItemDescription().equals(other.getItemDescription()))
            return false;
        if (getPrice() == null)
        {
            if (other.getPrice() != null)
                return false;
        }
        else if (!getPrice().equals(other.getPrice()))
            return false;
        if (getQuantity() != other.getQuantity())
            return false;
        if (getTotal() == null)
        {
            if (other.getTotal() != null)
                return false;
        }
        else if (!getTotal().equals(other.getTotal()))
            return false;
        return true;
    }
    
    

}
