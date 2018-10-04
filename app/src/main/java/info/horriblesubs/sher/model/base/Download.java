package info.horriblesubs.sher.model.base;

import java.io.Serializable;
import java.util.List;

public class Download implements Serializable {

    public String quality;
    public List<Links> links;

    Download() {
    }
}
