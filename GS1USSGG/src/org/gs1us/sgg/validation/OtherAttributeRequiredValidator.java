package org.gs1us.sgg.validation;

import java.util.List;

import org.gs1us.sgg.gbservice.api.AttributeSet;
import org.gs1us.sgg.gbservice.api.AttributeDesc;
import org.gs1us.sgg.gbservice.api.HasAttributes;
import org.gs1us.sgg.gbservice.api.ProductValidationError;

public class OtherAttributeRequiredValidator implements AttributeValidator
{
    private AttributeDesc m_otherAttrDesc;
    
    
    
    public OtherAttributeRequiredValidator(AttributeDesc otherAttrDesc)
    {
        super();
        m_otherAttrDesc = otherAttrDesc;
    }



    @Override
    public boolean validate(AttributeDesc attrDesc, HasAttributes objectToValidate, List<ProductValidationError> validationErrors)
    {
        AttributeSet attributes = objectToValidate.getAttributes();
        String value = attributes.getAttribute(attrDesc);
        if (value == null || value.length() == 0)
            return true;
        
        String otherValue = attributes.getAttribute(m_otherAttrDesc);
        if (otherValue == null || otherValue.length() == 0)
        {
            validationErrors.add(new ProductValidationErrorImpl(attrDesc.getName(), String.format("%s may only be specified if %s is also specified", attrDesc.getTitle(), m_otherAttrDesc.getTitle())));
            return false;
            
        }
        else
            return true;
    }

}
