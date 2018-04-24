package info.horriblesubs.sher.model;

import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;

@SuppressWarnings("all")
public class PageItem extends Item {

    public String id;
    public String body;
    public String image;

    public PageItem(String nId, String link, String title, String image, String id, String body) {
        super(nId, link, title);
        this.id = id;
        this.body = body;
        this.image = image;
    }

    public PageItem(@NotNull Map<String, String> map) {
        super(map.get("nId"), map.get("link"), map.get("title"));
        this.id = map.get("id");
        this.body = map.get("body");
        this.image = map.get("image");
    }

    @Override
    public String toString() {
        return "ID: " + this.id +
                "\n" + "NotificationRequest ID: " + this.nId +
                "\n" + "Title: " + this.title +
                "\n" + "Link: " + this.link +
                "\n" + "Image: " + this.image +
                "\n" + "Body: " + this.body;
    }

    public Map<String, String> getPageItem() {
        Map<String, String> strings = new HashMap<>();
        strings.put("id", id);
        strings.put("nId", nId);
        strings.put("title", title);
        strings.put("link", link);
        strings.put("image", image);
        return strings;
    }
}