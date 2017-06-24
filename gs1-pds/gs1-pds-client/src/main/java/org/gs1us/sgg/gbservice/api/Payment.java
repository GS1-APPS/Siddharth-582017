package org.gs1us.sgg.gbservice.api;

import java.util.Date;

public interface Payment extends GBAccountData
{
    String getGBAccountGln();
    String getPaymentId();
    Date getDate();
    String getPaymentReceiptId();
    Amount getAmount();
    OrderStatus getStatus(); // PAYMENT_COMMITTED or PAID
    String getPaidReference();
}
