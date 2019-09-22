package info.horriblesubs.sher.api.horrible.model;

import org.jetbrains.annotations.NotNull;

import java.io.Serializable;
import java.util.Date;

public class ShowDetail extends Item implements Serializable {

    public String image;
    private String time;
    public String body;
    public String sid;
    public String mal_id;
    public boolean featured;

    public ShowDetail() {
    }

    public Date getTime() {
        return getNetworkDate(time + " +00:00");
    }

    public String time() {
        return getShowShort(time + " +00:00");
    }

    public String time2() {
        return getNetworkDate(getTime());
    }

    @NotNull
    @Override
    public String toString() {
        return super.toString() +
                "\n" + "Image: " + this.image +
                "\n" + "ShowID: " + this.sid +
                "\n" + "Time: " + this.time +
                "\n" + "Body: " + this.body;
    }
}