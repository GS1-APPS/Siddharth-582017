package org.gs1us.sgg.gbservice.json;

import java.util.Date;

import org.gs1us.sgg.gbservice.api.Amount;
import org.gs1us.sgg.gbservice.api.BillingTransaction;
import org.gs1us.sgg.gbservice.api.BillingTransactionType;

public class InboundBillingTransaction implements BillingTransaction
{
    private BillingTransactionType m_type;
    private Date m_date;
    private String m_reference;
    private String m_description;
    private Amount m_amount;
    private Amount m_balance;
    public BillingTransactionType getType()
    {
        return m_type;
    }
    public void setType(BillingTransactionType type)
    {
        m_type = type;
    }
    public Date getDate()
    {
        return m_date;
    }
    public void setDate(Date date)
    {
        m_date = date;
    }
    public String getReference()
    {
        return m_reference;
    }
    public void setReference(String reference)
    {
        m_reference = reference;
    }
    public String getDescription()
    {
        return m_description;
    }
    public void setDescription(String description)
    {
        m_description = description;
    }
    public Amount getAmount()
    {
        return m_amount;
    }
    public void setAmount(Amount amount)
    {
        m_amount = amount;
    }
    public Amount getBalance()
    {
        return m_balance;
    }
    public void setBalance(Amount balance)
    {
        m_balance = balance;
    }
    

}
