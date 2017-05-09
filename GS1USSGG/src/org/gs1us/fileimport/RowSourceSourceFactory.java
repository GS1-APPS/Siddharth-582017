package org.gs1us.fileimport;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.Charset;
import java.nio.charset.UnsupportedCharsetException;

import org.gs1us.csv.DelimLineTokenizer;
import org.gs1us.csv.LineScanner;

/*
 * Factory to create a row source source for any type of file that contains one or more row/col structures.
 */
public class RowSourceSourceFactory
{
    /**
     * The MIME type of an ordinary text file
     */
    public static final String TEXT_MIME_TYPE = "application/text";
    
    /**
     * The MIME type of a Microsoft Office XLSX file
     */
    public static final String XLSX_MIME_TYPE = "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet";
    
    private ValueConditioner<String> m_valueConditioner;
    
    
    
    public ValueConditioner<String> getValueConditioner()
    {
        return m_valueConditioner;
    }

    public void setValueConditioner(ValueConditioner<String> valueConditioner)
    {
        m_valueConditioner = valueConditioner;
    }

    /**
     * Attempts to determine the character set of specified content. 
     * <ul>
     * <li>UTF-8 is the default, unless an invalid UTF-8 multibyte combination is detected.</li>
     * <li>Otherwise, if characters in the range 0x80 -- 0x9f are found, the result is windows-1252</li>
     * <li>Otherwise, iso-8859-1 is returned</li>
     * </ul>
     */
    public Charset guessCharset(byte[] content)
    {
        boolean couldBeCp1252 = false;
        boolean couldBeIso88591 = false;
        boolean couldBeUtf8 = true;
        
        for (int i = 0; i < content.length; i++)
        {
            int b = content[i] & 0xFF;
            if (b >= 0x80 && b <= 0x9f)
                couldBeCp1252 = true;
            if (b >= 0x80 && b <= 0xff)
                couldBeIso88591 = true;
            
            if (b >= 0x80)
            {
                int l = utf8MultibyteLength(content, i);
                if (l <= 0)
                    couldBeUtf8 = false;
                else
                    i += l-1;  // -1 because of the i++ in the for stmt
            }
        }
        
        if (couldBeUtf8)
            return Charset.forName("UTF-8");
        else if (couldBeCp1252)
        {
            try
            {
                return Charset.forName("windows-1252");
            }
            catch (UnsupportedCharsetException e)
            {
                return Charset.forName("ISO-8859-1");
            }
        }
        else
            return Charset.forName("ISO-8859-1");
    }

    private int utf8MultibyteLength(byte[] content, int i)
    {
        int b = content[i] & 0xFF;
        if (b >= 0xFE)
            return 0;
        else if (b >= 0xFC)
            return utf8MultibyteLength(content, i, 6);
        else if (b >= 0xF8)
            return utf8MultibyteLength(content, i, 5);
        else if (b >= 0xF0)
            return utf8MultibyteLength(content, i, 4);
        else if (b >= 0xE0)
            return utf8MultibyteLength(content, i, 3);
        else if (b >= 0xC0)
            return utf8MultibyteLength(content, i, 2);
        else
            return 0;
    }

    private int utf8MultibyteLength(byte[] content, int i, int length)
    {
        if (i + length > content.length)
            return 0;
        
        for (int j = i+1; j < i+length; j++)
        {
            if ((content[j] & 0xC0) != 0x80)
                return 0;
        }
        return length;
    }

