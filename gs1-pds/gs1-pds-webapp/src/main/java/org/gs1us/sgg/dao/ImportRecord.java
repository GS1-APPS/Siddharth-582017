package org.gs1us.sgg.dao;

import java.util.Date;

import org.gs1us.sgg.gbservice.api.Import;
import org.gs1us.sgg.gbservice.api.ImportPrevalidation;
import org.gs1us.sgg.gbservice.api.ImportStatus;
import org.gs1us.sgg.gbservice.api.ImportValidation;
import org.gs1us.sgg.gbservice.api.ProductStatus;

public abstract class ImportRecord implements Import
{

    @Override
    public abstract String getId();
    public abstract void setId(String id);

    @Override
    public abstract String getGBAccountGln();
    public abstract void setGBAccountGln(String gln);

    @Override
    public abstract String getFilename();
    public abstract void setFilename(String filename);

    @Override
    public abstract String getFormat();
    public abstract void setFormat(String format);

    @Override
    public abstract Date getUploadDate();
    public abstract void setUploadDate(Date uploadDate);

    @Override
    public abstract Date getValidatedDate();
    public abstract void setValidatedDate(Date validatedDate);

    @Override
    public abstract Date getConfirmedDate();
    public abstract void setConfirmedDate(Date confirmedDate);

    @Override
    public abstract ImportStatus getStatus();
    public abstract void setStatus(ImportStatus status);
    
    @Override
    public abstract ImportPrevalidation getPrevalidation();
    public abstract void setPrevalidation(ImportPrevalidation prevalidation);
    
    @Override
    public abstract ImportValidation getValidation();
    public abstract void setValidation(ImportValidation validation);
    
    public abstract String getImportFileId();
    public abstract void setImportFileId(String importFileId);

}
