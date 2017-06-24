package org.gs1us.sgg.dao.mock;

import java.util.HashMap;
import java.util.Map;

import org.gs1us.sgg.gbservice.api.AttributeDesc;
import org.gs1us.sgg.gbservice.api.AttributeSet;


public class MockAttributeSet extends AttributeSet
{
    private Map<String, String> m_map;
    
    public MockAttributeSet()
    {
        m_map = new HashMap<String, String>();
    }
    
    MockAttributeSet(MockAttributeSet attributes)
    {
        m_map = new HashMap<>(attributes.m_map);
    }

    @Override
    public Map<String, String> getAttributes()
    {
        return m_map;
    }

    @Override
    public void setAttributes(Map<String, String> attributes)
    {
        m_map = new HashMap<>(attributes);
    }

}
