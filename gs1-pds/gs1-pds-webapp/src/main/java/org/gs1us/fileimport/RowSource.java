package org.gs1us.fileimport;

import java.util.List;

/**
 * A stream of rows, each row an array of elements of some type.
 *
 */
public interface RowSource<T>
{
    /**
     * Advances to the first or next row, returning true if there is one.
     */
    public boolean next() throws ImportException;
    
    /**
     * Returns the contents of the current row. Implementations may return the same object
     * for each row, so if the clients wants to keep the list for a row it must make a copy.
     */
    public List<T> getRowContents();
    
    /**
     * Returns the index of the current row, zero being the first.
     */
    public int getRowIndex();
 
}
