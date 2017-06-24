package org.gs1us.sgg.gbservice.impl;

import java.util.Date;

import org.gs1us.sgg.gbservice.api.AppDesc;
import org.gs1us.sgg.gbservice.api.AppSubscription;

class AppSubscriptionImpl implements AppSubscription
{
    private String m_appName;
    private String m_subscribedByAgentUsername;
    private String m_subscribedByUsername;
    private Date m_subscriptionDate;
    private Object m_appArgs;
    public AppSubscriptionImpl(String appName, Object appArgs)
    {
        super();
        m_appName = appName;
        m_appArgs = appArgs;
    }
    public String getAppName()
    {
        return m_appName;
    }
    public AppDesc getAppDesc()
    {
        return null;
    }
    public Object getAppArgs()
    {
        return m_appArgs;
    }
    public String getSubscribedByAgentUsername()
    {
        return m_subscribedByAgentUsername;
    }
    public String getSubscribedByUsername()
    {
        return m_subscribedByUsername;
    }
    public Date getSubscriptionDate()
    {
        return m_subscriptionDate;
    }
    


}
