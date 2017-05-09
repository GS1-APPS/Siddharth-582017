package org.gs1us.sgg.dao;

import java.util.Collection;
import java.util.Date;

import org.gs1us.sgg.gbservice.api.Amount;
import org.gs1us.sgg.gbservice.api.OrderLineItem;
import org.gs1us.sgg.gbservice.api.SalesOrder;

public abstract class SalesOrderRecord implements SalesOrder
{
    public abstract String getId();
    public abstract void setId(String id);
    
    public abstract Date getDate();
    public abstract void setDate(Date date);
    
    public abstract String getOrderId();
    public abstract void setOrderId(String orderId);
    
    public abstract String getGBAccountGln();
    public abstract void setGBAccountGln(String gln);
    
    public abstract String getPOId();
    public abstract void setPOId(String poId);
    
    public abstract Collection<? extends OrderLineItem> getLineItems();
    public abstract void setLineItems(Collection<? extends OrderLineItem> lineItems);

    public abstract String getSummary();
    public abstract void setSummary(String summary);

    public abstract Amount getTotal();
    public abstract void setTotal(Amount amount);

    public abstract Amount getBalanceDue();
    public abstract void setBalanceDue(Amount balanceDue);

    public abstract String getInvoiceId();
    public abstract void setInvoiceId(String invoiceId);
}
