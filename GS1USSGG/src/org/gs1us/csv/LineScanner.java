package org.gs1us.csv;

import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;

public class LineScanner
{
    private static final int NORMAL = 0;
    private static final int IN_QUOTE = 1;
    private static final int AFTER_CR = 2;
    
    private StringBuffer m_line = new StringBuffer();
    private boolean m_content = false;
    private int m_state = NORMAL;
    private char m_quote;
    private Reader m_reader;
    private int m_lineno = 0;
    
    public LineScanner(Reader reader)
    {
        this(reader, '"');
    }
    
    public LineScanner(Reader reader, char quoteChar)
    {
        m_reader = reader;
        m_quote = quoteChar;
    }
    
    public String nextLine() throws IOException
    {
        m_line.setLength(0);
        m_content = false;
        
        int c;
        while ((c = m_reader.read()) >= 0)
        {
            switch (m_state)
            {
            case NORMAL:
                if (c == '\r')
                {
                    m_content = true;
                    m_state = AFTER_CR;
                    return finish();
                }
                else if (c == '\n')
                {
                    m_content = true;
                    return finish();
                }
                else if (c == m_quote)
                {
                    m_state = IN_QUOTE;
                    m_line.append((char)c);
                    m_content = true;
                }
                else
                {
                    m_line.append((char)c);
                    m_content = true;
                }
                break;
                
            case IN_QUOTE:
                if (c == m_quote)
                {
                    m_state = NORMAL;
                    m_line.append((char)c);
                    m_content = true;
                }
                else
                {
                    m_line.append((char)c);
                    m_content = true;
                }
                break;
                
            case AFTER_CR:
                if (c == '\n')
                {
                }
                else if (c == '\r')
                {
                    m_content = true;
                    return finish();
                }
                else if (c == m_quote)
                {
                    m_state = IN_QUOTE;
                    m_line.append((char)c);
                    m_content = true;
                }
                else
                {
                    m_state = NORMAL;
                    m_line.append((char)c);
                    m_content = true;
                }
                break;
            }
        }
        return finish();
    }

    private String finish()
    {
        m_lineno++;
        if (m_content)
            return m_line.toString();
        else
            return null;
    }
    
    public int getLineno()
    {
        return m_lineno;
    }
}
