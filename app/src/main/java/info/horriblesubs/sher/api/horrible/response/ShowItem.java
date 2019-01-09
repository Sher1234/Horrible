package info.horriblesubs.sher.api.horrible.response;

import java.io.Serializable;
import java.util.List;

import info.horriblesubs.sher.api.horrible.model.ShowDetail;
import info.horriblesubs.sher.api.horrible.model.ShowRelease;

public class ShowItem implements Serializable {

    public List<ShowRelease> episodes;
    public List<ShowRelease> batches;
    public ShowDetail detail;

    public ShowItem() {
    }
}
