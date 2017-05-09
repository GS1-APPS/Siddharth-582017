package org.gs1us.sgg.dao.jpa;

import java.util.Collection;
import java.util.Date;

import javax.persistence.Basic;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.gs1us.sgg.dao.ImportRecord;
import org.gs1us.sgg.daoutil.KeyMapper;
import org.gs1us.sgg.daoutil.jpa.LongKeyMapper;
import org.gs1us.sgg.gbservice.api.ImportPrevalidation;
import org.gs1us.sgg.gbservice.api.ImportStatus;
import org.gs1us.sgg.gbservice.api.ImportValidation;
import org.gs1us.sgg.gbservice.api.ProductStatus;

@Entity
@Table(name="import_record")
public class JpaImportRecord extends ImportRecord
{
    protected static final KeyMapper<Long> KEY_MAPPER = new LongKeyMapper();
    
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private long m_id;

    @Basic
    private String m_gbAccountGln;
    
    @Basic
    private String m_filename;
    
    @Basic
    private String m_format;
    
    @Basic 
    private Date m_uploadDate;
    
    @Basic 
    private Date m_validatedDate;
    
    @Basic 
    private Date m_confirmedDate;
    
    @Basic
    private ImportStatus m_status;
    
    @Basic
    private String m_importFileId;
    
    @Basic
    private ImportPrevalidation m_prevalidation;
    
    @Basic
    private ImportValidation m_validation;

    public String getId()
    {
        return KEY_MAPPER.keyToId(m_id);
    }

    public void setId(String id)
    {
        m_id = KEY_MAPPER.idToKey(id);
    }

    public String getGBAccountGln()
    {
        return m_gbAccountGln;
    }

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

    public String getImportFileId()
    {
        return m_importFileId;
    }

    public void setImportFileId(String importFileId)
    {
        m_importFileId = importFileId;
    }
    
    

    public ImportPrevalidation getPrevalidation()
    {
        return m_prevalidation;
    }

    public void setPrevalidation(ImportPrevalidation prevalidation)
    {
        m_prevalidation = prevalidation;
    }

    public ImportValidation getValidation()
    {
        return m_validation;
    }

    public void setValidation(ImportValidation validation)
    {
        m_validation = validation;
    }

   

}
