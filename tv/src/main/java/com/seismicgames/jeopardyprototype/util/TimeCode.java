package com.seismicgames.jeopardyprototype.util;

/**
 * Created by jduffy on 7/18/16.
 */
public class TimeCode {

    public static int parse(String ts) {
        int millis = - 1 * 60 * 60 * 1000;
        millis += Integer.parseInt(ts.substring(0, 2)) * 60 * 60 * 1000;
        millis += Integer.parseInt(ts.substring(3, 5)) * 60 * 1000;
        millis += Integer.parseInt(ts.substring(6, 8)) * 1000;
        millis += Integer.parseInt(ts.substring(9, 11)) * 1000d/29.97d; // *1000 /29.97
        return millis;
    }
}
