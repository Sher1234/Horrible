package info.horriblesubs.sher.model.base;

@SuppressWarnings("all")
public class PageItem extends Item {

    public String sid;
    public String body;
    public String image;

    @Override
    public String toString() {
        return "ShowID: " + this.sid +
                "\n" + "ID: " + this.id +
                "\n" + "Title: " + this.title +
                "\n" + "Link: " + this.link +
                "\n" + "Image: " + this.image +
                "\n" + "Body: " + this.body;
    }
}