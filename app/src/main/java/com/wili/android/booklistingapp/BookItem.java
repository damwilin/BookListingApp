package com.wili.android.booklistingapp;

/**
 * Created by Damian on 5/30/2017.
 */

public class BookItem {
    private String title;
    private String subtitle;
    private String authors;
    private String thumbnailURL;
    private String description;
    private int publishedDate;

    public BookItem(String title, String subtitle, String authors, int publishedDate, String thumbnailURL, String description) {
        this.title = title;
        this.subtitle = subtitle;
        this.authors = authors;
        this.publishedDate = publishedDate;
        this.thumbnailURL = thumbnailURL;
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

    public String getThumbnailURL() {
        return thumbnailURL;
    }

    public String getDescription() {
        return description;
    }

    public int getPublishedDate() {
        return publishedDate;
    }
}
