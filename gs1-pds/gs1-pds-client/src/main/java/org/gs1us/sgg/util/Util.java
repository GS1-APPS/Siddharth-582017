package org.gs1us.sgg.util;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.FieldPosition;
import java.text.ParseException;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

public class Util
{
    public static final SimpleDateFormat DATE_FORMAT = new SynchronizedSimpleDateFormat("yyyy-MM-dd");
    static
    {
        DATE_FORMAT.setTimeZone(TimeZone.getTimeZone("GMT"));
    }
    public static SimpleDateFormat ISO8601_DATE_FORMAT = new SynchronizedSimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSXXX");
    public static SimpleDateFormat ISO8601_DATE_FORMAT_NO_MILLIS = new SynchronizedSimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssXXX");
    
    public static byte[] inputStreamContent(InputStream is) throws IOException
    {
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        byte[] buf = new byte[1024];
        int count;
        while ((count = is.read(buf, 0, 1024)) > 0)
            os.write(buf, 0, count);
        return os.toByteArray();
    }
    
   
    /**
     * Like <code>SimpleDateFormat</code>, but the <code>parse</code> and <code>format</code> methods
     * are synchronized, to avoid problems because the Java class is not thread-safe.
     * @author kt
     *
     */
    private static class SynchronizedSimpleDateFormat extends SimpleDateFormat
    {
        private static final long serialVersionUID = 4508178404247776503L;

        public SynchronizedSimpleDateFormat(String format)
        {
            super(format);
        }

        @Override
        public synchronized StringBuffer format(Date date, StringBuffer toAppendTo, FieldPosition pos)
        {
            // TODO Auto-generated method stub
            return super.format(date, toAppendTo, pos);
        }

        @Override
        public synchronized Date parse(String source) throws ParseException
        {
            // TODO Auto-generated method stub
            return super.parse(source);
        }

        @Override
        public synchronized Object parseObject(String source, ParsePosition pos)
        {
            // TODO Auto-generated method stub
            return super.parseObject(source, pos);
        }

        
    }


}
