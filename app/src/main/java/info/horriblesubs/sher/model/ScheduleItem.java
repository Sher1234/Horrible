package info.horriblesubs.sher.model;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

@SuppressWarnings("all")
public class ScheduleItem extends Item {

    public String time;
    public boolean isScheduled;

    public ScheduleItem(String nId, String link, String title, String time, boolean isScheduled) {
        super(nId, link, title);
        this.time = time;
        this.isScheduled = isScheduled;
    }

    public Date getDate() {
        DateFormat dateFormat = new SimpleDateFormat("dd HH:mm Z");
        try {
            return dateFormat.parse(this.time);
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public String toString() {
        return "NotificationRequest ID: " + this.nId +
                "\n" + "Title: " + this.title +
                "\n" + "Link: " + this.link +
                "\n" + "Time: " + this.getDate() +
                "\n" + "isScheduled: " + this.isScheduled;
    }
}