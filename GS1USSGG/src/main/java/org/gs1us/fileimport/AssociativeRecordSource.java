package org.gs1us.fileimport;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * A record source where each record is composed by delivering name/value pairs from the underlying source
 * (as opposed to, say, delivering values positionally).
 * @author kt
 *
 * @param <K>
 * @param <V>
 */
public abstract class AssociativeRecordSource<K, V> implements RecordSource<K,V>
{
    private Map<K,Integer> m_fieldIndexMap;

    /**
     * Advances to the first or next record, returning true if there is one.
     * Concrete implementations should build up the record by first calling <code>startRecord</code> then
     * calling <code>setField</code> for each field to be contributed to that record.
     */
    public abstract boolean next() throws ImportException;

    private List<K> m_recordFieldNames;
    private List<V> m_recordContents;
    private int m_recordIndex = 0;

    public AssociativeRecordSource()
    {
        super();
    }

    /**
     * Called by concrete subclasses' <code>next</code> method to begin the process of building up a record.
     * @param sizeHint  A hint as to the likely number of fields. Zero can be specified if no hint is available.
     */
    protected void startRecord(int sizeHint)
    {
        m_recordIndex++;
        if (m_recordFieldNames == null)
        {
            m_fieldIndexMap = new HashMap<>(Math.min(16, sizeHint * 4 / 3));
            m_recordFieldNames = new ArrayList<>(Math.min(16, sizeHint));
            m_recordContents = new ArrayList<>(Math.min(16, sizeHint));
        }
        for (int i = 0; i < m_recordFieldNames.size(); i++)
        {
            m_recordFieldNames.set(i, null);
            m_recordContents.set(i, null);
        }
    }

    /**
     * Called by concrete subclasses' <code>next</code> method to supply the value of one field.
     * @param fieldName
     * @param value
     */
    protected void setField(K fieldName, V value)
    {
        Integer index = m_fieldIndexMap.get(fieldName);
        if (index == null)
        {
            index = new Integer(m_recordFieldNames.size());
            m_recordFieldNames.add(fieldName);
            m_recordContents.add(value);
            m_fieldIndexMap.put(fieldName, index);
        }
        else
        {
            m_recordFieldNames.set(index, fieldName);
            m_recordContents.set(index, value);
        }
    }

    @Override
    public List<K> getRecordFieldNames()
    {
        return m_recordFieldNames;
    }

    @Override
    public List<V> getRecordContents()
    {
        return m_recordContents;
    }

    @Override
    public int getRecordIndex()
    {
        return m_recordIndex;
    }

}