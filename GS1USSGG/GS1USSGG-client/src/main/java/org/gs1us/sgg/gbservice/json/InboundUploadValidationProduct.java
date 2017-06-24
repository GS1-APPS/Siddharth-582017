package org.gs1us.sgg.gbservice.json;


import java.util.Collection;

import org.gs1us.sgg.gbservice.api.ProductStatus;
import org.gs1us.sgg.gbservice.api.ProductValidationError;
import org.gs1us.sgg.gbservice.api.UploadValidationProduct;

public class InboundUploadValidationProduct implements UploadValidationProduct {

    private String m_gtin;
    private ProductStatus m_status;
    private String m_statusCode;
    
    public String getGtin()
    {
        return m_gtin;
    }
    public void setGtin(String gtin)
    {
        m_gtin = gtin;
    }
    public ProductStatus getStatus()
    {
        return m_status;
    }
    public void setStatus(ProductStatus status)
    {
        m_status = status;
    }
    
	public String getStatusCode() {
		return m_statusCode;
	}
	
	public void setStatusCode(String m_statusCode) {
		this.m_statusCode = m_statusCode;
	}
    
    public Collection<? extends ProductValidationError> getValidationErrors()
    {
        return null== m_status?null: m_status.getValidationErrors();
    }

	
}
