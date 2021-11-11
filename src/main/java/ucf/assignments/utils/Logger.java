/*
 * UCF COP3330 Fall 2021 Assignment 4 Solution
 * Copyright 2021 Tyler Snowdon
 */

package ucf.assignments.utils;

import ucf.assignments.App;

public class Logger {

    private static Logger instance;
    public static boolean debug = true;
    private static String debugHeader = "[DEBUG]";

    public Logger() {

    }

    public static boolean isDebug() {
        return debug;
    }

    public static void debug(String format, Object... parameters) {
        if (debug)
            System.out.printf("[DEBUG] " + format + "\n", parameters);
    }

    public static void debug(String message) {
        if (debug)
            System.out.printf("[DEBUG] " + message + "\n");
    }

    public static void initialize() {
        instance = new Logger();
    }
}
