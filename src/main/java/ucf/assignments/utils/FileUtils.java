/*
 * UCF COP3330 Fall 2021 Assignment 4 Solution
 * Copyright 2021 Tyler Snowdon
 */

package ucf.assignments.utils;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.nio.file.Files;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Scanner;

public class FileUtils {

    private static FileUtils instance;

    private static final String LIST_PATH = "src/main/content/tasklists/";

    public FileUtils() {

    }

    public static void write(String file, String contents) {
        try {
            FileWriter writer = new FileWriter(LIST_PATH + file);
            writer.write(contents);
            Logger.debug("\"%s\"\nhas been written", contents);
            writer.close();
        } catch (Exception e) {

        }
    }

    public static String read(String file) {
        try {
            String output = Files.readString(new File(LIST_PATH + file).toPath());
            Logger.debug("[FileUtils] read %s", output);
            return output;
        } catch (Exception e) {

        }
        return "";
    }

    public static String format(Date date) {
        String pattern = "yyyy-MM-dd";
        SimpleDateFormat format = new SimpleDateFormat(pattern);
        return format.format(date);
    }

    public static void FileUtils() {
        instance = new FileUtils();
    }
}
