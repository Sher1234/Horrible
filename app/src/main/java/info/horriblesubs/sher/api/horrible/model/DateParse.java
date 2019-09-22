package info.horriblesubs.sher.api.horrible.model;

import android.util.Log;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

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

    @Nullable
    public static Date getNetworkDate(String date) {
        try {
            String format = "yyyy-MM-d HH:mm:ss Z";
            return new SimpleDateFormat(format, Locale.US).parse(date);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Nullable
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
        int minutes = (int) ((today - time) / 60000);
        int hours = (int) ((today - time) / 3600000);
        int days = (int) ((today - time) / 86400000);
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

    String getScheduleLeftTime(String date) {
        String s = new SimpleDateFormat("dd-MM-yyyy", Locale.US).format(new Date()) + " " + getScheduleTime(date);
        try {
            Date d = new SimpleDateFormat("dd-MM-yyyy HH:mm", Locale.US).parse(s);
            return getTimeLeft(d);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @NotNull
    private static String getTimeLeft(Date date) {
        long time = date == null ? -1L : date.getTime();
        long today = new Date().getTime();
        if (today > time) return "Aired";
        int hours = (int) ((today - time) / 3600000);
        int minutes = (int) ((today - time) / 60000);
        minutes = minutes - (hours * 60);
        minutes = minutes >= 0 ? minutes : -minutes;
        hours = hours >= 0 ? hours : -hours;
        Log.e("ABC-D", "Today: " + today + " Time: " + time + " Diff: " + (today - time) + " H: " + hours + " M: " + minutes);
        if (time == -1L) return "Error";
        return "in " + (hours > 0 ? hours + "h " : "") + (minutes > 0 ? minutes + "m" : "");
    }
}