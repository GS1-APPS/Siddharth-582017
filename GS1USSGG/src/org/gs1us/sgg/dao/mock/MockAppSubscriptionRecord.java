package org.gs1us.sgg.dao.mock;

import java.util.Date;

import org.gs1us.sgg.dao.AppSubscriptionRecord;
import org.gs1us.sgg.gbservice.api.AttributeSet;

class MockAppSubscriptionRecord extends AppSubscriptionRecord implements DaoElement<MockAppSubscriptionRecord>, HasGln
{
    private String m_id;
    private String m_gbAccountGln;
    private String m_appId;
    private String m_subscribedByAgentUsername;
    private String m_subscribedByUsername;
    private Date m_subscriptionDate;
    private AttributeSet m_attributes;
    
    public String getId()
    {
        return m_id;
    }
    public void setId(String id)
    {
        m_id = id;
    }
    public String getGBAccountGln()
    {
        return m_gbAccountGln;
    }
    public void setGBAccountGln(String gbAccountGln)
    {
        m_gbAccountGln = gbAccountGln;
    }
    public String getAppId()
    {
        return m_appId;
    }
    public void setAppId(String appId)
    {
        m_appId = appId;
    }
    @Override
    public MockAppSubscriptionRecord newInstance()
    {
        return new MockAppSubscriptionRecord();
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
    public AttributeSet getAttributes()
    {
        return m_attributes;
    }
    public void setAttributes(AttributeSet attributes)
    {
        m_attributes = attributes;
    }
    @Override
    public MockAppSubscriptionRecord updateFrom(MockAppSubscriptionRecord from)
    {
        setId(from.getId());
        setGBAccountGln(from.getGBAccountGln());
        setAppId(from.getAppId());
        setAttributes(from.getAttributes());
        return this;
    }

    


}
