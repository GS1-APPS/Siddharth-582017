package org.gs1us.sgg.validation;

import java.util.List;

import org.gs1us.sgg.gbservice.api.AttributeSet;
import org.gs1us.sgg.gbservice.api.HasAttributes;
import org.gs1us.sgg.gbservice.api.Product;
import org.gs1us.sgg.gbservice.api.AttributeDesc;
import org.gs1us.sgg.gbservice.api.ProductValidationError;

abstract class SimpleAttributeValidator implements AttributeValidator
{

    @Override
    public boolean validate(AttributeDesc attrDesc, HasAttributes objectToValidate, List<ProductValidationError> validationErrors)
    {
        AttributeSet attributes = objectToValidate.getAttributes();
        return validate(attrDesc, attributes.getAttribute(attrDesc), validationErrors);
    }

    protected abstract boolean validate(AttributeDesc attrDesc, String attribute, List<ProductValidationError> validationErrors);

}
