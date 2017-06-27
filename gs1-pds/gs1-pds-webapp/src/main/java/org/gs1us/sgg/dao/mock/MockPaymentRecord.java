package org.gs1us.sgg.dao.mock;

import java.util.Date;

import org.gs1us.sgg.dao.PaymentRecord;
import org.gs1us.sgg.gbservice.api.Amount;
import org.gs1us.sgg.gbservice.api.OrderStatus;

class MockPaymentRecord extends PaymentRecord implements DaoElement<MockPaymentRecord>, HasGln
{
    private String m_id;
    private String m_gbAccountGln;
    private String m_paymentId;
    private Date m_date;
    private String m_paymentReceiptId;
    private Date m_paymentReceiptDate;
    private Amount m_amount;
    private String m_description;
    private OrderStatus m_status;
    private String m_paidReference;
    
    public String getId()
    {
        return m_id;
    }
    public void setId(String id)
    {
        m_id = id;
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
        return m_amount;
    }
    public void setAmount(Amount amount)
    {
        m_amount = amount;
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
    @Override
    public MockPaymentRecord newInstance()
    {
        return new MockPaymentRecord();
    }
    @Override
    public MockPaymentRecord updateFrom(MockPaymentRecord from)
    {
        setId(from.getId());
        setGBAccountGln(from.getGBAccountGln());
        setPaymentId(from.getPaymentId());
        setDate(from.getDate());
        setPaymentReceiptId(from.getPaymentReceiptId());
        setPaymentReceiptDate(from.getPaymentReceiptDate());
        setAmount(from.getAmount());
        setDescription(from.getDescription());
        setStatus(from.getStatus());
        setPaidReference(from.getPaidReference());
        return this;
    }
    

}
