package info.horriblesubs.sher.model.response;

import java.io.Serializable;
import java.util.List;

import info.horriblesubs.sher.model.base.FeaturedItem;
import info.horriblesubs.sher.model.base.LatestItem;
import info.horriblesubs.sher.model.base.Response;
import info.horriblesubs.sher.model.base.ScheduleItem;
import info.horriblesubs.sher.model.base.Update;

public class HomeResponse implements Serializable {

    public Update update;
    public Response response;
    public List<LatestItem> subs;
    public List<ScheduleItem> schedule;
    public List<FeaturedItem> featuredItems;

    public HomeResponse() {

    }
}