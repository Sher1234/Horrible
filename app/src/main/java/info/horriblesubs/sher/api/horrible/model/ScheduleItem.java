package info.horriblesubs.sher.api.horrible.model;

import androidx.annotation.Nullable;

import org.jetbrains.annotations.NotNull;

import java.util.Date;

public class ScheduleItem extends Item {

    public boolean scheduled;
    private String time;

    public ScheduleItem() {
        super();
    }

    @Nullable
    public Date getDate() {
        return getScheduleDate(time);
    }

    public String getTime() {
        return getScheduleTime(time);
    }

    public String getLeftTime() {
        return getScheduleLeftTime(time);
    }

    @NotNull
    @Override
    public String toString() {
        return super.toString() +
                "\n" + "Time: " + this.getTime() +
                "\n" + "isScheduled: " + this.scheduled;
    }
}