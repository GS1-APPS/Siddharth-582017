package org.gs1us.sgg.gbservice.api;

public enum Action
{
    CREATE,
    UPDATE,
    RENEW,
    DELETE,
    VALIDATE
    ;
    
    public static int mask(Action... actions)
    {
        int result = 0;
        for (Action action : actions)
        {
            result |= action.toMask();
        }
        return result;
    }
    
    public static final int ALL_ACTIONS_MASK = -1;
    
    public int toMask()
    {
        return (1 << ordinal());
    }
    
    public boolean matches(int mask)
    {
        return (mask & toMask()) != 0;
    }
}
