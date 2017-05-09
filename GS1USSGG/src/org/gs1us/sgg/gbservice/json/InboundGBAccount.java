package org.gs1us.sgg.gbservice.json;

import org.gs1us.sgg.gbservice.api.GBAccount;

public class InboundGBAccount implements GBAccount
{
    private String m_gln;
    private String m_name;
    private String[] m_gcps;
    private InboundAttributeSet m_attributes;
    
    public String getGln()
    {
        return m_gln;
    }
    public void setGln(String gln)
    {
        m_gln = gln;
    }
    public String getName()
    {
        return m_name;
    }
    public void setName(String name)
    {
        m_name = name;
    }
    public String[] getGcps()
    {
        return m_gcps;
    }
    public InboundAttributeSet getAttributes()
    {
        return m_attributes;
    }
    public void setAttributes(InboundAttributeSet attributes)
    {
        m_attributes = attributes;
    }
    public void setGcps(String[] gcps)
    {
        m_gcps = gcps;
    }
    
}


