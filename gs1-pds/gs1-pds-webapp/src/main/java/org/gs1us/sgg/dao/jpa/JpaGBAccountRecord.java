package org.gs1us.sgg.dao.jpa;

import javax.persistence.Basic;
import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import org.gs1us.sgg.dao.GBAccountRecord;
import org.gs1us.sgg.daoutil.KeyMapper;
import org.gs1us.sgg.daoutil.jpa.LongKeyMapper;
import org.gs1us.sgg.gbservice.api.AttributeSet;

@Entity
@Table(name="gb_account")
class JpaGBAccountRecord extends GBAccountRecord 
{
    protected static final KeyMapper<Long> KEY_MAPPER = new LongKeyMapper();
    
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private long m_id;

    @Basic
    private String m_gbAccountGln;
    
    @Basic
    private String m_name;
    
    @Basic
    private JpaAttributeSet m_attributes;
    
    @Basic
    @Convert(converter = StringArrayConverter.class)
    private StringArray m_gcps;
    
    public String getId()
    {
        return KEY_MAPPER.keyToId(m_id);
    }

    public void setId(String id)
    {
        m_id = KEY_MAPPER.idToKey(id);
    }

    public String getGln()
    {
        return m_gbAccountGln;
    }
    public void setGln(String gln)
    {
        m_gbAccountGln = gln;
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
        return StringArray.toArrayOrNull(m_gcps);
    }
    public void setGcps(String[] gcps)
    {
        m_gcps = StringArray.fromArrayOrNull(gcps);
    }
    public AttributeSet getAttributes()
    {
        if (m_attributes == null)
            m_attributes = new JpaAttributeSet();
        return m_attributes;
    }

    public void setAttributes(AttributeSet attributes)
    {
        m_attributes = new JpaAttributeSet(attributes);
    }


}
