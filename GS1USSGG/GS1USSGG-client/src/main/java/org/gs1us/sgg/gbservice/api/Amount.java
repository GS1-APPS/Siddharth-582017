package org.gs1us.sgg.gbservice.api;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class Amount
{
    public static Amount ZERO = new Amount(BigDecimal.ZERO.setScale(2), null);
    
    private BigDecimal m_value;
    private String m_currency;
    
    public Amount(BigDecimal value, String currency)
    {
        super();
        if (value.compareTo(BigDecimal.ZERO) != 0 && currency == null)
            throw new IllegalArgumentException();
        
        m_value = value.setScale(2, RoundingMode.HALF_EVEN);
        m_currency = currency;
    }
    
    public Amount(int value, String currency)
    {
        this(new BigDecimal(value), currency);
    }
    
    public Amount(double value, String currency)
    {
        this(new BigDecimal(value), currency);
    }
    
    public BigDecimal getValue()
    {
        return m_value;
    }
    
    public String getCurrency()
    {
        return m_currency;
    }
    
    public boolean isZero()
    {
        return m_value.compareTo(BigDecimal.ZERO) == 0;
    }
    
    public Amount negate()
    {
        return new Amount(m_value.negate(), m_currency);
    }
    
    public Amount add(Amount a)
    {
        return sameCurrency(m_value.add(a.getValue()), a);
    }

    public Amount subtract(Amount a)
    {
        return sameCurrency(m_value.subtract(a.getValue()), a);
    }

    public Amount multiplyBy(BigDecimal multiplier)
    {
        return new Amount(m_value.multiply(multiplier), m_currency);
    }

    public Amount divideBy(BigDecimal divisor)
    {
        return new Amount(m_value.divide(divisor, RoundingMode.HALF_EVEN), m_currency);
    }

    public Amount min(Amount a)
    {
        return sameCurrency(m_value.min(a.getValue()), a);
    }

    public Amount max(Amount a)
    {
        return sameCurrency(m_value.max(a.getValue()), a);
    }

    private Amount sameCurrency(BigDecimal newValue, Amount other)
    {
        if (m_currency != null && other.getCurrency() != null && !(m_currency.equals(other.getCurrency())))
            throw new IllegalArgumentException();
        
        return new Amount(newValue, m_currency == null ? other.getCurrency() : m_currency);
    }

    @Override
    public int hashCode()
    {
        final int prime = 31;
        int result = 1;
        result = prime * result
                 + ((m_currency == null) ? 0 : m_currency.hashCode());
        result = prime * result + ((m_value == null) ? 0 : m_value.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj)
    {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (!(obj instanceof Amount))
            return false;
        Amount other = (Amount) obj;
        if (m_currency == null)
        {
            if (other.m_currency != null)
                return false;
        }
        else if (!m_currency.equals(other.m_currency))
            return false;
        if (m_value == null)
        {
            if (other.m_value != null)
                return false;
        }
        else if (!m_value.equals(other.m_value))
            return false;
        return true;
    }

    @Override
    public String toString()
    {
        return String.format("%.2f %s", m_value, m_currency);
    }
    
    
}
