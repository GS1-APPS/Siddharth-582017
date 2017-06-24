package org.gs1us.sgg.dao.jpa;

import java.util.Date;

import javax.persistence.Basic;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import org.gs1us.sgg.dao.InvoiceRecord;
import org.gs1us.sgg.dao.IsoCountryRefRecord;
import org.gs1us.sgg.daoutil.KeyMapper;
import org.gs1us.sgg.daoutil.jpa.LongKeyMapper;

@Entity
@Table(name="iso_country_ref")
public class JpaIsoCountryRefRecord  extends IsoCountryRefRecord
{
    protected static final KeyMapper<Long> KEY_MAPPER = new LongKeyMapper();
    
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private long m_id;

    @Basic
    private String m_country_name;

    @Basic
    private String m_country_code_txt2;

    @Basic
    private String m_country_code_txt3;

    @Basic
    private String m_country_code_num;

    @Basic
    private String m_enabled;
    
    @Basic
    private Date m_modified_date;

    
    public String getId()
    {
        return KEY_MAPPER.keyToId(m_id);
    }

    public void setId(String id)
    {
        m_id = KEY_MAPPER.idToKey(id);
    }

        
    public String getCountryName()
    {
        return m_country_name;
    }

    public void setCountryName(String country_name)
    {
    	m_country_name = country_name;
    }
    
    public String getCountryCodeTxt2()
    {
        return m_country_code_txt2;
    }

    public void setCountryCodeTxt2(String country_code_txt2)
    {
    	m_country_code_txt2 = country_code_txt2;
    }

    public String getCountryCodeTxt3()
    {
        return m_country_code_txt3;
    }

    public void setCountryCodeTxt3(String country_code_txt3)
    {
    	m_country_code_txt3 = country_code_txt3;
    }

    public String getCountryCodeNum()
    {
        return m_country_code_num;
    }

    public void setCountryCodeNum(String country_code_num)
    {
    	m_country_code_num = country_code_num;
    }

    public String getEnabled()
    {
        return m_enabled;
    }

    public void setEnabled(String enabled)
    {
    	m_enabled = enabled;
    }
    
    public Date getModifiedDate()
    {
        return m_modified_date;
    }

    public void setModifiedDate(Date modified_date)
    {
    	m_modified_date = modified_date;
    }
}
