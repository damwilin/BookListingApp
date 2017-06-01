package com.wili.android.booklistingapp;

import android.content.AsyncTaskLoader;
import android.content.Context;

import java.util.List;

/**
 * Created by Damian on 6/1/2017.
 */

public class BookLoader extends AsyncTaskLoader<List<BookItem>> {
    private static String LOG_TAG = BookLoader.class.getSimpleName();
    private String url;

    public BookLoader(Context context, String url) {
        super(context);
        this.url = url;
    }

    @Override
    protected void onStartLoading() {
        forceLoad();
    }

    @Override
    public List<BookItem> loadInBackground() {
        if (url == null)
            return null;
        return Utils.fetchData(url);
    }
}
