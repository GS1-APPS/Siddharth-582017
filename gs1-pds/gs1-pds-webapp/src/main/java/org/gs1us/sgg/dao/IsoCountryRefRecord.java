package org.gs1us.sgg.dao;

import java.util.Date;

import org.gs1us.sgg.gbservice.api.IsoCountryRef;

public abstract class IsoCountryRefRecord implements IsoCountryRef 
{	
    public abstract String getId();
    public abstract void setId(String id);
    
    public abstract String getCountryName();
    public abstract void setCountryName(String countryName);

    public abstract String getCountryCodeTxt2();
    public abstract void setCountryCodeTxt2(String countryCodeTxt2);

    public abstract String getCountryCodeTxt3();
    public abstract void setCountryCodeTxt3(String countryCodeTxt3);

    public abstract String getCountryCodeNum();
    public abstract void setCountryCodeNum(String countryCodeNum);
    
    public abstract String getEnabled();
    public abstract void setEnabled(String enabled);

    public abstract Date getModifiedDate();
    public abstract void setModifiedDate(Date date);
}