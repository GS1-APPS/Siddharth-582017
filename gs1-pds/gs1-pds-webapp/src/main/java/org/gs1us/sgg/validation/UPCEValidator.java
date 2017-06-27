package org.gs1us.sgg.validation;

import java.util.List;

import org.gs1us.sgg.gbservice.api.HasAttributes;
import org.gs1us.sgg.gbservice.api.Product;
import org.gs1us.sgg.gbservice.api.AttributeDesc;
import org.gs1us.sgg.gbservice.api.ProductValidationError;
import org.gs1us.sgg.util.UPCE;

public class UPCEValidator implements AttributeValidator
{

    @Override
    public boolean validate(AttributeDesc attrDesc, HasAttributes objectToValidate, List<ProductValidationError> validationErrors)
    {
        String value = objectToValidate.getAttributes().getAttribute(attrDesc);
        if (value != null)
        {
            Product product = (Product)objectToValidate;
            String gtin = product.getGtin();
            String upce = UPCE.gtinToUPCE(gtin);
            if (upce == null)
            {
                validationErrors.add(new ProductValidationErrorImpl(attrDesc.getName(), "This GTIN cannot be encoded as a UPC-E symbol."));
                return false;
            }
        
        }
        return true;
    }
 
}
