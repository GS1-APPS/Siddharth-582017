package org.gs1us.csv;

import java.text.ParsePosition;

/**
 * LineTokenizer for delimeter separated files.  Use to parse a given line into
 * tokens based on the delimeter char. A "token" is defined here to 
 * be one field in a line (not including the delimiting commas or quotes). 
 * Quote char support is optional. Supports the escaping of the quote char
 * by repeating it. (e.g. "This token ""has"" embedded quotes" )
 */
public class DelimLineTokenizer extends LineTokenizer
{
    private boolean m_lastEndedWithDelim = false;

    private char m_delimChar = '\0';
    public void setDelimChar(char delimChar)
    {
        m_delimChar = delimChar;
    }

    private char m_quoteChar = '\0';
    public void setQuoteChar(char quoteChar)
    {
        m_quoteChar = quoteChar;
    }

    public boolean hasQuoteChar() {
        return '\0' != m_quoteChar ? true : false;
    }

    public DelimLineTokenizer(String str, char delimChar, char quoteChar )
    {
        super( str );

        m_quoteChar = quoteChar;
        m_delimChar = delimChar;
    }

    /**
     * Parse the next token in the input stream. If we already have one, just return.
     * Note the examination of null tokens and the handdling of trailing ',' which
     * indicates a null token at the end of the line.
     */
    protected void parseNextToken()
    {
        if (m_haveToken) {
            return;
        }
        else if (m_nextIndex >= m_length) {
            if (m_lastEndedWithDelim) {
                m_haveToken = true;
                m_lastEndedWithDelim = false;
            }
            else {
                m_done = true;
            }
        }
        else {
            StringBuffer tokenBuffer = new StringBuffer();

            ParsePosition pos = new ParsePosition(m_nextIndex);
            boolean wasNull = findDelimAndExtractToken(pos,
                                                       tokenBuffer);

            if (wasNull) {
                m_nextToken = null;
            }
            else {
                if (0 == tokenBuffer.length()) {
                    m_nextToken = new String(" "); // return the empty string
                }
                else {
                    m_nextToken = tokenBuffer.toString();
                }
            }

            if (pos.getIndex() >= m_length) {
                // We're done, but was a null token encountered at completion?
                if (null != m_nextToken) {
                    m_lastEndedWithDelim = false;
                    m_haveToken = true;
                }
                else { 
                    m_lastEndedWithDelim = true;
                    m_haveToken = false;
                }
                m_nextIndex = m_length;
            }
            else {
                m_haveToken = true;
                m_nextIndex = pos.getIndex()+1;
                m_lastEndedWithDelim = true;
            }
        }
    }

    /** 
     * Find the index of the next delimiter placing it in pos and fill the provided
     * string buffer with the token encountered during the search.
     * Uses a state machine to do the work.
     * @return true if the token was null, false if otherwise.
     */
    protected boolean findDelimAndExtractToken(ParsePosition pos,
            StringBuffer accum)
    {
        final int START = 1;    // state machine always begins here
        final int BODY = 2;     // state entered when body of a token is encountered
        final int QUOTED = 3;   // state entered when a quote char is encountered
        final int POSTQUOTE = 4;// state entered after a quote char is encountered

        int length = m_str.length();

        int state = START;

        for (int i = pos.getIndex(); i < length; i++) {
            char c = m_str.charAt(i);

            switch (state) {
            case START:
                if (hasQuoteChar() && c == m_quoteChar)
                    state = QUOTED;
                else if (c == m_delimChar) {
                    pos.setIndex(i);
                    return true;
                }
                else if (!(Character.isWhitespace(c))) {
                    state = BODY;
                    accum.append(c);
                }
                break;

            case BODY:
                if (c == m_delimChar) {
                    pos.setIndex(i);
                    return false;
                }
                else
                    accum.append(c);
                break;

            case QUOTED:
                if (hasQuoteChar() && c == m_quoteChar)
                    state = POSTQUOTE;
                else
                    accum.append(c);
                break;

            case POSTQUOTE:
                if (c == m_delimChar) {
                    pos.setIndex(i);
                    return false;
                }
                else if (hasQuoteChar() && c == m_quoteChar) {
                    state = QUOTED;
                    accum.append(c);
                }
                else {
                    // we unexpectedly encountered a non-quote/non-delim 
                    // character, assume the intention was to embed a 
                    // escaped double-quote
                    state = QUOTED;
                    accum.append('"');
                    accum.append(c);
                }
                break;
            }
        }
        pos.setIndex(length);
        return (accum.length() == 0); // return true if we've accumulated nothing
    }
}
