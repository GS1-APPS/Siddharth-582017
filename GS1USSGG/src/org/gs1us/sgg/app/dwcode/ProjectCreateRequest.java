package org.gs1us.sgg.app.dwcode;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude(value=Include.NON_NULL)
public class ProjectCreateRequest
{
    private String m_name;
    private String m_description;
    
    public String getName()
    {
        return m_name;
    }
    public void setName(String name)
    {
        m_name = name;
    }
    public String getDescription()
    {
        return m_description;
    }
    public void setDescription(String description)
    {
        m_description = description;
    }
    
    
}
