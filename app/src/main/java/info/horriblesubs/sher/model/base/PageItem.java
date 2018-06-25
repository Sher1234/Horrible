package info.horriblesubs.sher.model.base;

import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;

@SuppressWarnings("all")
public class PageItem extends Item {

    public String sid;
    public String body;
    public String image;

    public PageItem(String id, String link, String title, String image, String sid, String body) {
        super(id, link, title);
        this.sid = sid;
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
        return "ShowID: " + this.sid +
                "\n" + "ID: " + this.id +
                "\n" + "Title: " + this.title +
                "\n" + "Link: " + this.link +
                "\n" + "Image: " + this.image +
                "\n" + "Body: " + this.body;
    }

    public Map<String, String> getPageItem() {
        Map<String, String> strings = new HashMap<>();
        strings.put("id", id);
        strings.put("sid", sid);
        strings.put("link", link);
        strings.put("title", title);
        strings.put("image", image);
        return strings;
    }
}