    /**
     * Infers the most likely column delimiter in the specified content: comma, semicolon, or vertical bar.
     * Those possibilities are considered in that order. The first one that results in parsing more than one column is the one returned.
     * If none do, then comma is returned.
     */
    public char guessDelim(byte[] content, Charset charset, char quoteChar)
    {
        try
        {
            Reader reader = new InputStreamReader(new ByteArrayInputStream(content), charset);
            LineScanner scanner = new LineScanner(reader, quoteChar);
            String line = scanner.nextLine();
            if (line != null)
            {
                int commaCount = countTokens(new DelimLineTokenizer(line, ',', quoteChar));
                if (commaCount > 1)
                    return ',';
                int semicolonCount = countTokens(new DelimLineTokenizer(line, ';', quoteChar));
                if (semicolonCount > 1)
                    return ';';
                int pipeCount = countTokens(new DelimLineTokenizer(line, '|', quoteChar));
                if (pipeCount > 1)
                    return '|';
                else
                    return ',';

            }
            else
                return ',';
        }
        catch (IOException e)
        {
            return ',';
        }
    }

    private int countTokens(DelimLineTokenizer tokenizer)
    {
        int count = 0;
        while (tokenizer.hasMoreTokens())
        {
            tokenizer.nextToken();
            count++;
        }
        return count;
    }
    
    /**
     * Returns a <code>CsvRowSource</code> instance for the specified content, inferring the most appropriate character
     * set and delimiter character.
     */
    public CsvRowSource contentCsvRowSource(byte[] content) throws IOException
    {
        Charset charset = guessCharset(content);
        char quoteChar = '"';
        char delimChar = guessDelim(content, charset, quoteChar);
        CsvRowSource csvRowSource = new CsvRowSource(new InputStreamReader(new ByteArrayInputStream(content), charset), delimChar, quoteChar);
        if (m_valueConditioner != null)
            csvRowSource.setValueConditioner(m_valueConditioner);
        return csvRowSource;
    }

    /**
     * Returns the best guess at file format based on both the specified file format and the extension of the specified
     * filename (which may be omitted).
     */
    public String inferFormat(String filename, String format, byte[] content)
    {
        if (XLSX_MIME_TYPE.equals(format)) // format could be null
            return XLSX_MIME_TYPE;
        else if (filename != null && filename.toLowerCase().endsWith(".xlsx"))
            return XLSX_MIME_TYPE;
        else
            return TEXT_MIME_TYPE;
    }

    /**
     * Returns a <code>RowSourceSource</code> instance for the specified content, inferring the format from the
     * specified MIME format and filename. If the format is text, CSV syntax is assumed, inferring the most appropriate
     * character set and delimiter character.
     */
    public RowSourceSource<String> contentRowSourceSource(String filename, String format, byte[] content) throws ImportException
    {
        return contentRowSourceSource(inferFormat(filename, format, content), content);
    }
    
    /**
     * Returns a <code>RowSourceSource</code> instance for the specified content, inferring the format from the
     * specified MIME format. If the format is text, CSV syntax is assumed, inferring the most appropriate
     * character set and delimiter character.
     */
    public RowSourceSource<String> contentRowSourceSource(String format, byte[] content) throws ImportException
    {
        try
        {
            if (XLSX_MIME_TYPE.equals(format))
            {
                XlsxRowSourceSource xlsxRowSourceSource = new XlsxRowSourceSource(new ByteArrayInputStream(content));
                if (m_valueConditioner != null)
                    xlsxRowSourceSource.setValueConditioner(m_valueConditioner);
                return xlsxRowSourceSource;
            }
            else
                return new SingletonRowSourceSource<String>(contentCsvRowSource(content), null);
        }
        catch (IOException e)
        {
            throw new ImportException(e);
        }
    }
    
    public RowSource<String> contentFirstRowSource(String filename, String format, byte[] content) throws ImportException
    {
        return firstRowSource(contentRowSourceSource(filename, format, content));
    }
    
    public RowSource<String> contentFirstRowSource(String format, byte[] content) throws ImportException
    {
        return firstRowSource(contentRowSourceSource(format, content));
    }

    private <T> RowSource<T> firstRowSource(RowSourceSource<T> contentRowSourceSource) throws ImportException
    {
        if (contentRowSourceSource.next())
            return contentRowSourceSource.getRowSource();
        else
            return null;
    }
}
