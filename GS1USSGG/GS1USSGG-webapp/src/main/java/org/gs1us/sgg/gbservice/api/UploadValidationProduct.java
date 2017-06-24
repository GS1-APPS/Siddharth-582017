package org.gs1us.sgg.gbservice.api;

import java.util.Collection;

public interface UploadValidationProduct {
    public String getGtin();
    public ProductStatus getStatus();
    public String getStatusCode() ;
    public Collection<? extends ProductValidationError> getValidationErrors();
}
