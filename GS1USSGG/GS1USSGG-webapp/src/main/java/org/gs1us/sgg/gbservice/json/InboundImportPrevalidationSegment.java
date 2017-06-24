package org.gs1us.sgg.gbservice.json;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.gs1us.sgg.gbservice.api.ImportPrevalidationColumn;
import org.gs1us.sgg.gbservice.api.ImportPrevalidationSegment;
import org.gs1us.sgg.gbservice.api.ImportPrevalidationSegmentSettings;

public class InboundImportPrevalidationSegment implements
        ImportPrevalidationSegment
{
    private String m_name;
    private int m_rowCount;
    private int m_nonblankRowCount;
    private Collection<String> m_segmentErrors;
    private List<? extends InboundImportPrevalidationColumn> m_columns;
    private InboundImportPrevalidationSegmentSettings m_settings;
    
    public String getName()
    {
        return m_name;
    }
    public void setName(String name)
    {
        m_name = name;
    }
    public int getRowCount()
    {
        return m_rowCount;
    }
    public void setRowCount(int rowCount)
    {
        m_rowCount = rowCount;
    }
    
    public int getNonblankRowCount()
    {
        return m_nonblankRowCount;
    }
    public void setNonblankRowCount(int nonblankRowCount)
    {
        m_nonblankRowCount = nonblankRowCount;
    }
    public Collection<String> getSegmentErrors()
    {
        return m_segmentErrors;
    }
    public void setSegmentErrors(Collection<String> segmentErrors)
    {
        m_segmentErrors = segmentErrors;
    }
    public void addSegmentError(String segmentError)
    {
        if (m_segmentErrors == null)
            m_segmentErrors = new ArrayList<>();
        m_segmentErrors.add(segmentError);
    }
    public List<? extends InboundImportPrevalidationColumn> getColumns()
    {
        return m_columns;
    }
    public void setColumns(List<? extends InboundImportPrevalidationColumn> columns)
    {
        m_columns = columns;
    }
    public InboundImportPrevalidationSegmentSettings getSettings()
    {
        return m_settings;
    }
    public void setSettings(InboundImportPrevalidationSegmentSettings settings)
    {
        m_settings = settings;
    }
    

}
