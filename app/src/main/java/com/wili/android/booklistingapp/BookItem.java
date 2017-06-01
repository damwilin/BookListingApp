package com.wili.android.booklistingapp;

import android.graphics.Bitmap;

/**
 * Created by Damian on 5/30/2017.
 */

public class BookItem {
    private String title;
    private String subtitle;
    private String authors;
    private Bitmap thumbnail;
    private String description;
    private int publishedDate;

    public BookItem(String title, String subtitle, String authors, int publishedDate, Bitmap thumbnail, String description) {
        this.title = title;
        this.subtitle = subtitle;
        this.authors = authors;
        this.publishedDate = publishedDate;
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

    public Bitmap getThumbnail() {
        return thumbnail;
    }

    public String getDescription() {
        return description;
    }

    public int getPublishedDate() {
        return publishedDate;
    }
}
