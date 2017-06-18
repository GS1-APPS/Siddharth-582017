package org.gs1us.fileimport;

public class RowColImportLocator implements ImportLocator
{
    public static final int UNSPECIFIED = -1;
    private int m_rowIndex;
    private int m_colIndex;
    public RowColImportLocator(int rowIndex)
    {
        this(rowIndex, UNSPECIFIED);
    }
    public RowColImportLocator(int rowIndex, int colIndex)
    {
        super();
        m_rowIndex = rowIndex;
        m_colIndex = colIndex;
    }
    public int getRowIndex()
    {
        return m_rowIndex;
    }
    public int getColIndex()
    {
        return m_colIndex;
    }
    
    
}
