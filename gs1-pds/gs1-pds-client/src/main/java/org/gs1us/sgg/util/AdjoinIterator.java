package org.gs1us.sgg.util;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

public class AdjoinIterator<T> extends MultiIterator<T>
{
    
    public AdjoinIterator(T singleton, Iterator<T> remainder)
    {
        super(adjoin(singleton, remainder));
    }

    private static <T> Iterator<Iterator<T>> adjoin(T singleton, Iterator<T> remainder)
    {
        List<Iterator<T>> l = new ArrayList<Iterator<T>>(2);
        l.add(Collections.singletonList(singleton).iterator());
        l.add(remainder);
        return l.iterator();
    }
    

}
