package org.gs1us.sgg.app.dwcode;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonIgnoreProperties(ignoreUnknown=true)
@JsonInclude(value=Include.NON_NULL)
public class CountryReadResponse
{
    private String m_code;
    private String m_name;
    private Integer m_isoCode;
    public String getCode()
    {
        return m_code;
    }
    public void setCode(String code)
    {
        m_code = code;
    }
    public String getName()
    {
        return m_name;
    }
    public void setName(String name)
    {
        m_name = name;
    }
    public Integer getISOCode()
    {
        return m_isoCode;
    }
    public void setISOCode(Integer isoCode)
    {
        m_isoCode = isoCode;
    }
    
    
}
