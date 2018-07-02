package info.horriblesubs.sher.model.response;

import java.io.Serializable;
import java.util.List;

import info.horriblesubs.sher.model.base.PageItem;
import info.horriblesubs.sher.model.base.ReleaseItem;
import info.horriblesubs.sher.model.base.Response;

public class ShowResponse implements Serializable {

    public PageItem detail;
    public Response response;
    public List<ReleaseItem> subs;
    public List<ReleaseItem> batches;

    public ShowResponse() {

    }
}
