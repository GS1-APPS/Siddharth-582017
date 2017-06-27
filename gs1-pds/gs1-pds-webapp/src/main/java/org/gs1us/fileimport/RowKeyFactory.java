package org.gs1us.fileimport;

import java.util.List;

/**
 * Interface to extract a key from a row
 * @author kt
 *
 * @param <KeyType>
 * @param <T>
 */
public interface RowKeyFactory<KeyType,T>
{
    public KeyType createKey(List<T> values);
}
