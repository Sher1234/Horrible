package info.horriblesubs.sher.model;

import java.util.Date;

@SuppressWarnings("all")
public class ScheduleItem extends Item {

    private Date date;
    public String time;
    public boolean isScheduled;

    public ScheduleItem(String nId, String link, String title, String time, boolean isScheduled) {
        super(nId, link, title);
        this.time = time;
        this.isScheduled = isScheduled;
    }

    @Override
    public String toString() {
        return "Notification ID: " + this.nId +
                "\n" + "Title: " + this.title +
                "\n" + "Link: " + this.link +
                "\n" + "Time: " + this.date.toString() +
                "\n" + "isScheduled: " + this.isScheduled;
    }
}