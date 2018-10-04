package info.horriblesubs.sher.model.base;

import org.jetbrains.annotations.NotNull;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import androidx.annotation.Nullable;
import info.horriblesubs.sher.common.Strings;

public class ScheduleItem extends Item {

    private String time;
    public boolean isScheduled;

    public ScheduleItem() {
        super();
    }

    @Nullable
    public Date getTime() {
        DateFormat dateFormat = new SimpleDateFormat(Strings.ServerTime, Locale.US);
        try {
            return dateFormat.parse(this.time);
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
    }

    public String getViewTime() {
        return (new SimpleDateFormat(Strings.ViewTime, Locale.US)).format(getTime());
    }

    public String getViewDay() {
        return (new SimpleDateFormat(Strings.ViewDay, Locale.US)).format(getTime());
    }

    @NotNull
    @Override
    public String toString() {
        return "ID: " + this.id +
                "\n" + "Title: " + this.title +
                "\n" + "Link: " + this.link +
                "\n" + "Time: " + this.getViewTime() +
                "\n" + "isScheduled: " + this.isScheduled;
    }
}