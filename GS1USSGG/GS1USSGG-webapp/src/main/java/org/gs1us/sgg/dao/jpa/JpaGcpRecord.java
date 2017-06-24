package org.gs1us.sgg.dao.jpa;

import javax.persistence.Basic;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import org.gs1us.sgg.dao.GcpRecord;
import org.gs1us.sgg.daoutil.KeyMapper;
import org.gs1us.sgg.daoutil.jpa.LongKeyMapper;

@Entity
@Table(name="gcp_record")
public class JpaGcpRecord implements GcpRecord
{
    protected static final KeyMapper<Long> KEY_MAPPER = new LongKeyMapper();
    
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private long m_id;

    @Basic
    private String m_gcp;

    @Basic
    private String m_gln;

    public String getId()
    {
        return KEY_MAPPER.keyToId(m_id);
    }

    public void setId(String id)
    {
        m_id = KEY_MAPPER.idToKey(id);
    }

    public String getGcp()
    {
        return m_gcp;
    }

    public void setGcp(String gcp)
    {
        m_gcp = gcp;
    }

    public String getGln()
    {
        return m_gln;
    }

    public void setGln(String gln)
    {
        m_gln = gln;
    }
    
 

}
