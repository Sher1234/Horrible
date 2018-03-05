package info.horriblesubs.sher.model;

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

    @Override
    public String toString() {
        return "ID: " + this.id +
                "\n" + "NotificationRequest ID: " + this.nId +
                "\n" + "Title: " + this.title +
                "\n" + "Link: " + this.link +
                "\n" + "Image: " + this.image +
                "\n" + "Body: " + this.body;
    }
}