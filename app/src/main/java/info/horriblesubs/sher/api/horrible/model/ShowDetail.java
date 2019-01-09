package info.horriblesubs.sher.api.horrible.model;

import org.jetbrains.annotations.NotNull;

public class ShowDetail extends Item {

    public String image;
    public String body;
    public String sid;


    public ShowDetail() {
    }

    @NotNull
    @Override
    public String toString() {
        return super.toString() +
                "\n" + "ShowID: " + this.sid +
                "\n" + "Image: " + this.image +
                "\n" + "Body: " + this.body;
    }
}