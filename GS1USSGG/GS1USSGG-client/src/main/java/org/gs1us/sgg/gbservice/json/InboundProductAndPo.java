package org.gs1us.sgg.gbservice.json;

public class InboundProductAndPo
{
    private InboundProduct m_product;
    private InboundPurchaseOrder m_po;
    public InboundProduct getProduct()
    {
        return m_product;
    }
    public void setProduct(InboundProduct product)
    {
        m_product = product;
    }
    public InboundPurchaseOrder getPo()
    {
        return m_po;
    }
    public void setPo(InboundPurchaseOrder po)
    {
        m_po = po;
    }
    
    
}
