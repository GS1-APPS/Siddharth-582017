package org.gs1us.sgg.gbservice.json;

import java.util.Collection;

import org.gs1us.sgg.gbservice.api.ProductState;
import org.gs1us.sgg.gbservice.api.ProductStatus;
import org.gs1us.sgg.gbservice.api.ProductValidationError;
import org.gs1us.sgg.gbservice.api.Quotation;

public class InboundProductStatus implements ProductStatus
{
    private ProductState m_state;
    private Collection<InboundProductValidationError> m_validationErrors;
    private InboundQuotation m_quotation;
    public ProductState getState()
    {
        return m_state;
    }
    public void setState(ProductState state)
    {
        m_state = state;
    }
    public Collection<? extends ProductValidationError> getValidationErrors()
    {
        return m_validationErrors;
    }
    public void setValidationErrors(Collection<InboundProductValidationError> validationErrors)
    {
        m_validationErrors = validationErrors;
    }
    public InboundQuotation getQuotation()
    {
        return m_quotation;
    }
    public void setQuotation(InboundQuotation quotation)
    {
        m_quotation = quotation;
    }


}
