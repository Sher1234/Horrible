package info.horriblesubs.sher.api.horrible.response;

import java.util.Date;
import java.util.List;

import info.horriblesubs.sher.api.horrible.model.DateParse;
import info.horriblesubs.sher.api.horrible.model.Item;

public class ShowsItems {
    public List<Item> current;
    public List<Item> all;
    private String time;

    public Date getTime() {
        return DateParse.getNetworkDate(time);
    }

    public ShowsItems() {
    }
}
