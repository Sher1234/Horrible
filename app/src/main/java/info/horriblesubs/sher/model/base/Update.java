package info.horriblesubs.sher.model.base;

import android.annotation.SuppressLint;
import android.support.annotation.Nullable;

import java.io.Serializable;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import info.horriblesubs.sher.Api;

@SuppressWarnings("All")
public class Update implements Serializable {

    public String Link;
    public int Version;
    public String AppCode;
    public String UpdateID;
    public String Changelog;
    public String ReleaseDate;

    @Nullable
    @SuppressLint("SimpleDateFormat")
    private Date getDate() {
        DateFormat dateFormat = new SimpleDateFormat(Api.ServerDate);
        try {
            return dateFormat.parse(this.ReleaseDate);
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
    }

    @SuppressLint("SimpleDateFormat")
    public String getViewDate() {
        return (new SimpleDateFormat(Api.ViewDate)).format(getDate());
    }

    @Override
    public String toString() {
        return "AppCode: " + this.AppCode +
                "\n" + "Version: " + this.Version +
                "\n" + "UpdateID: " + this.UpdateID +
                "\n" + "Changelog: " + this.Changelog +
                "\n" + "ReleaseDate: " + this.getViewDate() +
                "\n" + "Link: " + this.Link;
    }
}
