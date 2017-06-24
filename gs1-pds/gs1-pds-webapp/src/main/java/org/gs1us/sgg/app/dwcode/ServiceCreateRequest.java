package org.gs1us.sgg.app.dwcode;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude(value=Include.NON_NULL)
public class ServiceCreateRequest extends ServiceCreateUpdateRequest
{
    private Integer m_projectId;
    private String m_serviceType;
    
    public Integer getProjectId()
    {
        return m_projectId;
    }
    public void setProjectId(Integer projectId)
    {
        m_projectId = projectId;
    }
    public String getServiceType()
    {
        return m_serviceType;
    }
    public void setServiceType(String serviceType)
    {
        m_serviceType = serviceType;
    }
    
    
}
