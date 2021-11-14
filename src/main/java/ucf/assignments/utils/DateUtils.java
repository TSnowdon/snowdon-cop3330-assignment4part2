/*
 * UCF COP3330 Fall 2021 Assignment 4 Solution
 * Copyright 2021 Tyler Snowdon
 */

package ucf.assignments.utils;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Scanner;

public class DateUtils {

    private static DateUtils instance;

    public DateUtils() {

    }

    /**
     * Parses from Strings to Dates
     * Returns todays date if date is empty
     *
     * @param date The input string, must be formatted as "YYYY-MM-DD".
     * @return A new Date that matches the date described in "date".
     */
    public static Date parse(String date) {
        Date out = new Date();
        if (date.isEmpty() || date.isBlank()) {
            Logger.debug("Empty date found");
            return out;
        }

        int year = 1;
        int month = 1;
        int day = 1;

        date = date.replace("-", " ");
        Scanner reader = new Scanner(date);
        try {
            year = reader.nextInt();
            month = reader.nextInt() - 1;
            day = reader.nextInt();
        } catch (Exception ignored) {
        }
        Logger.debug("Parsed %d %d %d", year, month, day);
        out = Date.from(new GregorianCalendar(year, month, day).toZonedDateTime().toInstant());

        return out;
    }

    public static String format(Date date) {
        String pattern = "yyyy-MM-dd";
        SimpleDateFormat format = new SimpleDateFormat(pattern);
        return format.format(date);
    }

    public static void initialize() {
        instance = new DateUtils();
    }
}
