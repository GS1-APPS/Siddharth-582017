package org.gs1us.sgg.validation;

import java.util.List;

import org.gs1us.sgg.gbservice.api.AttributeEnumValue;
import org.gs1us.sgg.gbservice.api.HasAttributes;
import org.gs1us.sgg.gbservice.api.Product;
import org.gs1us.sgg.gbservice.api.AttributeDesc;
import org.gs1us.sgg.gbservice.api.AttributeSet;
import org.gs1us.sgg.gbservice.api.ProductValidationError;

public class MeasurementAttributeValidator implements AttributeValidator
{

    @Override
    public boolean validate(AttributeDesc attrDesc, HasAttributes objectToValidate, List<ProductValidationError> validationErrors)
    {
        AttributeSet attributes = objectToValidate.getAttributes();
        int errorCount = validationErrors.size();
        
        String value = attributes.getAttribute(attrDesc);
        
        if (value == null || value.length() == 0)
            return true;
        
        if (!DecimalValidator.isNonNegativeNumber(value))
        {
            validationErrors.add(new ProductValidationErrorImpl(attrDesc.getName(), "Value must be a non-negative number"));
        }
        
        String uomAttrName = attrDesc.getName() + "_uom";
        String uomValue = attributes.getAttribute(uomAttrName);
        if (uomValue == null)
            validationErrors.add(new ProductValidationErrorImpl(uomAttrName, "Please choose a unit of measure"));
        else if (!isValidValue(uomValue, attrDesc.getEnumValues()))
            validationErrors.add(new ProductValidationErrorImpl(uomAttrName, "Invalid unit of measure"));
        
        return validationErrors.size() == errorCount;
    }

    private boolean isValidValue(String uomValue, List<? extends AttributeEnumValue> enumValues)
    {
        for (AttributeEnumValue enumValue : enumValues)
        {
            if (enumValue.getCode().equals(uomValue))
                return true;
        }
        return false;
    }

 
}
