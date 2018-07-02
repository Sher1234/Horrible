package info.horriblesubs.sher.model.base;

import java.io.Serializable;
import java.util.List;

public class Download implements Serializable {

    public String quality;
    public List<Links> links;

    Download() {
    }

    @Override
    public String toString() {
        return "Download: " + this.quality +
                "\n" + "Size: " + this.links.size();
    }
}
