package org.gs1us.sgg.validation;

import java.math.BigDecimal;
import java.util.List;

import org.gs1us.sgg.gbservice.api.AttributeDesc;
import org.gs1us.sgg.gbservice.api.ProductValidationError;

public class DecimalValidator extends SimpleAttributeValidator
{
    static boolean isNonNegativeNumber(String value)
    {
        try
        {
            BigDecimal d = new BigDecimal(value);
            return d.compareTo(BigDecimal.ZERO) >= 0;
        }
        catch (NumberFormatException e)
        {
            return false;
        }
    }


    @Override
    protected boolean validate(AttributeDesc attrDesc, String value,
            List<ProductValidationError> validationErrors)
    {
        if (value == null || value.length() == 0)
            return true;
        
        if (!DecimalValidator.isNonNegativeNumber(value))
        {
            validationErrors.add(new ProductValidationErrorImpl(attrDesc.getName(), "Value must be a non-negative number"));
            return false;
        }
        else
            return true;
        
    }

}
