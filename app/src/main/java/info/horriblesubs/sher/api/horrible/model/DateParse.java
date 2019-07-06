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

    String getShowShort(String s) {
        try {
            Date date = getNetworkDate(s);
            return getTimeString(date == null?-1L :date.getTime());
        } catch (Exception e) {
            e.printStackTrace();
            return "Never";
        }
    }

    @NotNull
    private static String getTimeString(long time) {
        long today = new Date().getTime();
        long minutes = Math.round((double) (today - time) / 60000);
        long hours = Math.round((double) (today - time) / 3600000);
        long days = Math.round((double) (today - time) / 86400000);
        if (time == -1L) return "Never";
        else if (days > 0)
            if (days == 1) return "Yesterday";
            else return days + "  Days";
        else if (hours > 0)
            if (hours == 1) return "1 Hour";
            else return hours + " Hours";
        else if (minutes > 5)
            return minutes + " Minutes";
        return "Now";
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