package org.communis.javawebintro.utils;

import java.text.SimpleDateFormat;
import java.util.Date;

public class DateUtils
{

    public static String getSimpleDateFormat(Date date, String pattern)
    {
        if (date == null)
        {
            return "";
        }
        else
        {
            SimpleDateFormat dateFormat = new SimpleDateFormat(pattern);
            return dateFormat.format(date);
        }
    }

    public static String getSimpleDate(Date date)
    {
        return getSimpleDateFormat(date, "dd.MM.yyyy");
    }

    public static String getDateTimeFormat(Date date)
    {
        return getSimpleDateFormat(date, "dd.MM.yyyy HH:mm:ss");
    }

    public static Date parseDate(String source)
    {
        Date rezult = null;
        SimpleDateFormat dateFormat;

        if (source != null)
        {
            try
            {

                dateFormat = new SimpleDateFormat("dd.MM.yyyy");
                rezult = dateFormat.parse(source);
            }
            catch (Exception ex)
            {
            }
        }
        return rezult;
    }
    
    public static Date parseDateTime(String source)
    {
        Date dt = null;
        if (source != null)
        {
            String newSource = source.replace(".", "-");
            String ss = newSource + " 00:00:00";
            try
            {
                SimpleDateFormat f = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
                dt = f.parse(ss);
            }
            catch (Exception ex)
            {
            }
        }
        return dt;
    }
}
