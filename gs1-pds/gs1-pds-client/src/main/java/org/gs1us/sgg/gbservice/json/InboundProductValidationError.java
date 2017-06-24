package org.gs1us.sgg.gbservice.json;

import org.gs1us.sgg.gbservice.api.ProductValidationError;

public class InboundProductValidationError implements ProductValidationError
{
    private String m_path;
    private String m_errorMessage;
    public String getPath()
    {
        return m_path;
    }
    public void setPath(String path)
    {
        m_path = path;
    }
    public String getErrorMessage()
    {
        return m_errorMessage;
    }
    public void setErrorMessage(String errorMessage)
    {
        m_errorMessage = errorMessage;
    }
    

}
