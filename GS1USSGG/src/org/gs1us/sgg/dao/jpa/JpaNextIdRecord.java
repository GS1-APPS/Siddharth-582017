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

@Entity
@Table(name="next_id")
class JpaNextIdRecord
{
    protected static final KeyMapper<Long> KEY_MAPPER = new LongKeyMapper();
    
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private long m_id;

    @Basic
    private String m_name;
    
    @Basic
    private long m_nextId;
    
    public String getId()
    {
        return KEY_MAPPER.keyToId(m_id);
    }

    public void setId(String id)
    {
        m_id = KEY_MAPPER.idToKey(id);
    }

    public String getName()
    {
        return m_name;
    }
    public void setName(String name)
    {
        m_name = name;
    }

    public long getNextId()
    {
        return m_nextId;
    }

    public void setNextId(long nextId)
    {
        m_nextId = nextId;
    }

}
