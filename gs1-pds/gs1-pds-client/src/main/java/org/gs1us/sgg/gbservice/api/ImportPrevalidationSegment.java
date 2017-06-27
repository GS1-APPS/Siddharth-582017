package org.gs1us.sgg.gbservice.api;

import java.util.Collection;
import java.util.List;

public interface ImportPrevalidationSegment
{
    public String getName();
    public int getRowCount();
    public int getNonblankRowCount();
    public Collection<String> getSegmentErrors();
    public List<? extends ImportPrevalidationColumn> getColumns();
    public ImportPrevalidationSegmentSettings getSettings();
}
