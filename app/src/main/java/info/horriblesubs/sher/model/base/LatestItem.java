package info.horriblesubs.sher.model.base;

import com.google.android.gms.common.util.ArrayUtils;

import java.util.List;

@SuppressWarnings("all")
public class LatestItem extends Item {

    public String number;
    public List<String> badge;

    public LatestItem() {
        super();
    }

    @Override
    public String toString() {
        return "ID: " + this.id +
                "\n" + "Link: " + this.link +
                "\n" + "Title: " + this.title +
                "\n" + "Number: " + this.number+
                "\n" + "Badges: " + ArrayUtils.toStringArray(this.badge);
    }
}