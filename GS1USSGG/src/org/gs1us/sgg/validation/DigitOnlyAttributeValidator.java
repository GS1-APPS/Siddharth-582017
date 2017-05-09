package org.gs1us.sgg.validation;
import java.text.ParseException;
import java.util.List;

import org.gs1us.sgg.gbservice.api.AttributeDesc;
import org.gs1us.sgg.gbservice.api.ProductValidationError;
import org.gs1us.sgg.util.Util;

public class DigitOnlyAttributeValidator extends SimpleAttributeValidator
{



    @Override
    public boolean validate(AttributeDesc attrDesc, String value, List<ProductValidationError> validationErrors)
    {
        if (value == null)
            return true;
        
        if (containsNonDigit(value))
        {
            validationErrors.add(new ProductValidationErrorImpl(attrDesc.getName(), "The value must be only contain digit characters"));
            return false;
        }
        else
            return true;
    }


    private boolean containsNonDigit(String value)
    {
        for (int i = 0; i < value.length(); i++)
        {
            char c = value.charAt(i);
            if (c < '0' || c > '9')
                return true;
        }
        return false;
    }
    
}
