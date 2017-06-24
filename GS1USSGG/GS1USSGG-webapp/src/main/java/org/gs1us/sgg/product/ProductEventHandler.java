package org.gs1us.sgg.product;

import org.gs1us.sgg.dao.AgentUser;
import org.gs1us.sgg.dao.ProductRecord;
import org.gs1us.sgg.gbservice.api.GBAccount;

public interface ProductEventHandler
{
    void handleCreate(AgentUser agentUser, String username, GBAccount gbAccount, ProductRecord productRecord);
    void handleUpdate(AgentUser agentUser, String username, GBAccount gbAccount, ProductRecord productRecord);
    void handleDelete(AgentUser agentUser, String username, GBAccount gbAccount, ProductRecord productRecord);
}
