package info.horriblesubs.sher.api.horrible.response;

import java.io.Serializable;
import java.util.List;

import info.horriblesubs.sher.api.horrible.model.ScheduleItem;

public class ScheduleItems implements Serializable {
    public List<ScheduleItem> items;

    public ScheduleItems() {
    }
}
