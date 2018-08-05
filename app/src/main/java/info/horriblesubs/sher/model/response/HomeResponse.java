package info.horriblesubs.sher.model.response;

import java.io.Serializable;
import java.util.List;

import info.horriblesubs.sher.model.base.LatestItem;
import info.horriblesubs.sher.model.base.PageItem;
import info.horriblesubs.sher.model.base.Response;
import info.horriblesubs.sher.model.base.ScheduleItem;

public class HomeResponse implements Serializable {

    public Response response;
    public List<LatestItem> subs;
    public List<PageItem> favourites;
    public List<ScheduleItem> schedule;

    public HomeResponse() {

    }
}