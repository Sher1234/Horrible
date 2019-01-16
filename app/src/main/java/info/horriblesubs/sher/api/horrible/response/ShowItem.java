package info.horriblesubs.sher.api.horrible.response;

import androidx.annotation.NonNull;

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

    @NonNull
    @Override
    public String toString() {
        return "Episodes: " + (episodes == null ? "null" : episodes.size()) + "\n" +
                (detail != null ? detail.toString() : "Details:Null") + "\n" +
                "Batches: " + (batches == null ? "null" : batches.size());
    }
}
