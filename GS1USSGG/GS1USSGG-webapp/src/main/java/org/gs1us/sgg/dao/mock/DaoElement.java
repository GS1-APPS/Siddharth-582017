package org.gs1us.sgg.dao.mock;

interface DaoElement<T extends DaoElement<T>>
{
    public String getId();
    public void setId(String id);
    
    public T newInstance();
    public T updateFrom(T from);
}
