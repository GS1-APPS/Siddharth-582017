package org.gs1us.sgg.gbservice.impl;

import org.gs1us.sgg.gbservice.api.ImportValidationProduct;
import org.gs1us.sgg.gbservice.api.ProductStatus;

public class ImportValidationProductImpl implements ImportValidationProduct
{
    private String m_gtin;
    private ProductStatus m_status;
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

    
    

}
