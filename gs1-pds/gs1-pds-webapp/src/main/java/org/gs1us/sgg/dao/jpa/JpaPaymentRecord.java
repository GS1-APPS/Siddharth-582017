package org.gs1us.sgg.dao.jpa;

import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Basic;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import org.gs1us.sgg.dao.PaymentRecord;
import org.gs1us.sgg.daoutil.KeyMapper;
import org.gs1us.sgg.daoutil.jpa.LongKeyMapper;
import org.gs1us.sgg.gbservice.api.Amount;
import org.gs1us.sgg.gbservice.api.OrderStatus;

@Entity
@Table(name="payment")
class JpaPaymentRecord extends PaymentRecord
{
    protected static final KeyMapper<Long> KEY_MAPPER = new LongKeyMapper();
    
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private long m_id;

    @Basic
    private String m_gbAccountGln;

    @Basic
    private String m_paymentId;

    @Basic
    private Date m_date;

    @Basic
    private String m_paymentReceiptId;

    @Basic
    private Date m_paymentReceiptDate;

    @Basic
    private BigDecimal m_amountValue;

    @Basic
    private String m_amountCurrency;

    @Basic
    private String m_description;

    @Basic
    private OrderStatus m_status;
    
    @Basic
    private String m_paidReference;
    
    public String getId()
    {
        return KEY_MAPPER.keyToId(m_id);
    }

    public void setId(String id)
    {
        m_id = KEY_MAPPER.idToKey(id);
    }

    public String getGBAccountGln()
    {
        return m_gbAccountGln;
    }
    public void setGBAccountGln(String gbAccountGln)
    {
        m_gbAccountGln = gbAccountGln;
    }
    
    public String getPaymentId()
    {
        return m_paymentId;
    }
    public void setPaymentId(String paymentId)
    {
        m_paymentId = paymentId;
    }
    
    public Date getDate()
    {
        return m_date;
    }
    public void setDate(Date date)
    {
        m_date = date;
    }
    public String getPaymentReceiptId()
    {
        return m_paymentReceiptId;
    }
    public void setPaymentReceiptId(String paymentReceiptId)
    {
        m_paymentReceiptId = paymentReceiptId;
    }
    public Date getPaymentReceiptDate()
    {
        return m_paymentReceiptDate;
    }
    public void setPaymentReceiptDate(Date paymentReceiptDate)
    {
        m_paymentReceiptDate = paymentReceiptDate;
    }
    public Amount getAmount()
    {
        if (m_amountValue == null || m_amountCurrency == null)
            return null;
        else
            return new Amount(m_amountValue, m_amountCurrency);
    }
    public void setAmount(Amount amount)
    {
        if (amount == null)
        {
            m_amountValue = null;
            m_amountCurrency = null;
        }
        else
        {
            m_amountValue = amount.getValue();
            m_amountCurrency = amount.getCurrency();
        }
    }
    public String getDescription()
    {
        return m_description;
    }
    public void setDescription(String description)
    {
        m_description = description;
    }
    
    public OrderStatus getStatus()
    {
        return m_status;
    }
    public void setStatus(OrderStatus status)
    {
        m_status = status;
    }

    public String getPaidReference()
    {
        return m_paidReference;
    }

    public void setPaidReference(String paidReference)
    {
        m_paidReference = paidReference;
    }
    
    

}
