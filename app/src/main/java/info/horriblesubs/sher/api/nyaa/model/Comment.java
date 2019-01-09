package info.horriblesubs.sher.api.nyaa.model;

import java.util.Date;

public class Comment extends ParseEnum {
    private String id;
    private String username;
    private int trusted;
    private String avatar;
    private String date;
    private String content;

    public Comment() {
    }

    public TorrentState getState() {
        return parseState(this.trusted);
    }

    public String getFullDate() {
        return parseDateTimeView(date);
    }

    public String getTime() {
        return parseTimeView(date);
    }

    public Date getDate() {
        return parseDate(this.date);
    }

    public String getUsername() {
        return this.username;
    }

    public String getComment() {
        return this.content;
    }

    public String getCommentId() {
        return this.id;
    }

    public String getAvatar() {
        return avatar;
    }
}
