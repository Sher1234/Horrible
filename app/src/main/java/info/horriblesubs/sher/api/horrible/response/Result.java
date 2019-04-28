package info.horriblesubs.sher.api.horrible.response;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import info.horriblesubs.sher.api.horrible.model.DateParse;

public class Result<T> implements Serializable {

    public List<T> items;
    public String time;

    public Date getDate() {
        return DateParse.getNetworkDate(time + " +00:00");
    }

    public String getTime() {
        return DateParse.getNetworkDate(getDate());
    }

    public Result() {
    }
}
