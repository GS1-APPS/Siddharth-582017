package org.gs1us.fileimport;

public class SingletonRowSourceSource<T> implements RowSourceSource<T>
{
    private int m_state = 0;
    private RowSource<T> m_rowSource;
    private String m_name;
    
    public SingletonRowSourceSource(RowSource<T> rowSource, String name)
    {
        m_rowSource = rowSource;
        m_name = name;
    }

    @Override
    public boolean next() throws ImportException
    {
        switch (m_state)
        {
        case 0:
            m_state = 1;
            return true;
            
        case 1:
            m_state = 2;
            return false;
            
        default:
            return false;
        }
    }

    @Override
    public RowSource<T> getRowSource()
    {
        if (m_state == 1)
            return m_rowSource;
        else
            return null;
    }

    @Override
    public String getRowSourceName()
    {
        if (m_state == 2)
            return m_name;
        else
            return null;

    }
    
    
}
