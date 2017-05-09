package org.gs1us.sgg.gbservice.json;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.gs1us.sgg.gbservice.api.ImportPrevalidationSegmentSettings;

public class InboundImportPrevalidationSegmentSettings implements
        ImportPrevalidationSegmentSettings
{
    private boolean m_enabled;
    private List<String> m_columnMappings;
    private Map<String,String> m_constantAttributeValueMap;
    public boolean isEnabled()
    {
        return m_enabled;
    }
    public void setEnabled(boolean enabled)
    {
        m_enabled = enabled;
    }
    public List<String> getColumnMappings()
    {
        return m_columnMappings;
    }
    public void setColumnMappings(List<String> columnMappings)
    {
        m_columnMappings = columnMappings;
    }
    public Map<String, String> getConstantAttributeValueMap()
    {
        return m_constantAttributeValueMap;
    }
    public void setConstantAttributeValueMap(
            Map<String, String> constantAttributeValueMap)
    {
        m_constantAttributeValueMap = constantAttributeValueMap;
    }
    

}
