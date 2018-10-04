package info.horriblesubs.sher.model.response;

import java.io.Serializable;
import java.util.List;

import info.horriblesubs.sher.model.base.Response;
import info.horriblesubs.sher.model.base.ScheduleItem;

public class ScheduleResponse implements Serializable {

    public Response response;
    public List<ScheduleItem> schedule;

    public ScheduleResponse() {

    }

    public ScheduleResponse(List<ScheduleItem> items) {
        this.schedule = items;
    }
}
