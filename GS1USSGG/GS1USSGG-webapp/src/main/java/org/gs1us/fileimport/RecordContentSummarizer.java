package org.gs1us.fileimport;

import java.util.ArrayList;
import java.util.List;

public class RecordContentSummarizer<K,V>
{
    private int m_recordCount;
    private int m_blankRecordCount;
    private List<FieldContentSummary<K>> m_fieldContentSummaries = new ArrayList<>();
    
    public RecordContentSummarizer(RecordSource<K,V> recordSource) throws ImportException
    {
        while (recordSource.next())
        {
            int fieldCount = recordSource.getRecordContents().size();
            boolean isAllBlank = true;
            for (int i = 0; i < fieldCount; i++)
            {
                FieldContentSummary<K> summary;
                if (m_fieldContentSummaries.size() <= i)
                {
                    // Initialize non-empty count from record count to account for prior rows that didn't have this field at all
                    summary = new FieldContentSummary<K>(recordSource.getRecordFieldNames().get(i), m_recordCount, 0);
                    m_fieldContentSummaries.add(summary);
                }
                else
                    summary = m_fieldContentSummaries.get(i);

                if (isEmpty(recordSource.getRecordContents().get(i)))
                    summary.incrementEmpty();
                else
                {
                    summary.incrementNonEmpty();
                    isAllBlank = false;
                }

            }
            m_recordCount++; 
            if (isAllBlank)
                m_blankRecordCount++;
        }
    }
    
    
    
    public int getRecordCount()
    {
        return m_recordCount;
    }



    public int getBlankRecordCount()
    {
        return m_blankRecordCount;
    }



    public List<FieldContentSummary<K>> getFieldContentSummaries()
    {
        return m_fieldContentSummaries;
    }
    
    public void truncateRightmostEmptyColumns()
    {
        int initialColCount = m_fieldContentSummaries.size();
        for (int col = initialColCount-1; col >= 0; col--)
        {
            FieldContentSummary<K> summary = m_fieldContentSummaries.get(col);
            if (summary.getFieldName() == null && summary.getEmptyCount() == m_recordCount)
                m_fieldContentSummaries.remove(col);
            else
                return;
        }
        return;
    }



    protected boolean isEmpty(V value)
    {
        return value == null;
    }
    
    public static class FieldContentSummary<K>
    {
        private K m_fieldName;
        private int m_emptyCount;
        private int m_nonEmptyCount;
        
        private FieldContentSummary(K fieldName, int emptyCount, int nonEmptyCount)
        {
            super();
            m_fieldName = fieldName;
            m_emptyCount = emptyCount;
            m_nonEmptyCount = nonEmptyCount;
        }

        public K getFieldName()
        {
            return m_fieldName;
        }

        public int getEmptyCount()
        {
            return m_emptyCount;
        }

        public int getNonEmptyCount()
        {
            return m_nonEmptyCount;
        }

        private void incrementEmpty()
        {
            m_emptyCount++;
        }
        
        private void incrementNonEmpty()
        {
            m_nonEmptyCount++;
        }
        
        
    }
}
