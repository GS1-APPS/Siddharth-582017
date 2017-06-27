package org.gs1us.sgg.validation;

import org.gs1us.sgg.gbservice.api.ProductValidationError;

public class ProductValidationErrorImpl implements ProductValidationError
{
    private String m_path;
    private String m_errorMessage;
    
    public ProductValidationErrorImpl(String path, String errorMessage)
    {
        super();
        m_path = path;
        m_errorMessage = errorMessage;
    }
    public String getPath()
    {
        return m_path;
    }
    public String getErrorMessage()
    {
        return m_errorMessage;
    }
    

}
