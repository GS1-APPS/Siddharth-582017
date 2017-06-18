package org.gs1us.sgg.gbservice.api;

import java.util.Collection;

public interface ImportValidation
{
    Collection<? extends ImportValidationProduct> getValidationProducts();
}
