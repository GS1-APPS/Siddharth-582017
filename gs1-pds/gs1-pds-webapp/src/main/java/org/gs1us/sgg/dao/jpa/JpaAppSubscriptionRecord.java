package org.gs1us.sgg.dao.jpa;

import java.util.Date;

import javax.persistence.Basic;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import org.gs1us.sgg.dao.AppSubscriptionRecord;
import org.gs1us.sgg.daoutil.KeyMapper;
import org.gs1us.sgg.daoutil.jpa.LongKeyMapper;
import org.gs1us.sgg.gbservice.api.AttributeSet;

@Entity
@Table(name="app_subscription")
class JpaAppSubscriptionRecord extends AppSubscriptionRecord 
{
    protected static final KeyMapper<Long> KEY_MAPPER = new LongKeyMapper();
    
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private long m_id;

    @Basic
    private String m_gbAccountGln;
    
    @Basic
    private String m_appId;
    
    @Basic
    private String m_subscribedByAgentUsername;
    
    @Basic
    private String m_subscribedByUsername;
    
    @Basic
    private Date m_subscriptionDate;
    
    @Basic
    private JpaAttributeSet m_attributes;
    
    public String getId()
    {
        return KEY_MAPPER.keyToId(m_id);
    }

    public void setId(String id)
    {
        m_id = KEY_MAPPER.idToKey(id);
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

    public JpaAttributeSet getAttributes()
    {
        return m_attributes;
    }

    public void setAttributes(AttributeSet attributes)
    {
        m_attributes = (JpaAttributeSet)attributes;
    }

    


}
