package org.gs1us.fileimport;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * A property set source defined over a 2D record set, where each field is either a key component or a property and each record is a
 * property set.
 * @author kt
 *
 * @param <KeyType>
 * @param <PropertyDescType>
 * @param <T>
 */
public class RecordSourcePropertySetSource<KeyType,PropertyDescType,T> implements PropertySetSource<KeyType,PropertyDescType,T>
{
    private RecordSource<?,T> m_recordSource;
    private RowKeyFactory<KeyType,T> m_keyFactory;
    private List<PropertyDescType> m_propertyDescs;
    private Map<PropertyDescType,T> m_constantPropertyValueMap;
    private int m_hashSize;
    
    private KeyType m_currentKey;
    private Map<PropertyDescType,T> m_currentPropertyMap;
    private Map<PropertyDescType,ImportLocator> m_currentLocatorMap;
    
    public RecordSourcePropertySetSource(RecordSource<?,T> recordSource, RowKeyFactory<KeyType,T> keyFactory, List<PropertyDescType> propertyDescs, Map<PropertyDescType,T> constantPropertyValueMap)
    {
        m_recordSource = recordSource;
        m_keyFactory = keyFactory;
        m_propertyDescs = propertyDescs;
        m_constantPropertyValueMap = constantPropertyValueMap;
        m_hashSize = (propertyDescs.size() * 4 + 3 ) / 3; // = ceiling(len / 0.75)
    }
    
    @Override
    public boolean next() throws ImportException
    {
        m_currentKey = null;
        m_currentPropertyMap = null;
        m_currentLocatorMap = null;
        
        if (!m_recordSource.next())
            return false;
        
        List<T> currentRecord = m_recordSource.getRecordContents();
        int currentRecordNum = m_recordSource.getRecordIndex();
        
        m_currentKey = m_keyFactory.createKey(currentRecord);
        
        int colsToProcess = Math.min(m_propertyDescs.size(), currentRecord.size());
        m_currentPropertyMap = new HashMap<PropertyDescType,T>(m_hashSize);
        m_currentLocatorMap = new HashMap<PropertyDescType,ImportLocator>(m_hashSize);
        for (int i = 0; i < colsToProcess; i++)
        {
            PropertyDescType propertyDesc = m_propertyDescs.get(i);
            if (propertyDesc != null)
            {
                m_currentPropertyMap.put(propertyDesc, currentRecord.get(i));
                m_currentLocatorMap.put(propertyDesc, new RowColImportLocator(currentRecordNum, i));
            }
        }
        if (m_constantPropertyValueMap != null)
        {
            for (Map.Entry<PropertyDescType,T> entry : m_constantPropertyValueMap.entrySet())
            {
                m_currentPropertyMap.put(entry.getKey(), entry.getValue());
                m_currentLocatorMap.put(entry.getKey(), new RowColImportLocator(currentRecordNum));
            }
        }
        
        return true;
    }

    @Override
    public KeyType getKey()
    {
        return m_currentKey;
    }

    @Override
    public Map<PropertyDescType, T> getPropertyMap()
    {
        return m_currentPropertyMap;
    }

    @Override
    public Map<PropertyDescType, ImportLocator> getLocatorMap()
    {
        return m_currentLocatorMap;
    }

}
