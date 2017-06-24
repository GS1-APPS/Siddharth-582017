package org.gs1us.sgg.app.dwcode;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude(value=Include.NON_NULL)
public class ProductDetailsRequest
{
    private String m_brand;
    private String m_productName;
    private String m_subBrand;
    private String m_description;
    private String m_color;
    private String m_flavor;
    private String m_scent;
    private String m_size;
    private String m_imageUrl;
    //@JsonInclude(value=Include.ALWAYS)
    private ProductQuantity[] m_quantities;
    public String getBrand()
    {
        return m_brand;
    }
    public void setBrand(String brand)
    {
        m_brand = brand;
    }
    public String getProductName()
    {
        return m_productName;
    }
    public void setProductName(String productName)
    {
        m_productName = productName;
    }
    public String getSubBrand()
    {
        return m_subBrand;
    }
    public void setSubBrand(String subBrand)
    {
        m_subBrand = subBrand;
    }
    public String getDescription()
    {
        return m_description;
    }
    public void setDescription(String description)
    {
        m_description = description;
    }
    public String getColor()
    {
        return m_color;
    }
    public void setColor(String color)
    {
        m_color = color;
    }
    public String getFlavor()
    {
        return m_flavor;
    }
    public void setFlavor(String flavor)
    {
        m_flavor = flavor;
    }
    public String getScent()
    {
        return m_scent;
    }
    public void setScent(String scent)
    {
        m_scent = scent;
    }
    public String getSize()
    {
        return m_size;
    }
    public void setSize(String size)
    {
        m_size = size;
    }
    public String getImageUrl()
    {
        return m_imageUrl;
    }
    public void setImageUrl(String imageUrl)
    {
        m_imageUrl = imageUrl;
    }
    //@JsonInclude(value=Include.ALWAYS)
    public ProductQuantity[] getQuantities()
    {
        return m_quantities;
    }
    public void setQuantities(ProductQuantity[] quantities)
    {
        m_quantities = quantities;
    }
    
    
    
}
