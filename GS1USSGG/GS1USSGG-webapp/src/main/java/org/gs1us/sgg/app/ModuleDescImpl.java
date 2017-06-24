package org.gs1us.sgg.app;

import java.util.List;

import org.gs1us.sgg.gbservice.api.AppDesc;
import org.gs1us.sgg.gbservice.api.AttributeDesc;
import org.gs1us.sgg.gbservice.api.AttributeSet;
import org.gs1us.sgg.gbservice.api.ModuleDesc;
import org.gs1us.sgg.gbservice.api.Product;

class ModuleDescImpl extends ModuleDesc
{
    private AttributeDesc m_selectionAttribute;
    private AttributeDesc m_startDateAttribute;
    private AttributeDesc m_endDateAttribute;
    private AttributeDesc m_paidThruDateAttribute;
    private AttributeDesc m_pendingPaidThruDateAttribute;
    private List<AttributeDesc> m_pkAttributeDescs;
    private List<AttributeDesc> m_userAttributeDescs;
    private Pricer m_pricer;



    public ModuleDescImpl(AttributeDesc selectionAttribute,
            AttributeDesc startDateAttribute, AttributeDesc endDateAttribute,
            AttributeDesc paidThruDateAttribute,
            AttributeDesc pendingPaidThruDateAttribute,
            List<AttributeDesc> pkAttributeDescs,
            List<AttributeDesc> userAttributeDescs,
            Pricer pricer)
    {
        super();
        m_selectionAttribute = selectionAttribute;
        m_startDateAttribute = startDateAttribute;
        m_endDateAttribute = endDateAttribute;
        m_paidThruDateAttribute = paidThruDateAttribute;
        m_pendingPaidThruDateAttribute = pendingPaidThruDateAttribute;
        m_pkAttributeDescs = pkAttributeDescs;
        m_userAttributeDescs = userAttributeDescs;
        m_pricer = pricer;
    }


    public AttributeDesc getSelectionAttribute()
    {
        return m_selectionAttribute;
    }


    public AttributeDesc getStartDateAttribute()
    {
        return m_startDateAttribute;
    }


    public AttributeDesc getEndDateAttribute()
    {
        return m_endDateAttribute;
    }


    public AttributeDesc getPaidThruDateAttribute()
    {
        return m_paidThruDateAttribute;
    }

    public AttributeDesc getPendingPaidThruDateAttribute()
    {
        return m_pendingPaidThruDateAttribute;
    }


    public List<AttributeDesc> getPkAttributeDescs()
    {
        return m_pkAttributeDescs;
    }


    public List<AttributeDesc> getUserAttributeDescs()
    {
        return m_userAttributeDescs;
    }
    
    


 
    public Pricer getPricer()
    {
        return m_pricer;
    }



}
