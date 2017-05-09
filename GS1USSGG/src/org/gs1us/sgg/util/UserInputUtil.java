package org.gs1us.sgg.util;

import java.nio.charset.Charset;
import java.nio.charset.UnsupportedCharsetException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;
import java.util.regex.Pattern;

public class UserInputUtil
{
    private static final String FORBIDDEN_FILENAME_CHARS = "/\\:*?\"<>|";
   
    /*
    private static final String DATE_FORMAT = "yyyy-MM-dd HH:mm:ss";
    private static final String DATE_ONLY_FORMAT = "yyyy-MM-dd";
    */
    
    private static final String DATE_FORMAT = "M/d/yyyy HH:mm:ss";
    private static final String DATE_ONLY_FORMAT = "M/d/yyyy";
    
    // word . word . word @ atom . atom . atom
    private static final String ATOM = "[^()<>@,;:\\\\\".\\[\\]]+";
    private static final Pattern EMAIL_ADDRESS_REGEX = 
            Pattern.compile(ATOM + "(\\." + ATOM + ")*@" + ATOM + "(\\." + ATOM + ")+");

    /**
     * Returns the specified content as a string, trimmed of leading and trailing whitespace, or null if the 
     * specified content is null or all whitespace.
     */
    public static String trimToNull(byte[] content)
    {
        if (content == null)
            return null;
        else
            return trimToNull(new String(content));
    }

    /**
     * Returns the string trimmed of leading and trailing whitespace, or null if the 
     * specified string is null or all whitespace.
     */
    public static String trimToNull(String s)
    {
        if (s == null)
            return null;
        else
        {
            // Note: Java trim() doesn't pick up all whitespace characters! If you want a job done right...
            int len = s.length();
            
            if (len == 0)
                return null;
            
            int start = 0;
            while (start < len && isTrimmable(s.charAt(start)))
                start++;
            // start is now first non-WS or is = len
            int end = len;
            while (end > 0 && isTrimmable(s.charAt(end - 1)))
                end--;
            // end is now one past last nonWS or is = 0
            
            if (end > start)
                return s.substring(start, end);
            else
                return null;
                       
        }
    }
    
    private static boolean isTrimmable(char c)
    {
        return Character.isWhitespace(c) || c == '\u00a0' || c == '\u2007' || c == '\u202f';
    }

    /**
     * Returns the first element of the specified string array trimmed of leading and trailing whitespace, or null if the 
     * specified string array is null, of zero length, or if the first element is null or all whitespace.
     */
    public static String trimToNull(String[] s)
    {
        if (s == null)
            return null;
        else if (s.length == 0)
            return null;
        else
            return trimToNull(s[0]);
    }
    
    /**
     * Converts a string into a string suitable for use as a filename, by substituting the underscore
     * character for all characters forbidden in filenames
     */
    public static String stringToFilename(String s)
    {
        return stringToFilename(s, '_');
    }
    
    /**
     * Converts a string into a string suitable for use as a filename, by substituting the specified surrogate
     * character for all characters forbidden in filenames
     */
    public static String stringToFilename(String s, char surrogate)
    {
        StringBuffer buf = new StringBuffer();
        for (int i = 0; i < s.length(); i++)
        {
            char c = s.charAt(i);
            if (FORBIDDEN_FILENAME_CHARS.indexOf(c) >= 0)
                buf.append(surrogate);
            else
                buf.append(c);
        }
        return buf.toString();
    }
    
    private static String[] s_timeZoneIdList = null;
    
    /**
     * Returns a list of time zone identifiers for use in UIs that need to offer a selection list.
     */
    public static synchronized String[] getTimeZoneIds()
    {
        if (s_timeZoneIdList == null)
        {
            String[] unfilteredTimeZoneIds = TimeZone.getAvailableIDs();
            List<String> filtered = new ArrayList<String>(unfilteredTimeZoneIds.length);
            
            for (String timeZoneId : unfilteredTimeZoneIds)
            {
                if (timeZoneId.indexOf('/') > 0 && !(timeZoneId.startsWith("SystemV/") || timeZoneId.startsWith("Etc/GMT")))
                    filtered.add(timeZoneId);
            }
           
            String[] sorted = filtered.toArray(new String[0]);
            Arrays.sort(sorted);
            s_timeZoneIdList = sorted;
        }
        return s_timeZoneIdList;
    }
    
    /**
     * Converts a date to a string in a consistent format.
     */
    public static String dateToString(Date date, String timeZoneId)
    {
        TimeZone tz = timeZoneId == null ? null : TimeZone.getTimeZone(timeZoneId);
        if (tz == null)
            tz = TimeZone.getDefault();
        
        DateFormat format = new SimpleDateFormat(DATE_FORMAT);
        format.setTimeZone(tz);
        return format.format(date);
    }
    
