package org.gs1us.sgg.validation;
import java.text.ParseException;
import java.util.Date;
import java.util.List;

import org.gs1us.sgg.gbservice.api.AttributeDesc;
import org.gs1us.sgg.gbservice.api.ProductValidationError;
import org.gs1us.sgg.util.Util;

public class AffirmationValidator extends SimpleAttributeValidator
{

    @Override
    public boolean validate(AttributeDesc attrDesc, String value, List<ProductValidationError> validationErrors)
    {
        if (value == null)
        {
            validationErrors.add(new ProductValidationErrorImpl(attrDesc.getName(), "You must confirm this."));
            return false;
        }
        else
            return true;

    }
    
}
