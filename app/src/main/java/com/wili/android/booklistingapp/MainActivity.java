package com.wili.android.booklistingapp;


import android.app.LoaderManager;
import android.content.Loader;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<List<BookItem>> {
    private static final int LOADER_ID = 1;
    private static final String BOOKS_URL = "https://www.googleapis.com/books/v1/volumes?q=";
    @BindView(R.id.search_text)
    EditText searchText;
    @BindView(R.id.search_button)
    Button searchButton;
    @BindView(R.id.list_view)
    ListView listView;
    private String searchUrl;
    private BooksAdapter booksAdapter;
    private LoaderManager loaderManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        booksAdapter = new BooksAdapter(this, new ArrayList<BookItem>());
        listView.setAdapter(booksAdapter);
        loaderManager = getLoaderManager();
        loaderManager.initLoader(LOADER_ID, null, this);
        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                search();
            }
        });
    }

    @Override
    public Loader<List<BookItem>> onCreateLoader(int id, Bundle args) {
        return new BookLoader(this, searchUrl);
    }

    @Override
    public void onLoadFinished(Loader<List<BookItem>> loader, List<BookItem> data) {
        booksAdapter.clear();
        if (data != null)
            booksAdapter.addAll(data);
    }

    @Override
    public void onLoaderReset(Loader<List<BookItem>> loader) {
        loader.reset();
    }

    private void search() {
        String keywords = searchText.getText().toString();
        keywords = keywords.replaceAll(" ", "+");
        searchUrl = null;
        searchUrl = BOOKS_URL + keywords;
        Log.e(MainActivity.class.getSimpleName(), searchUrl);
        loaderManager.restartLoader(LOADER_ID, null, this);
    }
}
