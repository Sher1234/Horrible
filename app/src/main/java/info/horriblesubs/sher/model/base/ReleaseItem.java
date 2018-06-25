package info.horriblesubs.sher.model.base;

@SuppressWarnings("all")
public class ReleaseItem extends Item {

    public String number;
    public String link480;
    public String link720;
    public String link1080;

    public ReleaseItem(String id, String link, String title, String number, String link480,
                       String link720, String link1080) {
        super(id, link, title);
        this.number = number;
        this.link480 = link480;
        this.link720 = link720;
        this.link1080 = link1080;
    }

    @Override
    public String toString() {
        return "ID: " + this.id +
                "\n" + "Link: " + this.link +
                "\n" + "Title: " + this.title +
                "\n" + "Number: " + this.number+
                "\n" + "Link 480p: " + this.link480 +
                "\n" + "Link 720p: " + this.link720 +
                "\n" + "Link 1080p: " + this.link1080;
    }
}