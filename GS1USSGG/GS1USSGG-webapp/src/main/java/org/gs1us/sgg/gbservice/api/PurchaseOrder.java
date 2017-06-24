package org.gs1us.sgg.gbservice.api;

import java.util.Collection;
import java.util.Date;

public interface PurchaseOrder
{
    String getPOId();
    Date getDate();
    Collection<? extends OrderLineItem> getLineItems();
}
