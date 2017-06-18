package org.gs1us.sgg.gbservice.json;

import java.util.Collection;
import java.util.Date;

import org.gs1us.sgg.gbservice.api.Import;
import org.gs1us.sgg.gbservice.api.ImportStatus;
import org.gs1us.sgg.gbservice.api.ImportValidation;
import org.gs1us.sgg.gbservice.api.ProductStatus;

import com.fasterxml.jackson.annotation.JsonProperty;

public class InboundImport implements Import
{
    private String m_id;
    private String m_gbAccountGln;
    private String m_filename;
    private String m_format;
    private Date m_uploadDate;
    private Date m_validatedDate;
    private Date m_confirmedDate;
    private ImportStatus m_status;
    private InboundImportPrevalidation m_prevalidation;
    private InboundImportValidation m_validation;
    
    public String getId()
    {
        return m_id;
    }
    public void setId(String id)
    {
        m_id = id;
    }
    @JsonProperty("gln")
    public String getGBAccountGln()
    {
        return m_gbAccountGln;
    }
    @JsonProperty("gln")
    public void setGBAccountGln(String gbAccountGln)
    {
        m_gbAccountGln = gbAccountGln;
    }
    public String getFilename()
    {
        return m_filename;
    }
    public void setFilename(String filename)
    {
        m_filename = filename;
    }
    public String getFormat()
    {
        return m_format;
    }
    public void setFormat(String format)
    {
        m_format = format;
    }
    public Date getUploadDate()
    {
        return m_uploadDate;
    }
    public void setUploadDate(Date uploadDate)
    {
        m_uploadDate = uploadDate;
    }
    
    public Date getValidatedDate()
    {
        return m_validatedDate;
    }
    public void setValidatedDate(Date validatedDate)
    {
        m_validatedDate = validatedDate;
    }
    public Date getConfirmedDate()
    {
        return m_confirmedDate;
    }
    public void setConfirmedDate(Date confirmedDate)
    {
        m_confirmedDate = confirmedDate;
    }
    public ImportStatus getStatus()
    {
        return m_status;
    }
    public void setStatus(ImportStatus status)
    {
        m_status = status;
    }
    
    public InboundImportPrevalidation getPrevalidation()
    {
        return m_prevalidation;
    }
    public void setPrevalidation(InboundImportPrevalidation prevalidation)
    {
        m_prevalidation = prevalidation;
    }
    public InboundImportValidation getValidation()
    {
        return m_validation;
    }
    public void setValidation(InboundImportValidation validation)
    {
        m_validation = validation;
    }



}
