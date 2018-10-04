package info.horriblesubs.sher.model.response;

import java.io.Serializable;
import java.util.List;

import info.horriblesubs.sher.model.base.Item;
import info.horriblesubs.sher.model.base.Response;

public class ShowsResponse implements Serializable {

    public List<Item> all;
    public Response response;
    public List<Item> current;

    public ShowsResponse() {
    }

    public ShowsResponse(List<Item> all, List<Item> current) {
        this.current = current;
        this.all = all;
    }
}