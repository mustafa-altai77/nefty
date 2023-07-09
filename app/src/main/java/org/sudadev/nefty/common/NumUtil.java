package org.sudadev.nefty.common;

import java.util.Locale;

public class NumUtil {
    public static String format(double number) {
        return String.format(Locale.ENGLISH, "%,.2f", number);
    }

    //without decimal digits
    public static String formatIntPercentage(double number){
        return String.format(Locale.ENGLISH, "%.0f",number*100)+"%";
    }

    //accept a param to determine the numbers of decimal digits
    public static String formatDoublePercentage(double number){
        int digits = 2;
        return String.format(Locale.ENGLISH, "%."+digits+"f",number*100)+"%";
    }
}
