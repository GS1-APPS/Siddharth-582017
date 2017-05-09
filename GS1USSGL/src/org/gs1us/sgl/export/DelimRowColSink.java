package org.gs1us.sgl.export;

import java.io.IOException;
import java.io.Writer;

public class DelimRowColSink implements RowColSink
{
    private Writer m_writer;
    private char m_delimChar;
    private char m_quoteChar;
    
    private boolean m_inHeadings = true;
    private boolean m_rowStart = true;
    
    
    public DelimRowColSink(Writer writer, char delimChar, char quoteChar)
    {
        super();
        m_writer = writer;
        m_delimChar = delimChar;
        m_quoteChar = quoteChar;
    }
    
    public DelimRowColSink(Writer writer)
    {
        this(writer, ',', '"');
    }

    @Override
    public void heading(String heading)
    {
        if (!m_inHeadings)
            throw new IllegalStateException();
        cellInternal(heading);
    }

    @Override
    public void cell(String value)
    {
        if (m_inHeadings)
            throw new IllegalStateException();
        cellInternal(value);
    }
    
    @Override
    public void cell(Number value)
    {
        if (value == null)
            cell();
        else
            cell(value.toString());
    }
    
    @Override
    public void cell()
    {
        if (m_inHeadings)
            throw new IllegalStateException();
        cellInternal(null);
    }
    
    private void cellInternal(String value)
    {
        try
        {
            if (m_rowStart)
                m_rowStart = false;
            else
                m_writer.write(m_delimChar);

            if (value != null)
            {
                if (needsQuote(value))
                {
                    m_writer.write(m_quoteChar);
                    for (int i = 0; i < value.length(); i++)
                    {
                        char c = value.charAt(i);
                        if (c == m_quoteChar)
                            m_writer.write(m_quoteChar);
                        m_writer.write(c);
                    }
                    m_writer.write(m_quoteChar);
                }
                else
                    m_writer.write(value);
            }
        }
        catch (IOException e)
        {
            
        }
    }
    
    private boolean needsQuote(String value)
    {
        for (int i = 0; i < value.length(); i++)
        {
            char c = value.charAt(i);
            if (c == m_quoteChar || c == m_delimChar)
                return true;
        }
        return false;
    }

    @Override
    public void finish()
    {
    }
    
    @Override
    public void endRow()
    {
        try
        {
            m_writer.write('\n');
            m_rowStart = true;
            m_inHeadings = false;
        }
        catch (IOException e)
        {
            
        }
    }

}
