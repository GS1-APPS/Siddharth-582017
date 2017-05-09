package org.gs1us.sgg.gbservice.api;

import java.text.ParseException;
import java.util.Date;
import java.util.Map;

import org.gs1us.sgg.util.Util;

public abstract class AttributeSet
{
    public String getAttribute(String attrName)
    {
        Map<String, String> map = getAttributes();
        if (map == null)
            return null;
        else
            return map.get(attrName);
    }
    public final String getAttribute(AttributeDesc desc)
    {
        return getAttribute(desc.getName());
    }
    public void setAttribute(String attrName, String value)
    {
        Map<String, String> map = getAttributes();
        if (value == null)
            map.remove(attrName);
        else
            map.put(attrName, value);
    }
    public final void setAttribute(AttributeDesc desc, String value)
    {
        setAttribute(desc.getName(), value);
    }
    
    public abstract Map<String, String> getAttributes();
    public abstract void setAttributes(Map<String, String> attributes);
    
    public Date getDateAttribute(AttributeDesc desc)
    {
        String s = getAttribute(desc);
        if (s == null)
            return null;
        else
        {
            try
            {
                Date dateAtMidnight = Util.DATE_FORMAT.parse(s);
                // Shift to noon UTC to try to avoid unexpected date boundary behavior
                Date result = new Date(dateAtMidnight.getTime() + 12*3600*1000);
                return result;
            }
            catch (ParseException e)
            {
                return null;
            }
        }
    }
    public void setDateAttribute(AttributeDesc desc, Date date)
    {
        if (date == null)
            setAttribute(desc, null);
        else
            setAttribute(desc, Util.DATE_FORMAT.format(date));
    }
    
    public boolean getBooleanAttribute(AttributeDesc desc)
    {
        String value = getAttribute(desc);
        if (value != null && value.length() > 0 && Character.toLowerCase(value.charAt(0)) == 't')
            return true;
        else
            return false;
    }
    
    public void setBooleanAttribute(AttributeDesc desc, boolean b)
    {
        if (b)
            setAttribute(desc, "true");
        else
            setAttribute(desc, "false");
    }
    
    public Integer getIntAttribute(AttributeDesc desc)
    {
        String s = getAttribute(desc);
        if (s == null)
            return null;
        else
        {
            try
            {
                return Integer.parseInt(s);
            }
            catch (NumberFormatException e)
            {
                return null;
            }
        }
    }
    public void setIntAttribute(AttributeDesc desc, Integer i)
    {
        if (i == null)
            setAttribute(desc, null);
        else
            setAttribute(desc, String.valueOf(i));
    }
    
    public void nullsToOldValues(AttributeSet oldAttributes)
    {
        if (oldAttributes != null)
        {
            for (Map.Entry<String,String> entry : oldAttributes.getAttributes().entrySet())
            {
                String attrName = entry.getKey();
                String oldValue = entry.getValue();
                if (getAttribute(attrName) == null)
                    setAttribute(attrName, oldValue);
            }
        }
        
        for (Map.Entry<String,String> entry : getAttributes().entrySet())
        {
            String newValue = entry.getValue();
            if (newValue != null && newValue.length() == 0)
                entry.setValue(null);
        }
    }
}
