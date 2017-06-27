package org.gs1us.sgg.gbservice.api;

import java.util.Collection;
import java.util.List;
import java.util.Map;

public interface ImportPrevalidationSegmentSettings
{
    public boolean isEnabled();
    public List<String> getColumnMappings();
    public Map<String,String> getConstantAttributeValueMap();
}
