package org.gs1us.sgg.dao.mock;

import org.gs1us.sgg.dao.GBAccountRecord;
import org.gs1us.sgg.gbservice.api.AttributeSet;

class MockGBAccountRecord extends GBAccountRecord implements DaoElement<MockGBAccountRecord>
{
    private String m_id;
    private String m_gln;
    private String m_name;
    private String[] m_gcps;
    private MockAttributeSet m_attributes = new MockAttributeSet();
    
    public String getId()
    {
        return m_id;
    }
    public void setId(String id)
    {
        m_id = id;
    }
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
    public void setGcps(String[] gcps)
    {
        m_gcps = gcps;
    }
    

    public MockAttributeSet getAttributes()
    {
        return m_attributes;
    }
    public void setAttributes(AttributeSet attributes)
    {
        m_attributes = (MockAttributeSet)attributes;
    }
    @Override
    public MockGBAccountRecord newInstance()
    {
        return new MockGBAccountRecord();
    }
    @Override
    public MockGBAccountRecord updateFrom(MockGBAccountRecord from)
    {
        setId(from.getId());
        setGln(from.getGln());
        setName(from.getName());
        setGcps(from.getGcps());  // not really right
        setAttributes(from.getAttributes());
        return this;
    }
    


}
