package org.gs1us.sgg.gbservice.json;

import java.util.List;

import org.gs1us.sgg.gbservice.api.ProductValidationError;
import org.gs1us.sgg.gbservice.api.ValidationException;

public class ExceptionInfo
{
    private String m_exception;
    private String m_message;
    private List<? extends ProductValidationError> m_validationErrors;
    
    public ExceptionInfo()
    {
        
    }
    
    public ExceptionInfo(ValidationException e, String message)
    {
        this((Throwable)e, message);
        m_validationErrors = e.getErrors();
    }
    
    public ExceptionInfo(ValidationException e)
    {
        this((Throwable)e);
        m_validationErrors = e.getErrors();
    }
    
    public ExceptionInfo(String exception, String message)
    {
        super();
        m_exception = exception;
        m_message = message;
    }

    public ExceptionInfo(Throwable t, String message)
    {
        m_exception = t.getClass().getSimpleName();
        m_message = message;
    }

    public ExceptionInfo(Throwable t)
    {
        m_exception = t.getClass().getSimpleName();
        m_message = t.getMessage();
    }

    public String getException()
    {
        return m_exception;
    }
    

    public List<? extends ProductValidationError> getValidationErrors()
    {
        return m_validationErrors;
    }
    
    

    public void setValidationErrors(List<InboundProductValidationError> validationErrors)
    {
        m_validationErrors = validationErrors;
    }

    public void setException(String exception)
    {
        m_exception = exception;
    }

    public String getMessage()
    {
        return m_message;
    }

    public void setMessage(String message)
    {
        m_message = message;
    }
    
}
