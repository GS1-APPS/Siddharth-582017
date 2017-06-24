package org.gs1us.fileimport;

import java.util.ArrayList;
import java.util.List;

/**
 * A record source derived from a row source, where the first row is considered to contain the field names.
 * @author kt
 *
 */
public class HeadingRowRecordSource<T> implements RecordSource<T,T>
{
    private RowSource<T> m_rowSource;
    private List<T> m_fieldNames = null;
    private List<T> m_recordContents = null;
    
    public HeadingRowRecordSource(RowSource<T> rowSource) throws ImportException
    {
        m_rowSource = rowSource;
        initializeFieldNames();
    }
    
    private void initializeFieldNames() throws ImportException
    {
        if (m_rowSource.next())
        {
            m_fieldNames = new ArrayList<>(m_rowSource.getRowContents());
        }
    }
    
    public boolean hasHeadings()
    {
        return m_fieldNames != null;
    }

    @Override
    public boolean next() throws ImportException
    {
        if (m_rowSource.next())
        {
            int size = m_rowSource.getRowContents().size();

            if (m_recordContents == null)
                m_recordContents = new ArrayList<>(m_rowSource.getRowContents());
            else
            {
                // Enlarge record contents if need be
                while (m_recordContents.size() < size)
                    m_recordContents.add(null);
                
                // Copy the content
                for (int i = 0; i < size; i++)
                    m_recordContents.set(i, m_rowSource.getRowContents().get(i));
            }
            // Enlarge field names vector if needed (using null, since we don't have headings for any extra cols)
            while (m_fieldNames.size() < size)
                m_fieldNames.add(null);
            
            return true;
        }
        else
            return false;
    }

    @Override
    public List<T> getRecordFieldNames()
    {
        return m_fieldNames;
    }

    @Override
    public List<T> getRecordContents()
    {
        return m_recordContents;
    }

    @Override
    public int getRecordIndex()
    {
        return m_rowSource.getRowIndex();
    }

}
