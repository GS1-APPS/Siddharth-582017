package org.gs1us.sgg.validation;

import java.util.List;

import org.gs1us.sgg.gbservice.api.AttributeDesc;
import org.gs1us.sgg.gbservice.api.ProductValidationError;


public class EvenLengthAttributeValidator extends SimpleAttributeValidator
{
    @Override
    public boolean validate(AttributeDesc attrDesc, String value,
            List<ProductValidationError> validationErrors)
    {
        if (value == null || value.length() % 2 != 0)
        {
            validationErrors.add(new ProductValidationErrorImpl(attrDesc.getName(), attrDesc.getTitle() + " must be an even number of characters"));
            return false;
        }
        else
            return true;
    }
}
