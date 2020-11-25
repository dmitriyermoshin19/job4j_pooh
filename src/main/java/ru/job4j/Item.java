package ru.job4j;

public class Item {
    private final String post;

    public Item(String post) {
        this.post = post;
    }

    public String getPost() {
        return post;
    }

    public Item copyOf() {
        return new Item(this.getPost());
    }
}
