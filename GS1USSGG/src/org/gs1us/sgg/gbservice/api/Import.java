package org.gs1us.sgg.gbservice.api;

import java.util.Date;

public interface Import extends GBAccountData
{
    String getId();
    String getGBAccountGln();
    String getFilename();
    String getFormat();
    Date getUploadDate();
    Date getValidatedDate();
    Date getConfirmedDate();
    ImportStatus getStatus();
    ImportPrevalidation getPrevalidation();
    ImportValidation getValidation();
    
}
