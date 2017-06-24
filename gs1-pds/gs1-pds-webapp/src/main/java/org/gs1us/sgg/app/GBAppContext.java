package org.gs1us.sgg.app;

import java.util.Collection;

import org.gs1us.sgg.dao.AgentUser;
import org.gs1us.sgg.gbservice.api.AppDesc.Scope;
import org.gs1us.sgg.gbservice.api.AppSubscription;
import org.gs1us.sgg.gbservice.api.AttributeSchema;
import org.gs1us.sgg.gbservice.api.AttributeSchemaModule;
import org.gs1us.sgg.gbservice.api.GBAccount;
import org.gs1us.sgg.gbservice.api.ModuleDesc;

public class GBAppContext
{
    private AgentUser m_agentUser;
    private String m_username;
    private GBAccount m_gbAccount;
    private Collection<? extends AppSubscription> m_subs;
    
    private AttributeSchema[] m_attributeSchemas;
    
    
    public GBAppContext(AgentUser agentUser, String username, GBAccount gbAccount, Collection<? extends AppSubscription> subs)
    {
        super();
        m_agentUser = agentUser;
        m_username = username;
        m_gbAccount = gbAccount;
        m_subs = subs;
        
        m_attributeSchemas = new AttributeSchema[Scope.values().length];
        for (Scope scope : Scope.values())
            m_attributeSchemas[scope.ordinal()] = attributeSchemaForSubs(scope, subs);
    }
    
    private AttributeSchema attributeSchemaForSubs(Scope scope, Collection<? extends AppSubscription> subs)
    {
        AttributeSchema result = new AttributeSchema();
        for (AppSubscription sub : subs)
        {
            ModuleDesc moduleDesc = sub.getAppDesc().getModuleDesc(scope);
            if (moduleDesc != null)
                result.addModule(moduleDesc.toAttributeSchemaModule());
        }
        return result;
    }


    public AgentUser getAgentUser()
    {
        return m_agentUser;
    }
    public String getUsername()
    {
        return m_username;
    }
    public GBAccount getGbAccount()
    {
        return m_gbAccount;
    }
    public Collection<? extends AppSubscription> getSubs()
    {
        return m_subs;
    }
    
    public AttributeSchema getAttributeSchema(Scope scope)
    {
        return m_attributeSchemas[scope.ordinal()];
    }

    public AttributeSchema getAccountAttributeSchema()
    {
        return getAttributeSchema(Scope.ACCOUNT);
    }

    public AttributeSchema getSubscriptionAttributeSchema()
    {
        return getAttributeSchema(Scope.SUBSCRIPTION);
    }

    public AttributeSchema getProductAttributeSchema()
    {
        return getAttributeSchema(Scope.PRODUCT);
    }

    
}
