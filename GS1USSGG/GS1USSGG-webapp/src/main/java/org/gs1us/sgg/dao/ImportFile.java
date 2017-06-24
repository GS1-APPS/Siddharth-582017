package org.gs1us.sgg.dao;

import org.gs1us.sgg.gbservice.api.Import;
import org.gs1us.sgg.gbservice.api.ImportStatus;

public abstract class ImportFile
{
    public abstract String getId();
    public abstract void setId(String id);

    public abstract byte[] getContent();
    public abstract void setContent(byte[] content);

}
