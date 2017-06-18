package org.gs1us.sgg.gbservice.json;

import java.util.Date;

import org.gs1us.sgg.gbservice.api.AppDesc;
import org.gs1us.sgg.gbservice.api.AppSubscription;

public class InboundAppSubscription implements AppSubscription
{
    private String m_appName;
    private InboundAppDesc m_appDesc;
    private String m_subscribedByAgentUsername;
    private String m_subscribedByUsername;
    private Date m_subscriptionDate;
    private Object m_appArgs;
    public String getAppName()
    {
        return m_appName;
    }
    public void setAppName(String appName)
    {
        m_appName = appName;
    }
    public AppDesc getAppDesc()
    {
        return m_appDesc;
    }
    public void setAppDesc(InboundAppDesc appDesc)
    {
        m_appDesc = appDesc;
    }
    
    public String getSubscribedByAgentUsername()
    {
        return m_subscribedByAgentUsername;
    }
    public void setSubscribedByAgentUsername(String subscribedByAgentUsername)
    {
        m_subscribedByAgentUsername = subscribedByAgentUsername;
    }
    public String getSubscribedByUsername()
    {
        return m_subscribedByUsername;
    }
    public void setSubscribedByUsername(String subscribedByUsername)
    {
        m_subscribedByUsername = subscribedByUsername;
    }
    public Date getSubscriptionDate()
    {
        return m_subscriptionDate;
    }
    public void setSubscriptionDate(Date subscriptionDate)
    {
        m_subscriptionDate = subscriptionDate;
    }
    public Object getAppArgs()
    {
        return m_appArgs;
    }
    public void setAppArgs(Object appArgs)
    {
        m_appArgs = appArgs;
    }

    
}

