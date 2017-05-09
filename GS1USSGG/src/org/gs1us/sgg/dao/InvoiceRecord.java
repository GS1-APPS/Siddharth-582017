package org.gs1us.sgg.dao;

import java.util.Collection;
import java.util.Date;

import org.gs1us.sgg.gbservice.api.Amount;
import org.gs1us.sgg.gbservice.api.Invoice;
import org.gs1us.sgg.gbservice.api.InvoiceExtra;
import org.gs1us.sgg.gbservice.api.OrderLineItem;
import org.gs1us.sgg.gbservice.api.OrderStatus;

public abstract class InvoiceRecord implements Invoice
{
    public abstract String getId();
   
    public abstract OrderStatus getOrderStatus();
    public abstract void setOrderStatus(OrderStatus orderStatus);
    
    public abstract String getGBAccountGln();
    public abstract void setGBAccountGln(String gln);
    
    public abstract String getInvoiceId();
    public abstract void setInvoiceId(String invoiceId);

    public abstract Date getDate();
    public abstract void setDate(Date date);

    public abstract Date getDueDate();
    public abstract void setDueDate(Date dueDate);

    public abstract String getSummary();
    public abstract void setSummary(String summary);

    public abstract Amount getTotal();
    public abstract void setTotal(Amount amount);

    public abstract Amount getBalanceDue();
    public abstract void setBalanceDue(Amount balanceDue);

    public abstract String getBillingReference();
    public abstract void setBillingReference(String billingReference);

    public abstract String getPaymentId();
    public abstract void setPaymentId(String paymentId);
    
    public abstract Collection<? extends InvoiceExtra> getExtras();
    public abstract void setExtras(Collection<? extends InvoiceExtra> extras);

    public abstract Date getBilledDate();
    public abstract void setBilledDate(Date billedDate);

    public abstract Date getPaymentCommittedDate();
    public abstract void setPaymentCommittedDate(Date paymentCommittedDate);

    public abstract Date getPaidDate();
    public abstract void setPaidDate(Date paidDate);

}
