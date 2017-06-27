package org.gs1us.sgg.app.dwcode;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonIgnoreProperties(ignoreUnknown=true)
@JsonInclude(value=Include.NON_NULL)
public class AccountCreateResponse 
{
    private int m_id;
    private int m_userId;

    public int getId()
    {
        return m_id;
    }

    public void setId(int id)
    {
        m_id = id;
    }

    public int getUserId()
    {
        return m_userId;
    }

    public void setUserId(int userId)
    {
        m_userId = userId;
    }
    
    
    
}
