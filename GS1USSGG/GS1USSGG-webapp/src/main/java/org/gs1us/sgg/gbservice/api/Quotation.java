package org.gs1us.sgg.gbservice.api;

import java.util.Collection;
import java.util.Date;

public interface Quotation
{
    Collection<? extends OrderLineItem> getLineItems();
}