    /**
     * Converts a string to a date, using the same format as used for dateToString.
     */
    public static Date stringToDate(String s, String timeZoneId) throws ParseException
    {
        TimeZone tz = timeZoneId == null ? null : TimeZone.getTimeZone(timeZoneId);
        if (tz == null)
            tz = TimeZone.getDefault();
        
        DateFormat format = new SimpleDateFormat(DATE_FORMAT);
        format.setTimeZone(tz);
        return format.parse(s);
    }

    /**
     * Converts a date to a string in a consistent format.
     */
    public static String dateOnlyToString(Date date, String timeZoneId)
    {
        TimeZone tz = timeZoneId == null ? null : TimeZone.getTimeZone(timeZoneId);
        if (tz == null)
            tz = TimeZone.getDefault();
        
        DateFormat format = new SimpleDateFormat(DATE_ONLY_FORMAT);
        format.setTimeZone(tz);
        return format.format(date);
    }
    
    /**
     * Converts a string to a date, using the same format as used for dateToString.
     */
    public static Date stringToDateOnly(String s, String timeZoneId) throws ParseException
    {
        TimeZone tz = timeZoneId == null ? null : TimeZone.getTimeZone(timeZoneId);
        if (tz == null)
            tz = TimeZone.getDefault();
        
        DateFormat format = new SimpleDateFormat(DATE_ONLY_FORMAT);
        format.setTimeZone(tz);
        return format.parse(s);
    }

    /**
     * Converts a filesystem or URL pathname to a simple name by stripping off directory prefixes and .type extensions.
     */
    public static String pathnameToName(String filename)
    {
        if (filename == null || filename.length() == 0)
            return null;
        
        int end = filename.length();
        
        int lastDot = filename.lastIndexOf('.');
        if (lastDot > 0)
            end = lastDot;
        
        // Note that end is always > 0
        int start = end;
        while (start > 0)
        {
            char c = filename.charAt(start-1);
            if (c == '/' || c == '\\')
                break;
            start--;
        }
        return filename.substring(start, end);
        
    }
    
    /**
     * Parses a string into an integer, returning null if the string cannot be so parsed.
     */
    public static Integer parseIntOrNull(String s)
    {
        String trimmed = trimToNull(s);
        if (trimmed == null)
            return null;
        
        try
        {
            return Integer.parseInt(s);
        }
        catch (NumberFormatException e)
        {
            return null;
        }
    }
    
    /**
     * Parses a string into an integer, returning the specified default value if the string cannot be so parsed.
     */
    public static int parseIntOrNull(String s, int defaultValue)
    {
        Integer value = parseIntOrNull(s);
        if (value == null)
            return defaultValue;
        else
            return value;
        
    }
    
    public static boolean isValidEmailAddress(String s)
    {
        return EMAIL_ADDRESS_REGEX.matcher(s).matches();
    }
    
    public static String ellipsis(String s, int threshold, int truncateTo)
    {
        if (s.length() > threshold)
            return s.substring(0, truncateTo) + "...";
        else
            return s;
    }
    
    public static Charset guessCharset(byte[] content)
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

    private static int utf8MultibyteLength(byte[] content, int i)
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

    private static int utf8MultibyteLength(byte[] content, int i, int length)
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

    public static boolean isValidGln(String gln)
    {
        if (gln.length() != 13)
            return false;
        
        int sum = 0;
        for (int i = 0; i < 13; i++)
        {
            char c = gln.charAt(i);
            if (c < '0' || c > '9')
                return false;
            
            int val = c - '0';
            sum += val * ((i % 2) == 0 ? 1 : 3);
        }
        return (sum % 10 == 0);
    }

    public static boolean isValidGtin(String gtin)
    {
        int len = gtin.length();
        
        if (len != 14 && len != 13 && len != 12 && len != 8)
            return false;
        
        String padded = "000000".substring(0, 14 - len) + gtin;
        
        int sum = 0;
        for (int i = 0; i < 14; i++)
        {
            char c = padded.charAt(i);
            if (c < '0' || c > '9')
                return false;
            
            int val = c - '0';
            sum += val * ((i % 2) == 0 ? 3 : 1);
        }
        return (sum % 10 == 0);
    }

    public static boolean isValidGcp(String gcp)
    {
        if (gcp.length() < 3 || gcp.length() > 12)
            return false;
        for (int i = 0; i < gcp.length(); i++)
        {
            char c = gcp.charAt(i);
            if (c < '0' || c > '9')
                return false;
        }
        return true;
    }

    public static String[] parseList(String s, String separators)
    {
        return s.split(separators);
    }
    
    public static String printList(String[] l, String separator)
    {
        StringBuffer buf = new StringBuffer();
        for (int i = 0; i < l.length; i++)
        {
            if (i > 0)
                buf.append(separator);
            buf.append(l[i]);
        }
        return buf.toString();
    }


}
