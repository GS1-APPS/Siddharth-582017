package org.gs1us.sgg.gbservice.impl;

import java.util.Date;

import org.gs1us.sgg.gbservice.api.Amount;
import org.gs1us.sgg.gbservice.api.PaymentReceipt;

public class PaymentReceiptImpl implements PaymentReceipt
{
    private String m_paymentId;
    private Date m_date;
    private Amount m_amount;
    public PaymentReceiptImpl(String paymentId, Date date, Amount amount)
    {
        super();
        m_paymentId = paymentId;
        m_date = date;
        m_amount = amount;
    }
    public String getPaymentId()
    {
        return m_paymentId;
    }
    public Date getDate()
    {
        return m_date;
    }
    public Amount getAmount()
    {
        return m_amount;
    }
    
    

}
