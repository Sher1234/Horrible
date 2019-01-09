package info.horriblesubs.sher.api.nyaa.response;

import info.horriblesubs.sher.api.nyaa.model.TorrentInfo;

public class Item {
    private TorrentInfo page;
    private String message;

    public Item() {
    }

    public String getMessage() {
        return this.message;
    }

    public TorrentInfo getItems() {
        return page;
    }
}
