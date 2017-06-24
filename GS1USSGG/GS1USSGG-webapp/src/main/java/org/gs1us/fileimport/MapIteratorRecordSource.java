package org.gs1us.fileimport;

import java.util.Iterator;
import java.util.Map;

/**
 * A record source where each record's contents is specified by a map, and the maps are provided
 * by means of an iterator.
 * @author kt
 *
 * @param <K> Key type
 * @param <V> Value type
 */
public class MapIteratorRecordSource<K,V> extends AssociativeRecordSource<K, V> 
{
    private Iterator<Map<K,V>> m_mapIterator;
    
    public MapIteratorRecordSource(Iterator<Map<K, V>> mapIterator)
    {
        super();
        m_mapIterator = mapIterator;
    }

    @Override
    public boolean next() throws ImportException
    {
        if (m_mapIterator.hasNext())
        {
            Map<K,V> recordMap = m_mapIterator.next();
            int sizeHint = recordMap.size();
            startRecord(sizeHint);
            for (Map.Entry<K,V> entry : recordMap.entrySet())
            {
                K fieldName = entry.getKey();
                V value = entry.getValue();
                setField(fieldName, value);
            }
            return true;
        }
        else
            return false;
    }

}
