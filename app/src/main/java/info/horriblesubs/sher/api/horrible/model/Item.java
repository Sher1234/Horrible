package info.horriblesubs.sher.api.horrible.model;

import org.jetbrains.annotations.NotNull;

import java.io.Serializable;

public class Item extends DateParse implements Serializable {

    public String title;
    public String link;
    public String id;

    @NotNull
    @Override
    public String toString() {
        return "Title: " + this.title +
                "\n" + "ID: " + this.id +
                "\n" + "Link: " + this.link;
    }
}
