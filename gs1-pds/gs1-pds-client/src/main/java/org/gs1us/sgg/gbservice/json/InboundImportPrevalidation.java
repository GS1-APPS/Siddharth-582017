package org.gs1us.sgg.gbservice.json;

import java.util.Collection;
import java.util.List;

import org.gs1us.sgg.gbservice.api.ImportPrevalidation;
import org.gs1us.sgg.gbservice.api.ImportPrevalidationSegment;

public class InboundImportPrevalidation implements ImportPrevalidation
{
    private String m_fileError;
    private List<? extends InboundImportPrevalidationSegment> m_segments;
    public String getFileError()
    {
        return m_fileError;
    }
    public void setFileError(String fileError)
    {
        m_fileError = fileError;
    }
    public List<? extends InboundImportPrevalidationSegment> getSegments()
    {
        return m_segments;
    }
    public void setSegments(List<? extends InboundImportPrevalidationSegment> segments)
    {
        m_segments = segments;
    }
    
    
}
