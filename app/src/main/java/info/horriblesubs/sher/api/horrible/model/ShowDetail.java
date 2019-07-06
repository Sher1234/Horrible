package info.horriblesubs.sher.api.horrible.model;

import org.jetbrains.annotations.NotNull;

import java.io.Serializable;
import java.util.Date;

public class ShowDetail extends Item implements Serializable {

    public String image;
    private String time;
    public String body;
    public String sid;
    public int views;
    public int favs;


    public ShowDetail() {
    }

    public Date getTime() {
        return getNetworkDate(time + " +00:00");
    }

    public String time() {
        return getShowShort(time + " +00:00");
    }

    public String time2() {
        return getNetworkDate(getNetworkDate(time + " +00:00"));
    }

    @NotNull
    @Override
    public String toString() {
        return super.toString() +
                "\n" + "Image: " + this.image +
                "\n" + "Views: " + this.views +
                "\n" + "ShowID: " + this.sid +
                "\n" + "Time: " + this.time +
                "\n" + "Favs: " + this.favs +
                "\n" + "Body: " + this.body;
    }
}