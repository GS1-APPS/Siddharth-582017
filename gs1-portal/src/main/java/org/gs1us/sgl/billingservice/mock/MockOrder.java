package org.gs1us.sgl.billingservice.mock;

import java.util.Collection;
import java.util.Date;

import org.gs1us.sgg.gbservice.api.Amount;
import org.gs1us.sgg.gbservice.api.OrderLineItem;
import org.gs1us.sgl.billingservice.Order;
import org.gs1us.sgl.billingservice.BillingStatus;

class MockOrder implements Order
{
    private String m_id;
    private String m_memberGln;
    private BillingStatus m_status;
    private Collection<OrderLineItem> m_lineItems;
    private Amount m_total;
    private Date m_requisitionDate;
    private Date m_invoiceDate;
    private Date m_paymentDate;
    private String m_quotationId;
    private String m_invoiceId;
    private String m_paymentId;
    
    public String getId()
    {
        return m_id;
    }
    public void setId(String id)
    {
        m_id = id;
    }
    public String getMemberGln()
    {
        return m_memberGln;
    }
    public void setMemberGln(String memberGln)
    {
        m_memberGln = memberGln;
    }
    public BillingStatus getStatus()
    {
        return m_status;
    }
    public void setStatus(BillingStatus status)
    {
        m_status = status;
    }
    public Collection<OrderLineItem> getLineItems()
    {
        return m_lineItems;
    }
    public void setLineItems(Collection<OrderLineItem> lineItems)
    {
        m_lineItems = lineItems;
    }
    public Amount getTotal()
    {
        return m_total;
    }
    public void setTotal(Amount total)
    {
        m_total = total;
    }
    public Date getRequisitionDate()
    {
        return m_requisitionDate;
    }
    public void setRequisitionDate(Date requisitionDate)
    {
        m_requisitionDate = requisitionDate;
    }
    public Date getInvoiceDate()
    {
        return m_invoiceDate;
    }
    public void setInvoiceDate(Date invoiceDate)
    {
        m_invoiceDate = invoiceDate;
    }
    public Date getPaymentDate()
    {
        return m_paymentDate;
    }
    public void setPaymentDate(Date paymentDate)
    {
        m_paymentDate = paymentDate;
    }
    public String getQuotationId()
    {
        return m_quotationId;
    }
    public void setQuotationId(String quotationId)
    {
        m_quotationId = quotationId;
    }
    public String getInvoiceId()
    {
        return m_invoiceId;
    }
    public void setInvoiceId(String invoiceId)
    {
        m_invoiceId = invoiceId;
    }
    public String getPaymentId()
    {
        return m_paymentId;
    }
    public void setPaymentId(String paymentId)
    {
        m_paymentId = paymentId;
    }
    


}
