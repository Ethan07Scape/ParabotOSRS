package org.osrs.min.utils;

import java.text.DecimalFormat;

/**
 * Created by Ethan on 3/1/2018.
 */

public class TimeTables {

    public static final String runTime(long i) {
        DecimalFormat nf = new DecimalFormat("00");
        long millis = System.currentTimeMillis() - i;
        long hours = millis / 3600000L;
        millis -= hours * 3600000L;
        long minutes = millis / 60000L;
        millis -= minutes * 60000L;
        long seconds = millis / 1000L;
        return nf.format(hours) + ":" + nf.format(minutes) + ":" + nf.format(seconds);
    }

    public static final String runTimeMinutes(long i) {
        DecimalFormat nf = new DecimalFormat("00");
        long millis = System.currentTimeMillis() - i;
        long hours = millis / 3600000L;
        millis -= hours * 3600000L;
        long minutes = millis / 60000L;
        millis -= minutes * 60000L;
        long seconds = millis / 1000L;
        return nf.format(minutes) + ":" + nf.format(seconds);
    }

    public static final String countDown(long i) {
        DecimalFormat nf = new DecimalFormat("00");
        long millis = i;
        long hours = millis / 3600000L;
        millis -= hours * 3600000L;
        long minutes = millis / 60000L;
        millis -= minutes * 60000L;
        long seconds = millis / 1000L;
        return nf.format(hours) + ":" + nf.format(minutes) + ":" + nf.format(seconds);
    }

    public static final String formatNumber(int start) {
        DecimalFormat nf = new DecimalFormat("0.0");
        double i = start;
        if (i >= 1000000.0D) {
            return nf.format(i / 1000000.0D) + "m";
        }
        if (i >= 1000.0D) {
            return nf.format(i / 1000.0D) + "k";
        }
        return Integer.toString(start);
    }

    public static final String perHour(long startTime, int gained) {
        return formatNumber((int) (gained * 3600000.0D / (System.currentTimeMillis() - startTime)));
    }

    public static final int getPerHour(long startTime, int value) {
        return (int) (value * 3600000.0D / (System.currentTimeMillis() - startTime));

    }
}
