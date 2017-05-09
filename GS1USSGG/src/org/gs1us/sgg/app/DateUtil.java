package org.gs1us.sgg.app;

import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import org.gs1us.sgg.util.Util;

class DateUtil
{
    public static Date dateMin(Date d1, Date d2, boolean nullTakesPriority)
    {
        if (d1 == null)
        {
            if (nullTakesPriority)
                return null;
            else
                return d2;
        }
        else if (d2 == null)
        {
            if (nullTakesPriority)
                return null;
            else
                return d1;
        }
        else if (d1.before(d2))
            return d1;
        else
            return d2;
    }

    public static Date dateMin(Date d1, Date d2)
    {
        if (d1.before(d2))
            return d1;
        else
            return d2;
    }

    public static Date dateMax(Date d1, Date d2)
    {
        if (d1.after(d2))
            return d1;
        else
            return d2;
    }
    
    public static Date roundToDay(Date d)
    {
        Calendar cal = new GregorianCalendar();
        cal.setTime(d);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.HOUR_OF_DAY, 0);
        return cal.getTime();
    }
    
    public static Date oneYearLater(Date d)
    {
        Calendar cal = new GregorianCalendar();
        cal.setTime(d);
        cal.set(Calendar.YEAR, cal.get(Calendar.YEAR) + 1);
        return cal.getTime();
        
    }
    
    public static double proRataFactor(Date originalDate, Date newDate)
    {
        if (originalDate == null || !newDate.before(originalDate))
            return 0.0;
        
        Date d = originalDate;
        int months = 0;
        while (!d.before(newDate))
        {
            months++;
            Calendar cal = new GregorianCalendar();
            cal.setTime(d);
            cal.set(Calendar.MONTH, cal.get(Calendar.MONTH) - 1);
            d = cal.getTime();
        }
        
        return months / 12.0;
        
    }
    /*
    public static Date parseDateAttribute(String string)
    {
        if (string == null)
            return null;
        try
        {
            // DATE_FORMAT's timezone is set to GMT, so will give us midnight GMT on the specified date
            return Util.DATE_FORMAT.parse(string);
        }
        catch (ParseException e)
        {
            return null;
        }
    }
    */

}
