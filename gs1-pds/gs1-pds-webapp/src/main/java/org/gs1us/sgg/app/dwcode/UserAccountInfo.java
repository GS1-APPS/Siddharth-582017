package org.gs1us.sgg.app.dwcode;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown=true)
public class UserAccountInfo
{
    private Integer m_accountId;
    private String m_companyName;
    private String m_role;
    private Date m_createDate;
    private boolean m_isDeveloper;
    public Integer getAccountId()
    {
        return m_accountId;
    }
    public void setAccountId(Integer accountId)
    {
        m_accountId = accountId;
    }
    public String getCompanyName()
    {
        return m_companyName;
    }
    public void setCompanyName(String companyName)
    {
        m_companyName = companyName;
    }
    public String getRole()
    {
        return m_role;
    }
    public void setRole(String role)
    {
        m_role = role;
    }
    public Date getCreateDate()
    {
        return m_createDate;
    }
    public void setCreateDate(Date createDate)
    {
        m_createDate = createDate;
    }
    public boolean isDeveloper()
    {
        return m_isDeveloper;
    }
    public void setDeveloper(boolean isDeveloper)
    {
        m_isDeveloper = isDeveloper;
    }
    
    
}
