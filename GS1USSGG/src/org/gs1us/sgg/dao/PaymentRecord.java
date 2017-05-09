package org.gs1us.sgg.dao;

import java.util.Date;

import org.gs1us.sgg.gbservice.api.Amount;
import org.gs1us.sgg.gbservice.api.OrderStatus;
import org.gs1us.sgg.gbservice.api.Payment;

public abstract class PaymentRecord implements Payment
{
    public abstract String getId();
    
    public abstract String getGBAccountGln();
    public abstract void setGBAccountGln(String gln);
    
    public abstract String getPaymentId();
    public abstract void setPaymentId(String paymentId);
    
    public abstract Date getDate();
    public abstract void setDate(Date date);
    
    public abstract String getPaymentReceiptId();
    public abstract void setPaymentReceiptId(String paymentReceiptId);
    
    public abstract Date getPaymentReceiptDate();
    public abstract void setPaymentReceiptDate(Date paymentReceiptDate);
    
    public abstract Amount getAmount();
    public abstract void setAmount(Amount amount);
    
    public abstract String getDescription();
    public abstract void setDescription(String description);
    
    public abstract OrderStatus getStatus();
    public abstract void setStatus(OrderStatus status);
    
    public abstract String getPaidReference();
    public abstract void setPaidReference(String paidReference);
}
