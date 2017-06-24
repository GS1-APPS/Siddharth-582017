package org.gs1us.sgg.dao;

import org.gs1us.sgg.gbservice.api.AttributeSet;
import org.gs1us.sgg.gbservice.api.GBAccount;

public abstract class GBAccountRecord implements GBAccount
{
    public abstract String getId();
    public abstract void setId(String id);
    
    
    @Override
    public abstract String getGln();
    public abstract void setGln(String gln);
    
    @Override
    public abstract String getName();
    public abstract void setName(String name);
    
    @Override
    public abstract String[] getGcps();
    public abstract void setGcps(String[] gcps);
    
    @Override
    public abstract AttributeSet getAttributes();
    public abstract void setAttributes(AttributeSet attributes);
    
    public void updateFrom(GBAccount other)
    {
        setGln(other.getGln());
        setName(other.getName());
        setGcps(other.getGcps());
        setAttributes(other.getAttributes());
    }
}
