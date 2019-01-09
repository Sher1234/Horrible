package info.horriblesubs.sher.api.nyaa.model;

import androidx.annotation.Nullable;

import java.util.Date;

public class TorrentPreview extends ParseEnum {

    public String category;
    public String torrent;
    public int completed;
    public String magnet;
    public int leechers;
    public int seeders;
    public int trusted;
    public String date;
    public String size;
    public String name;
    public String id;

    public TorrentPreview() {
    }

    public TorrentCategory getCategory() {
        return parseCategory(this.category);
    }

    public String getMagnet() {
        return this.magnet;
    }

    public String getCompleted() {
        return String.valueOf(this.completed);
    }

    public String getSeeders() {
        return String.valueOf(this.seeders);
    }

    public String getLeechers() {
        return String.valueOf(this.leechers);
    }

    public TorrentState getTorrentState() {
        return parseState(this.trusted);
    }

    public String getSize() {
        return this.size;
    }

    public String getTitle() {
        return this.name;
    }

    public String getId() {
        return this.id;
    }

    public String getTorrent() {
        return this.torrent;
    }

    @Nullable
    public Date getDate() {
        return parseDate(this.date);
    }

    @Nullable
    public String getFullDate() {
        return parseDateTimeView(this.date);
    }

    @Nullable
    public String getTime() {
        return parseTimeView(this.date);
    }
}
