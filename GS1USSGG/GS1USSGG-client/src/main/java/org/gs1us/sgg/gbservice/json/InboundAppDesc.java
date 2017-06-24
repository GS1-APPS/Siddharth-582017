package org.gs1us.sgg.gbservice.json;

import java.util.List;

import org.gs1us.sgg.gbservice.api.AppDesc;
import org.gs1us.sgg.gbservice.api.AttributeDesc;

public class InboundAppDesc extends AppDesc
{
    private String m_name;
    private String m_title;
    private String m_iconName;
    private String m_description;
    private InboundModuleDesc m_accountModuleDesc;
    private InboundModuleDesc m_subscriptionModuleDesc;
    private InboundModuleDesc m_productModuleDesc;
    
    public String getName()
    {
        return m_name;
    }
    public void setName(String name)
    {
        m_name = name;
    }
    public String getTitle()
    {
        return m_title;
    }
    public void setTitle(String title)
    {
        m_title = title;
    }
    public String getIconName()
    {
        return m_iconName;
    }
    public void setIconName(String iconName)
    {
        m_iconName = iconName;
    }
    public String getDescription()
    {
        return m_description;
    }
    public void setDescription(String description)
    {
        m_description = description;
    }
    
    public InboundModuleDesc getAccountModuleDesc()
    {
        return m_accountModuleDesc;
    }
    public void setAccountModuleDesc(InboundModuleDesc accountModuleDesc)
    {
        m_accountModuleDesc = accountModuleDesc;
    }
    public InboundModuleDesc getSubscriptionModuleDesc()
    {
        return m_subscriptionModuleDesc;
    }
    public void setSubscriptionModuleDesc(InboundModuleDesc subscriptionModuleDesc)
    {
        m_subscriptionModuleDesc = subscriptionModuleDesc;
    }
    public InboundModuleDesc getProductModuleDesc()
    {
        return m_productModuleDesc;
    }
    public void setProductModuleDesc(InboundModuleDesc productModuleDesc)
    {
        m_productModuleDesc = productModuleDesc;
    }

}
