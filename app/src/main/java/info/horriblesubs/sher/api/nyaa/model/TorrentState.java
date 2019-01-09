package info.horriblesubs.sher.api.nyaa.model;

public enum TorrentState {
    TRUSTED(1, "Trusted"),
    REMAKE(-1, "Remake"),
    NONE(0, null);

    private final String type;
    private final int id;

    TorrentState(int id, String type) {
        this.type = type;
        this.id = id;
    }

    public String getId() {
        return String.valueOf(id);
    }

    public String getType() {
        return type;
    }
}
