package org.gs1us.sgg.gbservice.impl;

import java.util.Collection;

import org.gs1us.sgg.gbservice.api.ImportValidation;
import org.gs1us.sgg.gbservice.api.ImportValidationProduct;

public class ImportValidationImpl implements ImportValidation
{
    private Collection<? extends ImportValidationProduct> m_validationProducts;

    public Collection<? extends ImportValidationProduct> getValidationProducts()
    {
        return m_validationProducts;
    }

    public void setValidationProducts(
            Collection<? extends ImportValidationProduct> validationProducts)
    {
        m_validationProducts = validationProducts;
    }

    
    

}
