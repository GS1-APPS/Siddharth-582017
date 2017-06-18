package org.gs1us.fileimport;

import java.util.List;

/**
 * A stream of record sources.
 * @author kt
 */
public interface RecordSourceSource<K,V>
{
    /**
     * Advances to the first or next record source, returning true if there is one.
     */
    public boolean next() throws ImportException;
    
    /**
     * Returns the current record source.
     */
    public RecordSource<K,V> getRecordSource();
    
    /**
     * Returns the name of the current record source, zero being the first.
     */
    public String getRecordSourceName();
}
