package info.horriblesubs.sher.api.nyaa.model;

import androidx.annotation.DrawableRes;

import java.util.List;

import info.horriblesubs.sher.R;

public class FileInfo extends ParseEnum {

    private List<FileInfo> files;
    private boolean file;
    private String name;
    private String size;

    public DataSize getSize() {
        return parseSize(this.size);
    }

    public List<FileInfo> getFiles() {
        return files;
    }

    public boolean isFile() {
        return this.file;
    }

    public String getName() {
        return name;
    }

    @DrawableRes
    public int getIcon() {
        if (this.file) return R.drawable.ic_notification_new;
        return R.drawable.ic_notifications_active;
    }
}
