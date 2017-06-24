package org.gs1us.sgg.gbservice.json;

import java.util.Date;

import org.gs1us.sgg.gbservice.api.Amount;
import org.gs1us.sgg.gbservice.api.OrderStatus;
import org.gs1us.sgg.gbservice.api.Payment;

import com.fasterxml.jackson.annotation.JsonProperty;

public class InboundPayment implements Payment
{
    private String m_gbAccountGln;
    private String m_paymentId;
    private Date m_date;
    private String m_paymentReceiptId;
    private Amount m_amount;
    private OrderStatus m_status;
    private String m_paidReference;
    
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
    public Amount getAmount()
    {
        return m_amount;
    }
    public void setAmount(Amount amount)
    {
        m_amount = amount;
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
