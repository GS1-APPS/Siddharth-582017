package org.gs1us.sgg.gbservice.json;

import org.gs1us.sgg.gbservice.api.Amount;
import org.gs1us.sgg.gbservice.api.InvoiceExtra;
import org.gs1us.sgg.gbservice.api.OrderLineItem;

public class InboundInvoiceExtra implements InvoiceExtra
{
    private String m_itemCode;
    private String m_itemDescription;
    private String[] m_itemParameters;
    private Amount m_total;
    
    public String getItemCode()
    {
        return m_itemCode;
    }
    public void setItemCode(String itemCode)
    {
        m_itemCode = itemCode;
    }
    public String getItemDescription()
    {
        return m_itemDescription;
    }
    public void setItemDescription(String itemDescription)
    {
        m_itemDescription = itemDescription;
    }
    public String[] getItemParameters()
    {
        return m_itemParameters;
    }
    public void setItemParameters(String[] itemParameters)
    {
        m_itemParameters = itemParameters;
    }

    public Amount getTotal()
    {
        return m_total;
    }
    public void setTotal(Amount total)
    {
        m_total = total;
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
