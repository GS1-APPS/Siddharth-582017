package org.gs1us.fileimport;

import java.util.List;

/**
 * A stream of records, each record an ordered list of named fields, each field having a value of some type
 *
 * @param <K> Key type
 * @param <V> Value type
 */
public interface RecordSource<K,V>
{
    /**
     * Advances to the first or next record, returning true if there is one.
     */
    public boolean next() throws ImportException;
    
    /**
     * Returns the names for the fields of the current record. Implementations may return the same object
     * for each record, so if the clients wants to keep the list for a row it must make a copy.
     */
    public List<K> getRecordFieldNames();
    
    /**
     * Returns the contents of the current record. Implementations may return the same object
     * for each record, so if the clients wants to keep the list for a row it must make a copy.
     */
    public List<V> getRecordContents();
    
    /**
     * Returns the index of the current record, zero being the first.
     */
    public int getRecordIndex();
 
}
