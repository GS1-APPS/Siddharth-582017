package org.gs1us.sgg.gbservice.api;

import java.util.Date;

/**
 * A subscription to a specific Application by a Global Broker account
 * @author kt
 *
 */
public interface AppSubscription
{
    public String getAppName();
    public AppDesc getAppDesc();
    public String getSubscribedByAgentUsername();
    public String getSubscribedByUsername();
    public Date getSubscriptionDate();
    public Object getAppArgs();
}
