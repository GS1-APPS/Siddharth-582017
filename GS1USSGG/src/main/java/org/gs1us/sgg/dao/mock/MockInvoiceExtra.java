package org.gs1us.sgg.dao.mock;

import org.gs1us.sgg.gbservice.api.Amount;
import org.gs1us.sgg.gbservice.api.InvoiceExtra;

public class MockInvoiceExtra implements InvoiceExtra
{
    private String m_itemCode;
    private String m_itemDescription;
    private String[] m_itemParameters;
    private Amount m_total;
    
    public MockInvoiceExtra(String itemCode, String itemDescription,
            String[] itemParameters, Amount total)
    {
        super();
        m_itemCode = itemCode;
        m_itemDescription = itemDescription;
        m_itemParameters = itemParameters;
        m_total = total;
    }

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


}
