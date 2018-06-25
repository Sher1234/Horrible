package info.horriblesubs.sher.model.response;

import java.io.Serializable;
import java.util.List;

import info.horriblesubs.sher.model.base.PageItem;
import info.horriblesubs.sher.model.base.ReleaseItem;
import info.horriblesubs.sher.model.base.Response;
import info.horriblesubs.sher.model.base.ScheduleItem;

public class ShowResponse implements Serializable {

    public PageItem detail;
    public Response response;
    public List<ReleaseItem> allSubs;
    public List<ScheduleItem> schedule;
    public List<ReleaseItem> allBatches;

    public ShowResponse() {

    }
}
