package org.gs1us.sgg.app;

import org.gs1us.sgg.gbservice.api.AttributeEnumValue;

public class AttributeEnumValueImpl implements AttributeEnumValue
{
    private String m_code;
    private String m_displayName;
    public AttributeEnumValueImpl(String code, String displayName)
    {
        super();
        m_code = code;
        m_displayName = displayName;
    }
    public String getCode()
    {
        return m_code;
    }
    public String getDisplayName()
    {
        return m_displayName;
    }
    

}
