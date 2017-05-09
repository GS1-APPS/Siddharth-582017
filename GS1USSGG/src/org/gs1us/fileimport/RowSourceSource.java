package org.gs1us.fileimport;

import java.util.List;

/**
 * A stream of row sources.
 * @author kt
 *
 * @param <T>
 */
public interface RowSourceSource<T>
{
    /**
     * Advances to the first or next row source, returning true if there is one.
     */
    public boolean next() throws ImportException;
    
    /**
     * Returns the current row source.
     */
    public RowSource<T> getRowSource();
    
    /**
     * Returns the name of the current row source, zero being the first.
     */
    public String getRowSourceName();
}
