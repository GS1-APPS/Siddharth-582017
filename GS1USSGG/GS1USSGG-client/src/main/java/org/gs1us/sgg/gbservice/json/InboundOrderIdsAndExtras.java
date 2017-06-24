package org.gs1us.sgg.gbservice.json;

import java.util.List;

public class InboundOrderIdsAndExtras
{
    private List<String> m_orderIds;
    private List<InboundInvoiceExtra> m_extras;
    public List<String> getOrderIds()
    {
        return m_orderIds;
    }
    public void setOrderIds(List<String> orderIds)
    {
        m_orderIds = orderIds;
    }
    public List<InboundInvoiceExtra> getExtras()
    {
        return m_extras;
    }
    public void setExtras(List<InboundInvoiceExtra> extras)
    {
        m_extras = extras;
    }
    
    
}
