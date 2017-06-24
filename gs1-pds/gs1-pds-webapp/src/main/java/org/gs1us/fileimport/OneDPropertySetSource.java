package org.gs1us.fileimport;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * A property set source defined over a 1D file, where each row includes a key value and a single property name and property value.
 * Adjacent rows will be grouped by key.
 * @author kt
 *
 * @param <KeyType>
 * @param <PropertyDescType>
 * @param <T>
 */
public class OneDPropertySetSource<KeyType,PropertyDescType,T> implements PropertySetSource<KeyType,PropertyDescType,T>
{
    private RowSource<T> m_rowSource;
    private RowKeyFactory<KeyType,T> m_keyFactory;
    private Map<T,PropertyDescType> m_propertyDescMap;
    private int m_hashSize;
    private int m_propertyNameIndex;
    private int m_propertyValueIndex;
    
    
    private List<T> m_nextRow = null;
    private KeyType m_nextKey = null;
    
    private KeyType m_currentKey;
    private Map<PropertyDescType,T> m_currentPropertyMap;
    private Map<PropertyDescType,ImportLocator> m_currentLocatorMap;
    
    public OneDPropertySetSource(RowSource<T> rowSource, RowKeyFactory<KeyType,T> keyFactory, int propertyNameIndex, int propertyValueIndex, Map<T,PropertyDescType> propertyDescMap)
    {
        m_rowSource = rowSource;
        m_keyFactory = keyFactory;
        
        m_propertyNameIndex = propertyNameIndex;
        m_propertyValueIndex = propertyValueIndex;
        
        m_propertyDescMap = propertyDescMap;
        m_hashSize = (propertyDescMap.size() * 4 + 3 ) / 3; // = ceiling(len / 0.75)
    }
    
    @Override
    public boolean next() throws ImportException
    {
        m_currentKey = null;
        m_currentPropertyMap = null;
        m_currentLocatorMap = null;
        
        if (m_nextRow == null)
        {
            do
            {
                if (!m_rowSource.next())
                    return false;
                m_nextRow = m_rowSource.getRowContents();
                m_nextKey = m_keyFactory.createKey(m_nextRow);
            } while (m_nextKey == null);
        }
        
        m_currentKey = m_nextKey;
        m_currentPropertyMap = new HashMap<PropertyDescType,T>(m_hashSize);
        m_currentLocatorMap = new HashMap<PropertyDescType,ImportLocator>(m_hashSize);
        
        do
        {
            if (m_propertyNameIndex < m_nextRow.size())
            {
                T propertyName = m_nextRow.get(m_propertyNameIndex);
                PropertyDescType propertyDesc = m_propertyDescMap.get(propertyName);
                if (propertyDesc != null)
                {
                    T propertyValue = m_propertyValueIndex < m_nextRow.size() ? m_nextRow.get(m_propertyValueIndex) : null;
                    m_currentPropertyMap.put(propertyDesc, propertyValue);
                    m_currentLocatorMap.put(propertyDesc, new RowColImportLocator(m_rowSource.getRowIndex(), m_propertyValueIndex));
                }
            }
            
            do
            {
                if (!m_rowSource.next())
                    return true;
                m_nextRow = m_rowSource.getRowContents();
                m_nextKey = m_keyFactory.createKey(m_nextRow);
            } while (m_nextKey == null);
         } while (m_nextKey.equals(m_currentKey));
        
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
