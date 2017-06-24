package org.gs1us.sgg.app.dwcode;


public class ServiceCreateUpdateRequest extends ServiceBase
{
    private ProductDetailsRequest m_productDetails;
    public ProductDetailsRequest getProductDetails()
    {
        return m_productDetails;
    }
    public void setProductDetails(ProductDetailsRequest productDetails)
    {
        m_productDetails = productDetails;
    }
    
    
}
