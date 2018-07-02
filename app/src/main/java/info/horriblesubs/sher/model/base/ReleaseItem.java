package info.horriblesubs.sher.model.base;

import java.io.Serializable;
import java.util.List;

@SuppressWarnings("all")
public class ReleaseItem implements Serializable {

    public String id;
    public String title;
    public String number;
    public List<String> badge;
    public List<Download> downloads;


    ReleaseItem() {
    }

    @Override
    public String toString() {
        return "ID: " + this.id +
                "\n" + "Title: " + this.title +
                "\n" + "Badge: " + this.badge.size() +
                "\n" + "Downloads: " + this.downloads.size() +
                "\n" + "Number: " + this.number;
    }
}