package org.gs1us.sgg.gbservice.api;

import java.util.Date;

public interface BillingTransaction
{
    BillingTransactionType getType();
    Date getDate();
    String getReference();
    String getDescription();
    Amount getAmount();
    Amount getBalance();
}
