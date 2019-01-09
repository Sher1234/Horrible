package info.horriblesubs.sher.api.horrible.model;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public class ListItem extends Item {

    public List<Boolean> quality;
    public String release;

    public ListItem() {
        super();
    }

    @NotNull
    @Override
    public String toString() {
        return super.toString() +
                "\n" + "Release: " + this.release +
                "\n" + "Quality: " + this.quality.size();
    }
}