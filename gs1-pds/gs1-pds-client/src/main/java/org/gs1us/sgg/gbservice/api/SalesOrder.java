package org.gs1us.sgg.gbservice.api;

import java.util.Collection;
import java.util.Date;

public interface SalesOrder extends GBAccountData
{
    String getOrderId();
    String getGBAccountGln();
    Date getDate();
    String getPOId();
    Collection<? extends OrderLineItem> getLineItems();
    String getSummary();
    Amount getTotal();
    String getInvoiceId();
}
