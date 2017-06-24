package org.gs1us.sgg.gbservice.api;

import java.util.Collection;
import java.util.List;

public interface ImportPrevalidation
{
    public String getFileError();
    public List<? extends ImportPrevalidationSegment> getSegments();
}
