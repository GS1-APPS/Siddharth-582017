package org.gs1us.fileimport;

import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;
import java.util.List;

import org.gs1us.csv.DelimLineTokenizer;
import org.gs1us.csv.LineScanner;

public class CsvRowSource implements RowSource<String>
{
    private char m_delimChar;
    private char m_quoteChar;
    
    private LineScanner m_scanner;
    private int m_rowNum = -1;
    private List<String> m_currentRow = new ArrayList<String>();
    
    private ValueConditioner<String> m_valueConditioner = new IdentityValueConditioner<String>();

    public CsvRowSource(Reader reader) throws IOException
    {
        this(reader, ',', '"');
    }
    
    public CsvRowSource(Reader reader, char delimChar, char quoteChar) throws IOException
    {
        m_delimChar = delimChar;
        m_quoteChar = quoteChar;
        m_scanner = new LineScanner(reader, m_quoteChar);
    }
    
    public ValueConditioner<String> getValueConditioner()
    {
        return m_valueConditioner;
    }

    public void setValueConditioner(ValueConditioner<String> valueConditioner)
    {
        m_valueConditioner = valueConditioner;
    }

 
    @Override
    public boolean next() throws ImportException
    {
        try
        {
            m_currentRow.clear();

            String line = m_scanner.nextLine();
            if (line == null)
                return false;
            
            DelimLineTokenizer tokenizer = new DelimLineTokenizer(line, m_delimChar, m_quoteChar);
            while (tokenizer.hasMoreTokens())
            {
                m_currentRow.add(m_valueConditioner.condition(tokenizer.nextToken()));
            }
            m_rowNum++;
            return true;
        }
        catch (IOException e)
        {
            throw new ImportException(e);
        }
    }

    @Override
    public List<String> getRowContents()
    {
        return m_currentRow;
    }

    @Override
    public int getRowIndex()
    {
        return m_rowNum;
    }

    

}
