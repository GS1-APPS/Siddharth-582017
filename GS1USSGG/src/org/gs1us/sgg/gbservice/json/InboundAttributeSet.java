package org.gs1us.sgg.gbservice.json;

import java.util.Map;

import org.gs1us.sgg.gbservice.api.AttributeDesc;
import org.gs1us.sgg.gbservice.api.AttributeSet;


public class InboundAttributeSet extends AttributeSet
{
    private Map<String,String> m_map;

    @Override
    public Map<String, String> getAttributes()
    {
        return m_map;
    }

    @Override
    public void setAttributes(Map<String, String> attributes)
    {
        m_map = attributes;
    }
    
}
