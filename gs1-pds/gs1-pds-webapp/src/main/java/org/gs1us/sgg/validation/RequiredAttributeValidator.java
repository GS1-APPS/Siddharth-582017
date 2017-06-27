package org.gs1us.sgg.validation;

import java.util.List;

import org.gs1us.sgg.gbservice.api.AttributeDesc;
import org.gs1us.sgg.gbservice.api.ProductValidationError;

public class RequiredAttributeValidator extends SimpleAttributeValidator
{
    @Override
    public boolean validate(AttributeDesc attrDesc, String value,
            List<ProductValidationError> validationErrors)
    {
        if (value == null || value.length() == 0)
        {
            validationErrors.add(new ProductValidationErrorImpl(attrDesc.getName(), "Missing " + attrDesc.getTitle()));
            return false;
        }
        else
            return true;
    }
}
