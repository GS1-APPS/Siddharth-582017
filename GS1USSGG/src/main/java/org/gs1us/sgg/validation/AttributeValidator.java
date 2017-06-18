package org.gs1us.sgg.validation;

import java.util.List;

import org.gs1us.sgg.gbservice.api.AttributeSet;
import org.gs1us.sgg.gbservice.api.AttributeDesc;
import org.gs1us.sgg.gbservice.api.HasAttributes;
import org.gs1us.sgg.gbservice.api.ProductValidationError;

public interface AttributeValidator
{
    public boolean validate(AttributeDesc attrDesc, HasAttributes objectToValidate, List<ProductValidationError> validationErrors);
}
