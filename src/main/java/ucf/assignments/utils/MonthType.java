/*
 * UCF COP3330 Fall 2021 Assignment 4 Solution
 * Copyright 2021 Tyler Snowdon
 */

package ucf.assignments.utils;

import java.util.Calendar;

public enum MonthType {
    JANUARY(0, "January", "Jan"),
    FEBRUARY(1, "February", "Feb"),
    MARCH(2, "March", "Mar"),
    APRIL(3, "April", "Apr"),
    MAY(4, "May", "May"),
    JUNE(5, "June", "Jun"),
    JULY(6, "July", "Jul"),
    AUGUST(7, "August", "Aug"),
    SEPTEMBER(8, "September", "Sep"),
    OCTOBER(9, "October", "Oct"),
    NOVEMBER(10, "November", "Nov"),
    DECEMBER(11, "December", "Dec");

    private final int pos;
    private final String full;
    private final String abb;

    MonthType(int pos, String full, String abb) {
        this.pos = pos;
        this.full = full;
        this.abb = abb;
    }

    public int getPos() {
        return pos;
    }

    public String getFull() {
        return full;
    }

    public String getAbb() {
        return abb;
    }

    public MonthType getMonth(int pos) {
        for (MonthType value : values()) {
            if (value.getPos() == pos) {
                return value;
            }
        }
        return null;
    }
}
