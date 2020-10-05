package com.automic.ara.cdd.plugin.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

public class Iso8601 {
	public static String fromCalendar(final Calendar calendar) {
        Date date = calendar.getTime();
        String formatted = getIsoDateFormat().format(date);
        return formatted.substring(0, 22) + ":" + formatted.substring(22);
    }
	
	public static SimpleDateFormat getIsoDateFormat() {
		return new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ");
	}

    /** Get current date and time formatted as ISO 8601 string. */
    public static String now() {
        return fromCalendar(GregorianCalendar.getInstance());
    }

    public static Calendar toCalendar(final String iso8601string) {
        Calendar calendar = GregorianCalendar.getInstance();
        String s = iso8601string.replace("Z", "+00:00");
        try {
            s = s.substring(0, 22) + s.substring(23);
        } catch (IndexOutOfBoundsException e) {
            throw new RuntimeException("Invalid length:" + e.getMessage(), e);
        }
        Date date;
		try {
			date = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ").parse(s);
		} catch (ParseException e) {
			throw new RuntimeException("Error passing " + s + ": "  + e.getMessage(), e);
		}
        calendar.setTime(date);
        return calendar;
    }
}
