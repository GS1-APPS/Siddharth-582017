package org.gs1us.sgg.gbservice.impl;

public class Gtin
{
    private String m_asEntered;
    private String m_canonical;
    private String m_as14;
    
    public Gtin(String asEntered)
    {
        if (asEntered == null)
            return;
        
        m_asEntered = asEntered;
        int asEnteredLength = m_asEntered.length();
        if (asEnteredLength > 14)
        {
            // They're invalid anyway
            m_canonical = asEntered;
            m_as14 = asEntered;
        }
        else
        {
            m_as14 = "00000000000000".substring(0, 14 - asEnteredLength) + asEntered;
            if (m_as14.startsWith("000000"))
                m_canonical = m_as14.substring(6); // GTIN-8
            else if (m_as14.startsWith("00"))
                m_canonical = m_as14.substring(2); // GTIN-12
            else if (m_as14.startsWith("0"))
                m_canonical = m_as14.substring(1); // GTIN-13
            else
                m_canonical = m_as14;
        }
    }

    public String getAsEntered()
    {
        return m_asEntered;
    }

    public String getCanonical()
    {
        return m_canonical;
    }

    public String getAs14()
    {
        return m_as14;
    }

    @Override
    public int hashCode()
    {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((m_as14 == null) ? 0 : m_as14.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj)
    {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (!(obj instanceof Gtin))
            return false;
        Gtin other = (Gtin) obj;
        if (m_as14 == null)
        {
            if (other.m_as14 != null)
                return false;
        }
        else if (!m_as14.equals(other.m_as14))
            return false;
        return true;
    }

    
        
    
}
