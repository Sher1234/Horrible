package info.horriblesubs.sher.api.horrible.model;

import org.jetbrains.annotations.NotNull;

import java.io.Serializable;
import java.util.List;

@SuppressWarnings("all")
public class ShowRelease extends ListItem {

    public List<List<Download>> downloads;

    ShowRelease() {
    }

    @NotNull
    @Override
    public String toString() {
        return super.toString() + "\n" + "Downloads: " + this.downloads.size();
    }

    public class Download implements Serializable {

        public String source;
        public String link;

        Download() {
        }

        @NotNull
        @Override
        public String toString() {
            return "Link: " + this.link +
                    "\n" + "Source: " + this.source;
        }
    }
}