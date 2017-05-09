package org.gs1us.csv;


import java.text.ParsePosition;

/** Abstract class LineTokinizer based on the the java StringTokenizer 
 * interface. The class implements the common functions of a tokenizer 
 * and defines a common interface for the implementation specific aspects
 * of finding and extracting tokens.
 */
public abstract class LineTokenizer {
    protected String m_str;
    protected int m_length;
    protected int m_nextIndex = 0;
    protected String m_nextToken = null;
    protected boolean m_haveToken = false;
    protected boolean m_done = false;

    /** Sole constructor for line tokenizer takes the line to tokenize. 
@param str The line to tokenize in String form.
     */
    public LineTokenizer(String str)
    {
        m_str = str;
        m_length = str.length();
        m_nextIndex = 0;
    }

    public boolean hasMoreTokens()
    {
        parseNextToken();

        return !m_done;
    }

    public String nextToken()
    {
        parseNextToken();

        String result = m_nextToken;
        m_nextToken = null;
        m_haveToken = false;
        return result;
    }

    /** Find and parse the next token in the string, if any */
    abstract protected void parseNextToken();

    /** 
     * Find the next delimeter in the line starting at the given index and
     * fill the provided before with the token.
     */
    abstract protected boolean findDelimAndExtractToken(ParsePosition pos,
            StringBuffer buffer);
}
