package info.horriblesubs.sher.model.base;

import android.annotation.SuppressLint;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import info.horriblesubs.sher.Api;

public class ScheduleItem extends Item {

    public final String time;
    public final boolean isScheduled;

    public ScheduleItem(String id, String link, String title, String time, boolean isScheduled) {
        super(id, link, title);
        this.time = time;
        this.isScheduled = isScheduled;
    }

    @SuppressLint("SimpleDateFormat")
    public Date getTime() {
        DateFormat dateFormat = new SimpleDateFormat(Api.ServerTime);
        try {
            return dateFormat.parse(this.time);
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
    }

    @SuppressLint("SimpleDateFormat")
    public String getViewTime() {
        return (new SimpleDateFormat(Api.ViewTime)).format(getTime());
    }

    @SuppressLint("SimpleDateFormat")
    public String getViewDay() {
        return (new SimpleDateFormat(Api.ViewDay)).format(getTime());
    }

    @Override
    public String toString() {
        return "ID: " + this.id +
                "\n" + "Title: " + this.title +
                "\n" + "Link: " + this.link +
                "\n" + "Time: " + this.getViewTime() +
                "\n" + "isScheduled: " + this.isScheduled;
    }
}