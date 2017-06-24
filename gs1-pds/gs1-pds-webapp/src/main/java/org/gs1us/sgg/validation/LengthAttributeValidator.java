package org.gs1us.sgg.validation;
import java.util.List;

import org.gs1us.sgg.gbservice.api.AttributeDesc;
import org.gs1us.sgg.gbservice.api.ProductValidationError;

public class LengthAttributeValidator extends SimpleAttributeValidator
{
    private int m_minLength;
    private int m_maxLength;
    

    public LengthAttributeValidator(int minLength, int maxLength)
    {
        super();
        m_minLength = minLength;
        m_maxLength = maxLength;
    }

    public LengthAttributeValidator(int maxLength)
    {
        this(1, maxLength);
    }

    @Override
    public boolean validate(AttributeDesc attrDesc, String value, List<ProductValidationError> validationErrors)
    {
        if (value == null)
            return true;
        
        if (value.length() < m_minLength || value.length() > m_maxLength)
        {
            if (m_minLength == m_maxLength)
                validationErrors.add(new ProductValidationErrorImpl(attrDesc.getName(), "The value must be exactly " + m_minLength + " characters"));
            else
                validationErrors.add(new ProductValidationErrorImpl(attrDesc.getName(), "The value must be between " + m_minLength + " and " + m_maxLength + " characters (inclusive)"));
            return false;
        }
        else
            return true;
    }
    
}
