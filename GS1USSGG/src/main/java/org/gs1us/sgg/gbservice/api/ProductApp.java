package org.gs1us.sgg.gbservice.api;

import java.util.Date;

public class ProductApp
{
    private AppDesc m_appDesc;
    private Product m_product;
    public ProductApp(AppDesc appDesc, Product product)
    {
        super();
        m_appDesc = appDesc;
        m_product = product;
    }
    public AppDesc getAppDesc()
    {
        return m_appDesc;
    }
    public Product getProduct()
    {
        return m_product;
    }
    
    public boolean isSelected()
    {
        AttributeDesc appSelectionAttribute = m_appDesc.getProductModuleDesc().getSelectionAttribute();
        if (appSelectionAttribute == null)
            return true;
        else
            return m_product.getAttributes().getBooleanAttribute(appSelectionAttribute);
    }
    
    public boolean isPaid(Date asOfDate)
    {
        AttributeDesc appSelectionDesc = m_appDesc.getProductModuleDesc().getSelectionAttribute();
        if (appSelectionDesc == null)
            return false;
        
        boolean isSelected = m_product.getAttributes().getBooleanAttribute(appSelectionDesc);
        AttributeDesc paidThruDesc = m_appDesc.getProductModuleDesc().getPaidThruDateAttribute();
        if (paidThruDesc == null)
            return false;
        
        Date paidThruDate = m_product.getAttributes().getDateAttribute(paidThruDesc);

        return isSelected && (paidThruDate != null && !paidThruDate.before(asOfDate));
    }
}
