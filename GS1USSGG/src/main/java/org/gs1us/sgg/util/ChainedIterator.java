package org.gs1us.sgg.util;

import java.util.Iterator;

public abstract class ChainedIterator<T> implements Iterator<T>
{
    private Iterator<T> m_currentIterator = null;
    private boolean m_primed = false;
    private boolean m_hasNext = false;
    private T m_next;
    
    public ChainedIterator()
    {
    }
    
    private void prime()
    {
        m_primed = true;
        while (m_currentIterator == null || !m_currentIterator.hasNext())
        {
            m_currentIterator = nextIterator();
            if (m_currentIterator == null)
            {
                m_hasNext = false;
                m_next = null;
                return;
            }
        }
        
        m_next = m_currentIterator.next();
        m_hasNext = true;
    }
    
    @Override
    public boolean hasNext()
    {
        // Defer the initial prime() from the constructor, in case the nextIterator() method needs the subclass constructor to execute
        if (!m_primed)
            prime();
        return m_hasNext;
    }

    @Override
    public T next()
    {
        // Defer the initial prime() from the constructor, in case the nextIterator() method needs the subclass constructor to execute
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
    
    protected abstract Iterator<T> nextIterator();

}
