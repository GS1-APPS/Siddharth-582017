package org.gs1us.sgg.gbservice.api;

import java.util.List;

public class ValidationException extends GlobalBrokerException
{
    private List<ProductValidationError> m_errors;
    
    // For deserialization
    public ValidationException()
    {
        super();
    }

    public ValidationException(List<ProductValidationError> errors)
    {
        super();
        m_errors = errors;
    }

    public List<ProductValidationError> getErrors()
    {
        return m_errors;
    }

    // To support deserialization 
    public void setErrors(List<ProductValidationError> errors)
    {
        m_errors = errors;
    }
    
    
    
}
