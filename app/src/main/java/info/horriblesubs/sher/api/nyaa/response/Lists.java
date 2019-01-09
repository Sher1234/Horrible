package info.horriblesubs.sher.api.nyaa.response;

import java.util.List;

import info.horriblesubs.sher.api.nyaa.model.TorrentPreview;

public class Lists {
    private List<TorrentPreview> items;
    private String message;

    public Lists() {
    }

    public List<TorrentPreview> getItems() {
        return items;
    }

    public String getMessage() {
        return this.message;
    }
}
