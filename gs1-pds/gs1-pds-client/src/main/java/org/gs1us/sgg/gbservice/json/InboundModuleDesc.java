package org.gs1us.sgg.gbservice.json;

import java.util.List;

import org.gs1us.sgg.gbservice.api.AttributeDesc;
import org.gs1us.sgg.gbservice.api.ModuleDesc;

public class InboundModuleDesc extends ModuleDesc
{
    private InboundAttributeDesc m_selectionAttribute;
    private InboundAttributeDesc m_startDateAttribute;
    private InboundAttributeDesc m_endDateAttribute;
    private InboundAttributeDesc m_paidThruDateAttribute;
    private InboundAttributeDesc m_pendingPaidThruDateAttribute;
    private List<InboundAttributeDesc> m_pkAttributeDescs;
    private List<InboundAttributeDesc> m_userAttributeDescs;

    public AttributeDesc getSelectionAttribute()
    {
        return m_selectionAttribute;
    }
    public void setSelectionAttribute(InboundAttributeDesc appSelectionAttribute)
    {
        m_selectionAttribute = appSelectionAttribute;
    }
    public AttributeDesc getStartDateAttribute()
    {
        return m_startDateAttribute;
    }
    public void setStartDateAttribute(InboundAttributeDesc startDateAttribute)
    {
        m_startDateAttribute = startDateAttribute;
    }
    public AttributeDesc getEndDateAttribute()
    {
        return m_endDateAttribute;
    }
    public void setEndDateAttribute(InboundAttributeDesc endDateAttribute)
    {
        m_endDateAttribute = endDateAttribute;
    }
    public AttributeDesc getPaidThruDateAttribute()
    {
        return m_paidThruDateAttribute;
    }
    public void setPaidThruDateAttribute(InboundAttributeDesc paidThruDateAttribute)
    {
        m_paidThruDateAttribute = paidThruDateAttribute;
    }
    public AttributeDesc getPendingPaidThruDateAttribute()
    {
        return m_pendingPaidThruDateAttribute;
    }
    public void setPendingPaidThruDateAttribute(
            InboundAttributeDesc pendingPaidThruDateAttribute)
    {
        m_pendingPaidThruDateAttribute = pendingPaidThruDateAttribute;
    }
    
    public List<InboundAttributeDesc> getPkAttributeDescs()
    {
        return m_pkAttributeDescs;
    }
    public void setPkAttributeDescs(List<InboundAttributeDesc> pkAttributeDescs)
    {
        m_pkAttributeDescs = pkAttributeDescs;
    }
    public List<? extends AttributeDesc> getUserAttributeDescs()
    {
        return m_userAttributeDescs;
    }
    public void setUserAttributeDescs(
            List<InboundAttributeDesc> userAttributeDescs)
    {
        m_userAttributeDescs = userAttributeDescs;
    }

}
