package org.gs1us.sgg.gbservice.json;

import org.gs1us.sgg.gbservice.api.ImportPrevalidationColumn;
import org.gs1us.sgg.gbservice.api.ImportPrevalidationColumnStatus;

public class InboundImportPrevalidationColumn implements
        ImportPrevalidationColumn
{
    private String m_name;
    private ImportPrevalidationColumnStatus m_status;
    public String getName()
    {
        return m_name;
    }
    public void setName(String name)
    {
        m_name = name;
    }
    public ImportPrevalidationColumnStatus getStatus()
    {
        return m_status;
    }
    public void setStatus(ImportPrevalidationColumnStatus status)
    {
        m_status = status;
    }
    
}
