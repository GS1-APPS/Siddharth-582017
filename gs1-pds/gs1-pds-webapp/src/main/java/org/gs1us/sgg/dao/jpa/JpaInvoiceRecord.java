package org.gs1us.sgg.dao.jpa;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import javax.persistence.Basic;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OrderColumn;
import javax.persistence.Table;

import org.gs1us.sgg.dao.InvoiceRecord;
import org.gs1us.sgg.daoutil.KeyMapper;
import org.gs1us.sgg.daoutil.jpa.LongKeyMapper;
import org.gs1us.sgg.gbservice.api.Amount;
import org.gs1us.sgg.gbservice.api.InvoiceExtra;
import org.gs1us.sgg.gbservice.api.OrderLineItem;
import org.gs1us.sgg.gbservice.api.OrderStatus;

@Entity
@Table(name="invoice")
class JpaInvoiceRecord extends InvoiceRecord 
{
    protected static final KeyMapper<Long> KEY_MAPPER = new LongKeyMapper();
    
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private long m_id;

    @Basic
    private OrderStatus m_orderStatus;

    @Basic
    private String m_gbAccountGln;

    @Basic
    private String m_invoiceId;

    @Basic
    private Date m_date;

    @Basic
    private Date m_dueDate;

    @Basic
    private String m_description;

    @Basic
    private BigDecimal m_totalValue;

    @Basic
    private String m_totalCurrency;

    @Basic
    private BigDecimal m_balanceDueValue;

    @Basic
    private String m_balanceDueCurrency;

    @Basic
    private String m_billingReference;

    @Basic
    private String m_paymentId;
    
    @OneToMany
    @JoinColumn(name="invoice_id")
    @OrderColumn(name="invoice_extra_index")
    private List<JpaInvoiceExtra> m_extras;
    
    @Basic
    private Date m_billedDate;
    
    @Basic
    private Date m_paymentCommittedDate;
    
    @Basic
    private Date m_paidDate;
    
    public String getId()
    {
        return KEY_MAPPER.keyToId(m_id);
    }

    public void setId(String id)
    {
        m_id = KEY_MAPPER.idToKey(id);
    }

    public OrderStatus getOrderStatus()
    {
        return m_orderStatus;
    }
    public void setOrderStatus(OrderStatus orderStatus)
    {
        m_orderStatus = orderStatus;
    }
    public String getGBAccountGln()
    {
        return m_gbAccountGln;
    }
    public void setGBAccountGln(String gbAccountGln)
    {
        m_gbAccountGln = gbAccountGln;
    }
    public String getInvoiceId()
    {
        return m_invoiceId;
    }
    public void setInvoiceId(String invoiceId)
    {
        m_invoiceId = invoiceId;
    }
    public Date getDate()
    {
        return m_date;
    }
    public void setDate(Date date)
    {
        m_date = date;
    }
    public Date getDueDate()
    {
        return m_dueDate;
    }
    public void setDueDate(Date dueDate)
    {
        m_dueDate = dueDate;
    }

    public String getSummary()
    {
        return m_description;
    }
    public void setSummary(String description)
    {
        m_description = description;
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
   
    public String getBillingReference()
    {
        return m_billingReference;
    }
    public void setBillingReference(String billingReference)
    {
        m_billingReference = billingReference;
    }
    public String getPaymentId()
    {
        return m_paymentId;
    }
    public void setPaymentId(String paymentId)
    {
        m_paymentId = paymentId;
    }
    public Collection<? extends InvoiceExtra> getExtras()
    {
        return m_extras;
    }
    public void setExtras(Collection<? extends InvoiceExtra> extras)
    {
        List<JpaInvoiceExtra> jpa = new ArrayList<>(extras.size());
        for (InvoiceExtra extra : extras)
            jpa.add(new JpaInvoiceExtra(extra));
        m_extras = jpa;
    }

    public Date getBilledDate()
    {
        return m_billedDate;
    }

    public void setBilledDate(Date billedDate)
    {
        m_billedDate = billedDate;
    }

    public Date getPaymentCommittedDate()
    {
        return m_paymentCommittedDate;
    }

    public void setPaymentCommittedDate(Date paymentCommittedDate)
    {
        m_paymentCommittedDate = paymentCommittedDate;
    }

    public Date getPaidDate()
    {
        return m_paidDate;
    }

    public void setPaidDate(Date paidDate)
    {
        m_paidDate = paidDate;
    }

}
