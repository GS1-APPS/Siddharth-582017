package org.gs1us.sgg.app.dwcode;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown=true)
public class ServiceReadResponse extends ServiceBase
{
    private Integer m_id;
    private Integer m_projectId;
    private ProductDetailsReadResponse m_productDetails;
    public Integer getId()
    {
        return m_id;
    }
    public void setId(Integer id)
    {
        m_id = id;
    }
    public Integer getProjectId()
    {
        return m_projectId;
    }
    public void setProjectId(Integer projectId)
    {
        m_projectId = projectId;
    }
    public ProductDetailsReadResponse getProductDetails()
    {
        return m_productDetails;
    }
    public void setProductDetails(ProductDetailsReadResponse productDetails)
    {
        m_productDetails = productDetails;
    }
    
    
}
