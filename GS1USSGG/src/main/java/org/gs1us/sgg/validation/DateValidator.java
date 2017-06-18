package org.gs1us.sgg.validation;
import java.text.ParseException;
import java.util.List;

import org.gs1us.sgg.gbservice.api.AttributeDesc;
import org.gs1us.sgg.gbservice.api.ProductValidationError;
import org.gs1us.sgg.util.Util;

public class DateValidator extends SimpleAttributeValidator
{

    @Override
    public boolean validate(AttributeDesc attrDesc, String value, List<ProductValidationError> validationErrors)
    {
        if (value == null)
            return true;
        
        try
        {
            Util.DATE_FORMAT.parse(value);
            return true;
        }
        catch (ParseException e)
        {
            validationErrors.add(new ProductValidationErrorImpl(attrDesc.getName(), "Please enter a valid date (in MM/DD/YYYY format)"));
            return false;
        }
    }
    
}
