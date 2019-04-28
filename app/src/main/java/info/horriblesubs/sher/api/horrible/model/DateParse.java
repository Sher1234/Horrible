package info.horriblesubs.sher.api.horrible.model;

import org.jetbrains.annotations.NotNull;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public abstract class DateParse {

    Date getScheduleDate(String date) {
        try {
            String format = "dd HH:mm Z";
            return new SimpleDateFormat(format, Locale.US).parse(date);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static Date getNetworkDate(String date) {
        try {
            String format = "yyyy-MM-d HH:mm:ss Z";
            return new SimpleDateFormat(format, Locale.US).parse(date);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static String getNetworkDate(Date date) {
        try {
            String format = "MMMM dd, yyyy @ hh:mm a";
            return new SimpleDateFormat(format, Locale.US).format(date);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @NotNull
    public static String getTimeString(long time) {
        long minutes = Math.round((double) time / 60000);
        long hours = Math.round((double) time / 3600000);
        long days = Math.round((double) time / 86400000);
        if (time == -1L) return "never.";
        else if (days > 0)
            if (days == 1) return "a day ago.";
            else return days + " days ago.";
        else if (hours > 0)
            if (hours == 1) return "an hours ago.";
            else return hours + " hours ago.";
        else if (minutes > 0)
            if (minutes == 1) return "a minutes ago.";
            else return minutes + " minutes ago.";
        return "few seconds ago.";
    }

    String getScheduleTime(String date) {
        try {
            return new SimpleDateFormat("HH:mm", Locale.US).format(getScheduleDate(date));
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}