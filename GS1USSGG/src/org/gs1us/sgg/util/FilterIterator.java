package org.gs1us.sgg.util;

import java.util.Iterator;

public abstract class FilterIterator<T> implements Iterator<T>
{
    private Iterator<T> m_underlying;
    private boolean m_primed = false;
    private boolean m_hasNext;
    private T m_next;

    public FilterIterator(Iterator<T> underlying)
    {
        super();
        m_underlying = underlying;
    }
    
    private void prime()
    {
        m_primed = true;
        while (m_underlying.hasNext())
        {
            m_next = m_underlying.next();
            if (isIncluded(m_next))
            {
                m_hasNext = true;
                return;
            }
        }
        m_next = null;
        m_hasNext = false;
    }

    @Override
    public boolean hasNext()
    {
        // Defer the initial prime() from the constructor, in case the filter condition needs the subclass constructor to execute
        if (!m_primed)
            prime();
        return m_hasNext;
    }

    @Override
    public T next()
    {
        // Defer the initial prime() from the constructor, in case the filter condition needs the subclass constructor to execute
        if (!m_primed)
            prime();
        
        if (m_hasNext)
        {
            T result = m_next;
            prime();
            return result;
        }
        else
            throw new IllegalStateException();
    }

    @Override
    public void remove()
    {
        throw new UnsupportedOperationException();
    }
    
    protected abstract boolean isIncluded(T value);
}
