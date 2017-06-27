package org.gs1us.sgg.dao.mock;

import java.util.Collection;
import java.util.Date;

import org.gs1us.sgg.dao.InvoiceRecord;
import org.gs1us.sgg.gbservice.api.Amount;
import org.gs1us.sgg.gbservice.api.InvoiceExtra;
import org.gs1us.sgg.gbservice.api.OrderStatus;

class MockInvoiceRecord extends InvoiceRecord implements DaoElement<MockInvoiceRecord>, HasGln
{
    private String m_id;
    private OrderStatus m_orderStatus;
    private String m_gbAccountGln;
    private String m_invoiceId;
    private Date m_date;
    private Date m_dueDate;
    private String m_description;
    private Amount m_total;
    private Amount m_balanceDue;
    private String m_billingReference;
    private String m_paymentId;
    private Collection<? extends InvoiceExtra> m_extras;
    private Date m_billedDate;
    private Date m_paymentCommittedDate;
    private Date m_paidDate;
    
    public String getId()
    {
        return m_id;
    }
    public void setId(String id)
    {
        m_id = id;
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
        return m_total;
    }
    public void setTotal(Amount total)
    {
        m_total = total;
    }
    public Amount getBalanceDue()
    {
        return m_balanceDue;
    }
    public void setBalanceDue(Amount balanceDue)
    {
        m_balanceDue = balanceDue;
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
    @Override
    public MockInvoiceRecord newInstance()
    {
        return new MockInvoiceRecord();
    }
    
    public Collection<? extends InvoiceExtra> getExtras()
    {
        return m_extras;
    }
    public void setExtras(Collection<? extends InvoiceExtra> extras)
    {
        m_extras = extras;
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
    @Override
    public MockInvoiceRecord updateFrom(MockInvoiceRecord from)
    {
        setId(from.getId());
        setOrderStatus(from.getOrderStatus());
        setGBAccountGln(from.getGBAccountGln());
        setInvoiceId(from.getInvoiceId());
        setDate(from.getDate());
        setDueDate(from.getDueDate());
        setSummary(from.getSummary());
        setTotal(from.getTotal());
        setBalanceDue(from.getBalanceDue());
        setBillingReference(from.getBillingReference());
        setPaymentId(from.getPaymentId());
        setExtras(from.getExtras()); // TODO not really right?
        setBilledDate(from.getBilledDate());
        setPaymentCommittedDate(from.getPaymentCommittedDate());
        setPaidDate(from.getPaidDate());
        return this;
    }
    


}
