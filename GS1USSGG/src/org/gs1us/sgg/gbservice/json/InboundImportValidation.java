package org.gs1us.sgg.gbservice.json;

import java.util.Collection;

import org.gs1us.sgg.gbservice.api.ImportValidation;
import org.gs1us.sgg.gbservice.api.ImportValidationProduct;

public class InboundImportValidation implements ImportValidation
{

    private Collection<? extends InboundImportValidationProduct> m_validationProducts;

    public Collection<? extends InboundImportValidationProduct> getValidationProducts()
    {
        return m_validationProducts;
    }

    public void setValidationProducts(Collection<? extends InboundImportValidationProduct> validationProducts)
    {
        m_validationProducts = validationProducts;
    }
    
    
}
