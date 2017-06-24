package org.gs1us.sgg.validation;
import java.util.List;

import org.gs1us.sgg.gbservice.api.AttributeDesc;
import org.gs1us.sgg.gbservice.api.ProductValidationError;
import org.gs1us.sgg.util.UserInputUtil;

public class GLNAttributeValidator extends SimpleAttributeValidator
{



    @Override
    public boolean validate(AttributeDesc attrDesc, String value, List<ProductValidationError> validationErrors)
    {
        if (value == null)
            return true;
        
        if (!UserInputUtil.isValidGln(value))
        {
            validationErrors.add(new ProductValidationErrorImpl(attrDesc.getName(), "The value must be a valid GLN"));
            return false;
        }
        else
            return true;
    }

    
}
