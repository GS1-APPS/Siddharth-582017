package org.gs1us.sgg.gbservice.json;

import java.util.Date;
import org.gs1us.sgg.gbservice.api.IsoCountryRef;

public class InboundIsoCountryRef implements IsoCountryRef
{	
    private String m_id;
    private String m_country_name;
    private String m_country_code_txt2;
    private String m_country_code_txt3;
    private String m_country_code_num;
    private String m_enabled;    
    private Date m_modified_date;
        
    public String getId()
    {
        return m_id;
    }
    public void setId(String Id)
    {
        m_id = Id;
    }
    
    public String getCountryName()
    {
        return m_country_name;
    }
    public void setCountryName(String countryName)
    {
    	m_country_name = countryName;
    }
    
    public String getCountryCodeTxt2()
    {
        return m_country_code_txt2;
    }
    public void setCountryCodeTxt2(String countryCodeTxt2)
    {
    	m_country_code_txt2 = countryCodeTxt2;
    }
    
    public String getCountryCodeTxt3()
    {
        return m_country_code_txt3;
    }
    public void setCountryCodeTxt3(String countryCodeTxt3)
    {
    	m_country_code_txt3 = countryCodeTxt3;
    }
    
    public String getCountryCodeNum()
    {
        return m_country_code_num;
    }
    public void setCountryCodeNum(String countryCodeNum)
    {
    	m_country_code_num = countryCodeNum;
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
    public void setModifiedDate(Date date)
    {
    	m_modified_date = date;
    }
}
