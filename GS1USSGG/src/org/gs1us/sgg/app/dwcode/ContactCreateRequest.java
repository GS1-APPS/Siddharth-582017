package org.gs1us.sgg.app.dwcode;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude(value=Include.NON_NULL)
public class ContactCreateRequest extends ContactBase
{
    // (string(10)): Unique identifier for the country,
    private String m_countryCode;
    // (string(10)): Unique identifier for the province
    private String m_provinceCode;
    public String getCountryCode()
    {
        return m_countryCode;
    }
    public void setCountryCode(String countryCode)
    {
        m_countryCode = countryCode;
    }
    public String getProvinceCode()
    {
        return m_provinceCode;
    }
    public void setProvinceCode(String provinceCode)
    {
        m_provinceCode = provinceCode;
    }


}
