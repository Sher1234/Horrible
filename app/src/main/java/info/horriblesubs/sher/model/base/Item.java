package info.horriblesubs.sher.model.base;

import java.io.Serializable;

public class Item implements Serializable {

    public String id;
    public String link;
    public String title;


    public Item(String id, String link, String title) {
        this.id = id;
        this.link = link;
        this.title = title;
    }

    @Override
    public String toString() {
        return "ID: " + this.id +
                "\n" + "Title: " + this.title +
                "\n" + "Link: " + this.link;
    }
}
