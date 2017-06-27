package org.gs1us.sgg.gbservice.api;

public enum ImportStatus
{
    /**
     * In the process of receiving data (not currently used). Actions from this state: none.
     */
    UPLOADING,
    
    /**
     * File is uploaded, ready to change settings. Actions from this state: changeSettings, delete
     */
    UPLOADED,
    
    /**
     * File was unable to be uploaded, due to indecipherable format or other decoding error. The prevalidation contains
     * a file error. Actions from this state: delete.
     */
    UPLOAD_FAILED,
    
     /**
     * File is in the process of validating. Actions from this state: none.
     */
    VALIDATING,
    
    /**
     * File is validated and ready to confirm. Actions from this state: changeSettings, confirm, delete.
     */
    VALIDATED,
    
    /**
     * File is confirmed and in the middle of processing. Actions from this state: none
     */
    PROCESSING,
    
    /**
     * File has completed all processing. Actions from this state: delete.
     */
    PROCESSED
}
