package org.gs1us.sgg.gbservice.impl;

import java.util.Date;

import org.gs1us.sgg.gbservice.api.Amount;
import org.gs1us.sgg.gbservice.api.BillingTransaction;
import org.gs1us.sgg.gbservice.api.BillingTransactionType;

class BillingTransactionImpl implements BillingTransaction
{
    private BillingTransactionType m_type;
    private Date m_date;
    private String m_reference;
    private String m_description;
    private Amount m_amount;
    private Amount m_balance;
    public BillingTransactionImpl(BillingTransactionType type, Date date,
            String reference, String description, Amount amount)
    {
        super();
        m_type = type;
        m_date = date;
        m_reference = reference;
        m_description = description;
        m_amount = amount;
    }
    public BillingTransactionType getType()
    {
        return m_type;
    }
    
    public Date getDate()
    {
        return m_date;
    }
    public String getReference()
    {
        return m_reference;
    }
    public String getDescription()
    {
        return m_description;
    }
    public Amount getAmount()
    {
        return m_amount;
    }
    public Amount getBalance()
    {
        return m_balance;
    }
    void setBalance(Amount balance)
    {
        m_balance = balance;
    }
    
}
