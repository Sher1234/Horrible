package info.horriblesubs.sher.api.horrible.model;

import androidx.annotation.Nullable;

import org.jetbrains.annotations.NotNull;

import java.util.Date;

public class ScheduleItem extends Item {

    public boolean scheduled;
    public String time;

    public ScheduleItem() {
        super();
    }

    @Nullable
    public Date getDate() {
        return parseDate(time);
    }

    public String getTime() {
        return parseTimeView(time);
    }

    @NotNull
    @Override
    public String toString() {
        return super.toString() +
                "\n" + "Time: " + this.getTime() +
                "\n" + "isScheduled: " + this.scheduled;
    }
}