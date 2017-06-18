package org.gs1us.sgg.app;

import org.gs1us.sgg.dao.AgentUser;
import org.gs1us.sgg.dao.AppSubscriptionRecord;
import org.gs1us.sgg.gbservice.api.GBAccount;

public interface AppEventHandler
{
    public void handleSubscribe(AgentUser agentUser, String username, GBAccount gbAccount, AppSubscriptionRecord appSubscriptionRecord);
}
