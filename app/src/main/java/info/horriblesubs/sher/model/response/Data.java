package info.horriblesubs.sher.model.response;

import java.io.Serializable;
import java.util.List;

import info.horriblesubs.sher.model.base.Item;
import info.horriblesubs.sher.model.base.LatestItem;
import info.horriblesubs.sher.model.base.PageItem;
import info.horriblesubs.sher.model.base.ScheduleItem;

public class Data implements Serializable {

    public List<Item> allShows;
    public List<Item> currentShows;
    public List<PageItem> favouriteItems;
    public List<LatestItem> latestReleases;
    public List<ScheduleItem> scheduleItems;

    public Data() {

    }
}