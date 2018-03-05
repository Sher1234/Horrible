package info.horriblesubs.sher.model;

import java.io.Serializable;

@SuppressWarnings("all")
public class Item implements Serializable {

    public String nId;
    public String link;
    public String title;


    public Item(String nId, String link, String title) {
        this.nId = nId;
        this.link = link;
        this.title = title;
    }

    @Override
    public String toString() {
        return "NotificationRequest ID: " + this.nId +
                "\n" + "Title: " + this.title +
                "\n" + "Link: " + this.link;
    }
}
