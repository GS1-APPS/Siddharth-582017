package org.gs1us.sgg.validation;

import java.util.Iterator;
import java.util.List;

import org.gs1us.sgg.attribute.AttributeDescImpl;
import org.gs1us.sgg.gbservice.api.Action;
import org.gs1us.sgg.gbservice.api.AttributeDesc;
import org.gs1us.sgg.gbservice.api.AttributeSchema;
import org.gs1us.sgg.gbservice.api.GlobalBrokerException;
import org.gs1us.sgg.gbservice.api.HasAttributes;
import org.gs1us.sgg.gbservice.api.Product;
import org.gs1us.sgg.gbservice.api.ProductValidationError;

public class Validator
{
    public void validateProductData(AttributeSchema attributeSchema, String[] gcps, Product product, Action action, List<ProductValidationError> validationErrors) throws GlobalBrokerException
    {
        String gtin = product.getGtin();
        GtinValidator gtinValidator = new GtinValidator(gcps);
        gtinValidator.validate(null, gtin, validationErrors);
        
        DataAccuracyAckValidator dataAccuracyValidator = new DataAccuracyAckValidator();
        dataAccuracyValidator.validate(null, product.getDataAccuracyAckUser(), validationErrors);
        
        validateUserAttributes(attributeSchema, action, product, validationErrors);
        
        return;
    }

    public void validateUserAttributes(
            AttributeSchema attributeSchema,
            Action action,
            HasAttributes objectToValidate,
            List<ProductValidationError> validationErrors)
    {
        for (Iterator<AttributeDesc> i = attributeSchema.selectedAttributesIterator(objectToValidate); i.hasNext();)
        {
            AttributeDesc attrDesc = i.next();

            if (action.matches(attrDesc.getActions()))
            {
                ((AttributeDescImpl)attrDesc).validateAttribute(objectToValidate, validationErrors);
            }
        }
    }

}
