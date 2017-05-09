package org.gs1us.sgg.gbservice.api;

import java.util.Collection;
import java.util.Date;

public interface Invoice extends GBAccountData
{
    String getInvoiceId();
    String getGBAccountGln();
    Date getDate();
    //Date getDueDate();
    //Collection<OrderLineItem> getLineItems();
    String getSummary();
    Amount getTotal();
    //Amount getBalanceDue();
    OrderStatus getOrderStatus();
    String getBillingReference();
    Collection<? extends InvoiceExtra> getExtras();
    Date getBilledDate();
    Date getPaymentCommittedDate();
    Date getPaidDate();
}
