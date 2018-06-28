package info.horriblesubs.sher.model.response;

import java.io.Serializable;
import java.util.List;

import info.horriblesubs.sher.model.base.Response;
import info.horriblesubs.sher.model.base.Update;

public class UpdatesResponse implements Serializable {

    public Update update;
    public Response response;
    public List<Update> updates;

    public UpdatesResponse() {

    }
}