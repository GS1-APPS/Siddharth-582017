package org.gs1us.sgg.gbservice.api;

import java.util.Iterator;
import java.util.List;

import org.gs1us.sgg.util.AdjoinIterator;

public class AttributeSchemaModule
{
    private AttributeDesc m_selectionAttributeDesc;
    private List<AttributeDesc> m_attributeDescs;
    
    public AttributeSchemaModule(AttributeDesc selectionAttributeDesc, List<? extends AttributeDesc> attributeDescs)
    {
        super();
        m_selectionAttributeDesc = selectionAttributeDesc;
        m_attributeDescs = (List<AttributeDesc>) attributeDescs;
    }

    public Iterator<AttributeDesc> iterator()
    {
        if (m_selectionAttributeDesc == null)
            return m_attributeDescs.iterator();
        else
            return new AdjoinIterator(m_selectionAttributeDesc, m_attributeDescs.iterator());
    }
    
    public boolean isSelected(HasAttributes o)
    {
        return isSelected(o.getAttributes());
    }

    public boolean isSelected(AttributeSet attributes)
    {
        AttributeDesc attrDesc = m_selectionAttributeDesc;
        if (attrDesc == null)
            return true;
        else if (attributes == null)
            return false;
        else
            return attributes.getBooleanAttribute(attrDesc);
    }

}
