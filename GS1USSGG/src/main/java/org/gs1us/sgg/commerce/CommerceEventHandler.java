package org.gs1us.sgg.commerce;

import java.util.Date;

import org.gs1us.sgg.dao.InvoiceRecord;
import org.gs1us.sgg.dao.PaymentRecord;
import org.gs1us.sgg.dao.SalesOrderRecord;
import org.gs1us.sgg.gbservice.api.OrderLineItem;
import org.gs1us.sgg.gbservice.api.OrderStatus;

public interface CommerceEventHandler
{
    public void handle(OrderStatus status, Date date, OrderLineItem lineItem, SalesOrderRecord salesOrder, InvoiceRecord invoice, PaymentRecord payment);
}
