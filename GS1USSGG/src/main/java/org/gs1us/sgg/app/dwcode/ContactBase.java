package org.gs1us.sgg.app.dwcode;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude(value=Include.NON_NULL)
public class ContactBase
{

    private String m_firstName;
    private String m_lastName;
    private String m_email;
    private String m_phone1;
    private String m_phone2;
    private String m_fax;
    private String m_street1;
    private String m_street2;
    private String m_city;
    private String m_postalCode;

    public ContactBase()
    {
        super();
    }

    public String getFirstName()
    {
        return m_firstName;
    }

    public void setFirstName(String firstName)
    {
        m_firstName = firstName;
    }

    public String getLastName()
    {
        return m_lastName;
    }

    public void setLastName(String lastName)
    {
        m_lastName = lastName;
    }

    public String getEmail()
    {
        return m_email;
    }

    public void setEmail(String email)
    {
        m_email = email;
    }

    public String getPhone1()
    {
        return m_phone1;
    }

    public void setPhone1(String phone1)
    {
        m_phone1 = phone1;
    }

    public String getPhone2()
    {
        return m_phone2;
    }

    public void setPhone2(String phone2)
    {
        m_phone2 = phone2;
    }

    public String getFax()
    {
        return m_fax;
    }

    public void setFax(String fax)
    {
        m_fax = fax;
    }

    public String getStreet1()
    {
        return m_street1;
    }

    public void setStreet1(String street1)
    {
        m_street1 = street1;
    }

    public String getStreet2()
    {
        return m_street2;
    }

    public void setStreet2(String street2)
    {
        m_street2 = street2;
    }

    public String getCity()
    {
        return m_city;
    }

    public void setCity(String city)
    {
        m_city = city;
    }

    public String getPostalCode()
    {
        return m_postalCode;
    }

    public void setPostalCode(String postalCode)
    {
        m_postalCode = postalCode;
    }

}
