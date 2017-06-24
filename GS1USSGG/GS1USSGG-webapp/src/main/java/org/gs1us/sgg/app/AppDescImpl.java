package org.gs1us.sgg.app;

import org.gs1us.sgg.gbservice.api.Amount;
import org.gs1us.sgg.gbservice.api.AppDesc;
import org.gs1us.sgg.gbservice.api.Product;
import org.gs1us.sgg.gbservice.api.AttributeDesc;

class AppDescImpl extends AppDesc
{
    private String m_name;
    private String m_title;
    private String m_iconName;
    private String m_description;
    private ModuleDescImpl m_accountModuleDesc;
    private ModuleDescImpl m_subscriptionModuleDesc;
    private ModuleDescImpl m_productModuleDesc;


    public AppDescImpl(String name, String title, String iconName, String description, 
                       ModuleDescImpl accountModuleDesc,
                       ModuleDescImpl subscriptionModuleDesc,
                       ModuleDescImpl productModuleDesc)
    {
        super();
        m_name = name;
        m_title = title;
        m_iconName = iconName;
        m_description = description;
        m_accountModuleDesc = accountModuleDesc;
        m_subscriptionModuleDesc = subscriptionModuleDesc;
        m_productModuleDesc = productModuleDesc;
    }


    public String getName()
    {
        return m_name;
    }


    public String getTitle()
    {
        return m_title;
    }


    public String getIconName()
    {
        return m_iconName;
    }


    public String getDescription()
    {
        return m_description;
    }




    public ModuleDescImpl getAccountModuleDesc()
    {
        return m_accountModuleDesc;
    }


    public ModuleDescImpl getSubscriptionModuleDesc()
    {
        return m_subscriptionModuleDesc;
    }


    public ModuleDescImpl getProductModuleDesc()
    {
        return m_productModuleDesc;
    }

    

}
