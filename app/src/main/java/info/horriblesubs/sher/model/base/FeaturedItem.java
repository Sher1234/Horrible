package info.horriblesubs.sher.model.base;

@SuppressWarnings("all")
public class FeaturedItem extends Item {

    public String image;
    public String number;

    public FeaturedItem() {
        super();
    }

    @Override
    public String toString() {
        return "ID: " + this.id +
                "\n" + "Link: " + this.link +
                "\n" + "Title: " + this.title +
                "\n" + "Number: " + this.number+
                "\n" + "Image: " + this.image;
    }
}