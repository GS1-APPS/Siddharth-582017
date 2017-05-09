package org.gs1us.sgg.dao.mock;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

class GlnAwareMockDao<T extends DaoElement<T> & HasGln> extends MockDao<T>
{
    public T getByGlnAndFilter(String gln, MockDao.Filter<T> filter)
    {
        for (T elt : getAll())
        {
            if (gln.equals(elt.getGBAccountGln()) && filter.include(elt))
                return elt;
        }
        return null;
    }
    
    public Collection<T> getAllByGlnAndFilter(String gln, MockDao.Filter<T> filter, Comparator<T> orderBy)
    {
        Collection<T> all = getAll();
        List<T> result = new ArrayList<>(all.size());
        for (T elt : all)
        {
            if (gln.equals(elt.getGBAccountGln())
                && (filter == null || filter.include(elt)))
                result.add(elt);
        }
        if (orderBy != null)
            Collections.sort(result, orderBy);
        return result;
    }
}
