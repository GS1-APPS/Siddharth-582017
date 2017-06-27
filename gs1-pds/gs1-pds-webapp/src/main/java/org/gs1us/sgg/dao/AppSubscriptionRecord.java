package org.gs1us.sgg.dao;

import java.util.Date;

import org.gs1us.sgg.gbservice.api.AttributeSet;

public abstract class AppSubscriptionRecord
{
    public abstract String getId();
    public abstract void setId(String id);
    
    public abstract String getGBAccountGln();
    public abstract void setGBAccountGln(String gln);
    
    public abstract String getAppId();
    public abstract void setAppId(String appId);
    
    public abstract String getSubscribedByAgentUsername();
    public abstract void setSubscribedByAgentUsername(String subscribedByAgentUsername);
    
    public abstract String getSubscribedByUsername();
    public abstract void setSubscribedByUsername(String subscribedByAgentUsername);
    
    public abstract Date getSubscriptionDate();
    public abstract void setSubscriptionDate(Date subscriptionDate);
    
    public abstract AttributeSet getAttributes();
    public abstract void setAttributes(AttributeSet attributes);
}
