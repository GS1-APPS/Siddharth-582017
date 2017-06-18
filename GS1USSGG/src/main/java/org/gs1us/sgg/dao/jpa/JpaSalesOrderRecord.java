package org.gs1us.sgg.dao.jpa;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import javax.persistence.Basic;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OrderColumn;
import javax.persistence.Table;

import org.gs1us.sgg.dao.SalesOrderRecord;
import org.gs1us.sgg.daoutil.KeyMapper;
import org.gs1us.sgg.daoutil.jpa.LongKeyMapper;
import org.gs1us.sgg.gbservice.api.Amount;
import org.gs1us.sgg.gbservice.api.OrderLineItem;

@Entity
@Table(name="sales_order")
class JpaSalesOrderRecord extends SalesOrderRecord 
{
    protected static final KeyMapper<Long> KEY_MAPPER = new LongKeyMapper();

    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private long m_id;
    
    @Basic
    private Date m_date;
    
    @Basic
    private String m_orderId;
    
    @Basic
    private String m_gbAccountGln;
    
    @Basic
    private String m_poId;
    
    @OneToMany
    @JoinColumn(name="sales_order_id")
    @OrderColumn(name="order_line_item_index")
    private List<JpaOrderLineItem> m_lineItems;
    
    @Basic
    private String m_summary;
    
    @Basic
    private BigDecimal m_totalValue;
    
    @Basic
    private String m_totalCurrency;
    
    @Basic
    private BigDecimal m_balanceDueValue;
    
    @Basic
    private String m_balanceDueCurrency;
    
    @Basic
    private String m_invoiceId;
    
    public String getId()
    {
        return KEY_MAPPER.keyToId(m_id);
    }

    public void setId(String id)
    {
        m_id = KEY_MAPPER.idToKey(id);
    }

    public Date getDate()
    {
        return m_date;
    }
    public void setDate(Date date)
    {
        m_date = date;
    }
    public String getOrderId()
    {
        return m_orderId;
    }
    public void setOrderId(String orderId)
    {
        m_orderId = orderId;
    }
    public String getGBAccountGln()
    {
        return m_gbAccountGln;
    }
    public void setGBAccountGln(String gbAccountGln)
    {
        m_gbAccountGln = gbAccountGln;
    }
    public String getPOId()
    {
        return m_poId;
    }
    public void setPOId(String poId)
    {
        m_poId = poId;
    }

    public Collection<? extends OrderLineItem> getLineItems()
    {
        return m_lineItems;
    }
    public void setLineItems(Collection<? extends OrderLineItem> lineItems)
    {
        List<JpaOrderLineItem> jpa = new ArrayList<>(lineItems.size());
        for (OrderLineItem lineItem : lineItems)
            jpa.add(new JpaOrderLineItem(lineItem));
        m_lineItems = jpa;
    }
    
    public String getSummary()
    {
        return m_summary;
    }
    public void setSummary(String summary)
    {
        m_summary = summary;
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
    public Amount getBalanceDue()
    {
        if (m_balanceDueValue == null || m_balanceDueCurrency == null)
            return null;
        else
            return new Amount(m_balanceDueValue, m_balanceDueCurrency);

    }
    public void setBalanceDue(Amount balanceDue)
    {
        if (balanceDue == null)
        {
            m_balanceDueValue = null;
            m_balanceDueCurrency = null;
        }
        else
        {
            m_balanceDueValue = balanceDue.getValue();
            m_balanceDueCurrency = balanceDue.getCurrency();
        }

    }
    
    public String getInvoiceId()
    {
        return m_invoiceId;
    }
    public void setInvoiceId(String invoiceId)
    {
        m_invoiceId = invoiceId;
    }


}
