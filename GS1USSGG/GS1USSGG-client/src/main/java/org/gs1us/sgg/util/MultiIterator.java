package org.gs1us.sgg.util;

import java.util.Iterator;

public class MultiIterator<T> extends ChainedIterator<T>
{
    private Iterator<Iterator<T>> m_iteratorIterator;
    
    
    public MultiIterator(Iterator<Iterator<T>> iteratorIterator)
    {
        super();
        m_iteratorIterator = iteratorIterator;
    }


    @Override
    protected Iterator<T> nextIterator()
    {
        if (m_iteratorIterator.hasNext())
            return m_iteratorIterator.next();
        else
            return null;
    }
    
}
