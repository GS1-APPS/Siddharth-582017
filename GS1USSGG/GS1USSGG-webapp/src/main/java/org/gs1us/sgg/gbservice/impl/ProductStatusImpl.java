package org.gs1us.sgg.gbservice.impl;

import java.util.Collection;

import org.gs1us.sgg.gbservice.api.Invoice;
import org.gs1us.sgg.gbservice.api.ProductState;
import org.gs1us.sgg.gbservice.api.ProductStatus;
import org.gs1us.sgg.gbservice.api.ProductValidationError;
import org.gs1us.sgg.gbservice.api.Quotation;

class ProductStatusImpl implements ProductStatus
{
    private ProductState m_state;
    private Collection<ProductValidationError> m_validationErrors;
    private Quotation m_quotation;

    
    public ProductStatusImpl(ProductState state,
            Collection<ProductValidationError> validationErrors,
            Quotation quotation)
    {
        super();
        m_state = state;
        m_validationErrors = validationErrors;
        m_quotation = quotation;
    }


    public ProductStatusImpl(ProductState state)
    {
        this(state, null, null);
    }


    public ProductState getState()
    {
        return m_state;
    }

    public Collection<ProductValidationError> getValidationErrors()
    {
        return m_validationErrors;
    }
    
    

    public Quotation getQuotation()
    {
        return m_quotation;
    }

    

}
