package org.gs1us.sgg.app.dwcode;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown=true)
public class UserAccountsReadResponse
{
    private Integer m_userId;
    private String m_name;
    private Integer m_lastAccountUsed;
    private List<UserAccountInfo> m_accounts;
    public Integer getUserId()
    {
        return m_userId;
    }
    public void setUserId(Integer userId)
    {
        m_userId = userId;
    }
    public String getName()
    {
        return m_name;
    }
    public void setName(String name)
    {
        m_name = name;
    }
    public Integer getLastAccountUsed()
    {
        return m_lastAccountUsed;
    }
    public void setLastAccountUsed(Integer lastAccountUsed)
    {
        m_lastAccountUsed = lastAccountUsed;
    }
    public List<UserAccountInfo> getAccounts()
    {
        return m_accounts;
    }
    public void setAccounts(List<UserAccountInfo> accounts)
    {
        m_accounts = accounts;
    }
    
    
}
