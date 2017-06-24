package org.gs1us.sgg.app.dwcode;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonIgnoreProperties(ignoreUnknown=true)
@JsonInclude(value=Include.NON_NULL)
public class ContactReadResponse extends ContactBase
{
    private CountryReadResponse m_countryProperties;
    private ProvinceReadResponse m_provinceProperties;
    public CountryReadResponse getCountryProperties()
    {
        return m_countryProperties;
    }
    public void setCountryProperties(CountryReadResponse countryProperties)
    {
        m_countryProperties = countryProperties;
    }
    public ProvinceReadResponse getProvinceProperties()
    {
        return m_provinceProperties;
    }
    public void setProvinceProperties(ProvinceReadResponse provinceProperties)
    {
        m_provinceProperties = provinceProperties;
    }
    
    
}
