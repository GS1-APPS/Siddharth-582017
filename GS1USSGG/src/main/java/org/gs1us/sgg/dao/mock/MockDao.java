package org.gs1us.sgg.dao.mock;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.HashMap;

class MockDao<T extends DaoElement<T>>
{
    public interface Filter<T>
    {
        boolean include(T elt);
    }

    private int m_nextId = 1;
    private Map<String, T> m_byId = new HashMap<>();
    
    public T getById(String id)
    {
        T elt = m_byId.get(id);
        if (elt == null)
            return null;
        else
            return elt.newInstance().updateFrom(elt);
    }
    
    public T getByFilter(MockDao.Filter<T> filter)
    {
        for (T elt : getAll())
        {
            if (filter.include(elt))
                return elt;
        }
        return null;
    }
    
    public Collection<T> getAllByFilter(MockDao.Filter<T> filter, Comparator<T> orderBy)
    {
        Collection<T> elts = m_byId.values();
        List<T> newElts = new ArrayList<>(elts.size());
        for (T elt : elts)
        {
            if (filter == null || filter.include(elt))
                newElts.add(elt.newInstance().updateFrom(elt));
        }
        if (orderBy != null)
            Collections.sort(newElts, orderBy);
        return newElts;
    }

    
    public Collection<T> getAll()
    {
        Collection<T> elts = m_byId.values();
        Collection<T> newElts = new ArrayList<>(elts.size());
        for (T elt : elts)
            newElts.add(elt.newInstance().updateFrom(elt));
        return newElts;
    }
    
    public void update(T elt)
    {
        T newElt;
        if (elt.getId() == null)
        {
            assignId(elt);
            newElt = elt.newInstance();
            m_byId.put(elt.getId(), newElt);
        }
        else
            newElt = m_byId.get(elt.getId());
        newElt.updateFrom(elt);
    }
    
    private synchronized void assignId(T elt)
    {
        elt.setId(String.valueOf(m_nextId));
        m_nextId++;
    }

    public void delete(T elt)
    {
        m_byId.remove(elt.getId());
    }
}
