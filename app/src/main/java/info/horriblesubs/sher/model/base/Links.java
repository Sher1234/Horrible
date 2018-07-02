package info.horriblesubs.sher.model.base;

import java.io.Serializable;

public class Links implements Serializable {

    public String type;
    public String link;


    Links() {
    }

    @Override
    public String toString() {
        return "Type: " + this.type +
                "\n" + "Link: " + this.link;
    }
}
