package info.horriblesubs.sher.model.response;

import java.io.Serializable;
import java.util.List;

import info.horriblesubs.sher.model.base.LatestItem;
import info.horriblesubs.sher.model.base.Response;

public class SearchResponse implements Serializable {

    public Response response;
    public List<LatestItem> search;

    public SearchResponse() {

    }
}
