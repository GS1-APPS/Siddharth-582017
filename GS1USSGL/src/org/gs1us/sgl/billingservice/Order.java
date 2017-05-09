package org.gs1us.sgl.billingservice;

import java.util.Collection;
import java.util.Date;

import org.gs1us.sgg.gbservice.api.Amount;
import org.gs1us.sgg.gbservice.api.OrderLineItem;

public interface Order
{
    String getId();
    String getMemberGln();
    BillingStatus getStatus();
    Collection<OrderLineItem> getLineItems();
    Amount getTotal();
    Date getRequisitionDate();
    Date getInvoiceDate();
    Date getPaymentDate();
    String getQuotationId();
    String getInvoiceId();
    String getPaymentId();
}
