package org.gs1us.sgg.gbservice.api;

public abstract class AppDesc
{
    public static enum Scope
    {
        ACCOUNT,
        SUBSCRIPTION,
        PRODUCT
    }
    public abstract String getName();
    public abstract String getTitle();
    public abstract String getIconName();
    public abstract String getDescription();
    public abstract ModuleDesc getAccountModuleDesc();
    public abstract ModuleDesc getSubscriptionModuleDesc();
    public abstract ModuleDesc getProductModuleDesc();
    public ModuleDesc getModuleDesc(AppDesc.Scope scope)
    {
        switch (scope)
        {
        case ACCOUNT:
            return getAccountModuleDesc();
        case SUBSCRIPTION:
            return getSubscriptionModuleDesc();
        case PRODUCT:
            return getProductModuleDesc();
        default:
            return null;   
        }
    }
    public boolean isSelected(HasAttributes object)
    {
        if (object == null)
            return false;
        else
            return getProductModuleDesc().isSelected(object.getAttributes());

    }

}
