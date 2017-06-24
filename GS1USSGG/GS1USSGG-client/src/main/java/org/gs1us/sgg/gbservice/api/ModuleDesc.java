package org.gs1us.sgg.gbservice.api;

import java.util.Date;
import java.util.List;

public abstract class ModuleDesc
{
    public abstract AttributeDesc getSelectionAttribute();

    public abstract AttributeDesc getStartDateAttribute();
    public abstract AttributeDesc getEndDateAttribute();
    public abstract AttributeDesc getPaidThruDateAttribute();
    public abstract AttributeDesc getPendingPaidThruDateAttribute();

    public abstract List<? extends AttributeDesc> getPkAttributeDescs();
    public abstract List<? extends AttributeDesc> getUserAttributeDescs();
    
    public Date getStartDate(AttributeSet attributes)
    {
        return attributes.getDateAttribute(getStartDateAttribute());
    }

    public void setStartDate(AttributeSet attributes, Date date)
    {
        attributes.setDateAttribute(getStartDateAttribute(), date);
    }

    public Date getEndDate(AttributeSet attributes)
    {
        return attributes.getDateAttribute(getEndDateAttribute());
    }

    public void setEndDate(AttributeSet attributes, Date date)
    {
        attributes.setDateAttribute(getEndDateAttribute(), date);
    }

    public Date getPaidThruDate(AttributeSet attributes)
    {
        return attributes.getDateAttribute(getPaidThruDateAttribute());
    }

    public void setPaidThruDate(AttributeSet attributes, Date date)
    {
        attributes.setDateAttribute(getPaidThruDateAttribute(), date);
    }

    public Date getPendingPaidThruDate(AttributeSet attributes)
    {
        return attributes.getDateAttribute(getPendingPaidThruDateAttribute());
    }

    public void setPendingPaidThruDate(AttributeSet attributes, Date date)
    {
        attributes.setDateAttribute(getPendingPaidThruDateAttribute(), date);
    }



    public String getStartDatePath()
    {
        return getStartDateAttribute().getName();
    }

    public String getEndDatePath()
    {
        return getEndDateAttribute().getName();
    }




    public boolean isSelected(AttributeSet attributes)
    {
        AttributeDesc attrDesc = getSelectionAttribute();
        if (attrDesc == null)
            return true;
        else if (attributes == null)
            return false;
        else
            return attributes.getBooleanAttribute(attrDesc);
    }

    public AttributeSchemaModule toAttributeSchemaModule()
    {
        return new AttributeSchemaModule(getSelectionAttribute(), getUserAttributeDescs());
    }

}
