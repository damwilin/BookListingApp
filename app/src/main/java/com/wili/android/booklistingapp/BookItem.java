package com.wili.android.booklistingapp;

/**
 * Created by Damian on 5/30/2017.
 */

public class BookItem {
    private String title;
    private String subtitle;
    private String authors;
    private String thumbnail;
    private String description;

    public BookItem(String title, String subtitle, String authors, String thumbnail, String description) {
        this.title = title;
        this.subtitle = subtitle;
        this.authors = authors;
        this.thumbnail = thumbnail;
        this.description = description;
    }

    public String getTitle() {
        return title;
    }

    public String getSubtitle() {
        return subtitle;
    }

    public String getAuthors() {
        return authors;
    }

    public String getThumbnail() {
        return thumbnail;
    }

    public String getDescription() {
        return description;
    }
}
