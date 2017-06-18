package org.gs1us.sgg.gbservice.json;

import java.util.Collection;
import java.util.Date;

import org.gs1us.sgg.gbservice.api.Amount;
import org.gs1us.sgg.gbservice.api.Invoice;
import org.gs1us.sgg.gbservice.api.OrderStatus;

import com.fasterxml.jackson.annotation.JsonProperty;

public class InboundInvoice implements Invoice
{
    private String m_invoiceId;
    private String m_gbAccountGln;
    private Date m_date;
    private String m_summary;
    private Amount m_total;
    private OrderStatus m_orderStatus;
    private String m_billingReference;
    private Collection<InboundInvoiceExtra> m_extras;
    private Date m_billedDate;
    private Date m_paymentCommittedDate;
    private Date m_paidDate;
    
    public String getInvoiceId()
    {
        return m_invoiceId;
    }
    public void setInvoiceId(String invoiceId)
    {
        m_invoiceId = invoiceId;
    }
    @JsonProperty("gln")
    public String getGBAccountGln()
    {
        return m_gbAccountGln;
    }
    @JsonProperty("gln")
    public void setGBAccountGln(String gbAccountGln)
    {
        m_gbAccountGln = gbAccountGln;
    }
    public Date getDate()
    {
        return m_date;
    }
    public void setDate(Date date)
    {
        m_date = date;
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
        return m_total;
    }
    public void setTotal(Amount total)
    {
        m_total = total;
    }
    public OrderStatus getOrderStatus()
    {
        return m_orderStatus;
    }
    public void setOrderStatus(OrderStatus orderStatus)
    {
        m_orderStatus = orderStatus;
    }
    public String getBillingReference()
    {
        return m_billingReference;
    }
    public void setBillingReference(String billingReference)
    {
        m_billingReference = billingReference;
    }
    public Collection<InboundInvoiceExtra> getExtras()
    {
        return m_extras;
    }
    public void setExtras(Collection<InboundInvoiceExtra> extras)
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
    
    

}
