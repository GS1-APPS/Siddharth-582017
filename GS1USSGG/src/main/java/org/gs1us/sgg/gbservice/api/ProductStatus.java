package org.gs1us.sgg.gbservice.api;

import java.util.Collection;

public interface ProductStatus
{
    ProductState getState();
    Collection<? extends ProductValidationError> getValidationErrors();
    Quotation getQuotation();
}
