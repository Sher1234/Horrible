package info.horriblesubs.sher.api.horrible.model;

import org.jetbrains.annotations.NotNull;

import java.io.Serializable;

public class Item extends DateParse implements Serializable {

    public float rating;
    public int views;
    public int users;
    public int favs;

    public String title;
    public String link;
    public String id;

    @NotNull
    @Override
    public String toString() {
        return "Title: " + this.title +
                "\n" + "ID: " + this.id +
                "\n" + "Views: " + this.views +
                "\n" + "Favs: " + this.favs +
                "\n" + "Users: " + this.users +
                "\n" + "Rating: " + this.rating +
                "\n" + "Link: " + this.link;
    }
}
