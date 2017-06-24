package org.gs1us.sgg.gbservice.json;

import java.util.Date;

import org.gs1us.sgg.gbservice.api.Amount;
import org.gs1us.sgg.gbservice.api.PaymentReceipt;

public class InboundPaymentReceipt implements PaymentReceipt
{
    private String m_paymentId;
    private Date m_date;
    private Amount m_amount;
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
    public Amount getAmount()
    {
        return m_amount;
    }
    public void setAmount(Amount amount)
    {
        m_amount = amount;
    }
    
}
