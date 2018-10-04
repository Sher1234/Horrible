package info.horriblesubs.sher.model.base;

import java.io.Serializable;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import androidx.annotation.Nullable;
import info.horriblesubs.sher.common.Strings;

@SuppressWarnings("All")
public class Update implements Serializable {

    public String Link;
    public int Version;
    public String AppCode;
    public String UpdateID;
    public String Changelog;
    public String ReleaseDate;

    @Nullable
    private Date getDate() {
        DateFormat dateFormat = new SimpleDateFormat(Strings.ServerDate, Locale.US);
        try {
            return dateFormat.parse(this.ReleaseDate);
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
    }

    public String getViewDate() {
        return (new SimpleDateFormat(Strings.ViewDate, Locale.US)).format(getDate());
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
