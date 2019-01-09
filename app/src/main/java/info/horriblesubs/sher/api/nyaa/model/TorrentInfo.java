package info.horriblesubs.sher.api.nyaa.model;

import java.util.List;

public class TorrentInfo extends TorrentPreview {
    private List<Comment> comments;
    private String submitter;
    private FileInfo files;
    private String info;
    private String hash;
    private String desc;


    public List<Comment> getComments() {
        return comments;
    }

    public String getDescription() {
        return this.desc;
    }

    public String getUploader() {
        return submitter;
    }

    public String getInformation() {
        return info;
    }

    public FileInfo getFiles() {
        return files;
    }

    public String getHash() {
        return hash;
    }
}
