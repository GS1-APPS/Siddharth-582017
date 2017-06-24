package org.gs1us.sgg.dao.jpa;

import java.math.BigDecimal;

import javax.persistence.Basic;
import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import org.gs1us.sgg.daoutil.KeyMapper;
import org.gs1us.sgg.daoutil.jpa.LongKeyMapper;
import org.gs1us.sgg.gbservice.api.Amount;
import org.gs1us.sgg.gbservice.api.InvoiceExtra;
import org.gs1us.sgg.gbservice.api.OrderLineItem;

@Entity
@Table(name="invoice_extra")
public class JpaInvoiceExtra implements InvoiceExtra
{
    protected static final KeyMapper<Long> KEY_MAPPER = new LongKeyMapper();

    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private long m_id;
    
    @Basic
    private String m_itemCode;
    
    @Basic
    private String m_itemDescription;
    
    @Basic
    @Convert(converter = StringArrayConverter.class)
    private StringArray m_itemParameters;
    
    @Basic
    private BigDecimal m_totalValue;
    
    @Basic
    private String m_totalCurrency;
    
    public JpaInvoiceExtra()
    {
    }

    public JpaInvoiceExtra(InvoiceExtra extra)
    {
        m_itemCode = extra.getItemCode();
        m_itemDescription = extra.getItemDescription();
        setItemParameters(extra.getItemParameters());
        setTotal(extra.getTotal());
        
    }

    public String getId()
    {
        return KEY_MAPPER.keyToId(m_id);
    }

    public void setId(String id)
    {
        m_id = KEY_MAPPER.idToKey(id);
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
        return StringArray.toArrayOrNull(m_itemParameters);
    }

    public void setItemParameters(String[] itemParameters)
    {
        m_itemParameters = StringArray.fromArrayOrNull(itemParameters);
    }

    public Amount getTotal()
    {
        if (m_totalValue == null || m_totalCurrency == null)
            return null;
        else
            return new Amount(m_totalValue, m_totalCurrency);
    }
    public void setTotal(Amount total)
    {
        if (total == null)
        {
            m_totalValue = null;
            m_totalCurrency = null;
        }
        else
        {
            m_totalValue = total.getValue();
            m_totalCurrency = total.getCurrency();
        }
    }



}
