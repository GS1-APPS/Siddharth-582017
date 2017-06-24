package org.gs1us.sgg.gbservice.api;

import java.util.Date;

public interface PaymentReceipt
{
    public String getPaymentId();
    public Date getDate();
    public Amount getAmount();
}
