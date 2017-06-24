package org.gs1us.sgg.gbservice.json;

import org.gs1us.sgg.gbservice.api.AttributeEnumValue;

public class InboundAttributeEnumValue implements AttributeEnumValue
{
    private String m_code;
    private String m_displayName;
    public String getCode()
    {
        return m_code;
    }
    public void setCode(String code)
    {
        m_code = code;
    }
    public String getDisplayName()
    {
        return m_displayName;
    }
    public void setDisplayName(String displayName)
    {
        m_displayName = displayName;
    }
    

}
