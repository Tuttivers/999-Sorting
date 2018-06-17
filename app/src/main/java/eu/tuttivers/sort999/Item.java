package eu.tuttivers.sort999;


public class Item {

    String photoURL;
    String name;
    String price;
    String link;

    Item(String photoURL, String name, String price, String link) {
        this.photoURL = photoURL;
        this.name = name;
        this.price = price;
        this.link = link;
    }

    public long getLongPrice() {
        try {
            return Long.parseLong(price.replaceAll("[^0-9]", ""));
        } catch (Exception ignored) {
        }
        return -1;
    }

}
