package org.sudadev.nefty.common;

import android.util.Log;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

public class DatesUtil {

    private Date date;
    private String dateString;
    private String timeString;
    private String dateMonthNameString;
    private String monthNameString;

    public static DatesUtil newInstance() {
        return new DatesUtil();
    }

    private DatesUtil() {
    }

    public boolean parse(String dateWithTime) {
        dateWithTime = dateWithTime.replaceAll("\u200f", "");

        SimpleDateFormat sdf;

        Locale locale;
        if (dateWithTime.contains("ص") || dateWithTime.contains("م")) {
            locale = new Locale("ar");
        }
        else {
            locale = Locale.ENGLISH;
        }

        if (dateWithTime.contains("/")) {
            if (dateWithTime.contains(":")) {
                if (locale.getLanguage().equals("ar")) {
                    sdf = new SimpleDateFormat("dd/MM/yy hh:mm:ss a", locale);
                }
                else
                    sdf = new SimpleDateFormat("MM/dd/yy hh:mm:ss a", locale);
            }
            else {
                if (locale.getLanguage().equals("ar")) {
                    sdf = new SimpleDateFormat("dd/MM/yy", locale);
                }
                else
                    sdf = new SimpleDateFormat("MM/dd/yy", locale);
            }
        }
        else {
            if (dateWithTime.endsWith("Z")) {
                sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", locale);
                sdf.setTimeZone(TimeZone.getTimeZone("GMT"));
            } else {
                sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", locale);
            }
        }

        DateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy", locale);
        DateFormat timeFormat = new SimpleDateFormat("hh:mm a", locale);
        DateFormat datemonthFormat = new SimpleDateFormat("dd MMMM yyyy", locale);
        DateFormat monthFormat = new SimpleDateFormat("MMMM", locale);

        try {
            date = sdf.parse(dateWithTime);
            dateString = dateFormat.format(date);
            timeString = timeFormat.format(date);
            dateMonthNameString  = datemonthFormat.format(date);
            monthNameString  = monthFormat.format(date);
            return true;
        } catch (ParseException e) {
            Log.d("PAARSE", dateWithTime);
            e.printStackTrace();
        }
        return false;
    }

    public String getDateTimeString() {
        return getDateString() + " - " + getTimeString();
    }

    public String getDateString() {
        return dateString;
    }

    public String getTimeString() {
        return timeString;
    }

    public String getDateMonthNameString() {
        return dateMonthNameString;
    }

    public String getMonthNameString() {
        return monthNameString;
    }

    private static SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("dd-MM-yyyy",
            Locale.ENGLISH);

    public static String formatDate(Date date) {
        return DATE_FORMAT.format(date);
    }

    public static Date addDays(Date date, int days) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.add(Calendar.DATE, days);
        return cal.getTime();
    }

    public static Date subtractDays(Date date, int days) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.add(Calendar.DATE, -days);
        return cal.getTime();
    }
}
