package org.gs1us.sgg.app.dwcode;

import org.gs1us.sgg.attribute.AttributeDescImpl;
import org.gs1us.sgg.dao.AgentUser;
import org.gs1us.sgg.gbservice.api.AttributeDesc;
import org.gs1us.sgg.gbservice.api.AttributeType;

public class DWCodeIntegrationHandler
{
    protected static final AttributeDesc ACCOUNT_ID_ATTR_DESC = 
            new AttributeDescImpl("dmAccountId", "Digimarc Account ID", null, null, AttributeType.INTEGER, true, "n/a", null);
    protected static final AttributeDesc PROJECT_ID_ATTR_DESC = 
            new AttributeDescImpl("dmProjectId", "Digimarc DWCode Inbound Project ID", null, null, AttributeType.INTEGER, true, "n/a", null);
    

    private static final Object DIGIMARC_AGENT_USERNAME = "digimarc";
    private DigimarcService m_digimarcService;



    public DWCodeIntegrationHandler(DigimarcService digimarcService)
    {
        super();
        m_digimarcService = digimarcService;
    }



    public DigimarcService getDigimarcService()
    {
        return m_digimarcService;
    }



    protected boolean suppressDigimarcOperations(AgentUser agentUser)
    {
        return m_digimarcService == null || !m_digimarcService.isEnabled() || (agentUser != null && DIGIMARC_AGENT_USERNAME.equals(agentUser.getUsername()));
    }

}