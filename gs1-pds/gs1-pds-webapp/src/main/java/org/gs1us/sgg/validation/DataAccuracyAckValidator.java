package org.gs1us.sgg.validation;

import java.util.List;

import org.gs1us.sgg.gbservice.api.AttributeDesc;
import org.gs1us.sgg.gbservice.api.ProductValidationError;

public class DataAccuracyAckValidator extends SimpleAttributeValidator
{
    public DataAccuracyAckValidator()
    {
        super();
    }
    
    @Override
    public boolean validate(AttributeDesc attrDesc, String dataAccuracyAckUser, List<ProductValidationError> errors)
    {
        int oldCount = errors.size();
        if (dataAccuracyAckUser == null)
            errors.add(new ProductValidationErrorImpl("dataAccuracyAck", "You must verify the accuracy of all data entered here and check the box to confirm."));

        return errors.size() == oldCount;
    }


}
