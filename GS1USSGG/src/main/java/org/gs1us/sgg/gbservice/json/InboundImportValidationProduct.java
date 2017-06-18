package org.gs1us.sgg.gbservice.json;

import org.gs1us.sgg.gbservice.api.ImportValidationProduct;
import org.gs1us.sgg.gbservice.api.ProductStatus;

public class InboundImportValidationProduct implements ImportValidationProduct
{
    private String m_gtin;
    private InboundProductStatus m_status;
    public String getGtin()
    {
        return m_gtin;
    }
    public void setGtin(String gtin)
    {
        m_gtin = gtin;
    }
    public InboundProductStatus getStatus()
    {
        return m_status;
    }
    public void setStatus(InboundProductStatus status)
    {
        m_status = status;
    }
    
    

}
