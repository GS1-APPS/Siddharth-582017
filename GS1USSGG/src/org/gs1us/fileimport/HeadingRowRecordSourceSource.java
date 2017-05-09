package org.gs1us.fileimport;

public class HeadingRowRecordSourceSource<T> implements RecordSourceSource<T,T>
{
    private RowSourceSource<T> m_rowSourceSource;
    private RecordSource<T,T> m_recordSource;
    private String m_recordSourceName;
    
    public HeadingRowRecordSourceSource(RowSourceSource<T> rowSourceSource)
    {
        super();
        m_rowSourceSource = rowSourceSource;
    }

    @Override
    public boolean next() throws ImportException
    {
        if (m_rowSourceSource.next())
        {
            m_recordSource = new HeadingRowRecordSource<T>(m_rowSourceSource.getRowSource());
            m_recordSourceName = m_rowSourceSource.getRowSourceName();
            return true;
        }
        else
            return false;
    }

    @Override
    public RecordSource<T,T> getRecordSource()
    {
        return m_recordSource;
    }

    @Override
    public String getRecordSourceName()
    {
        return m_recordSourceName;
    }

}
