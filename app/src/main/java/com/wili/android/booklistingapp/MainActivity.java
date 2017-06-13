package com.wili.android.booklistingapp;

import android.app.LoaderManager;
import android.content.Context;
import android.content.Loader;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<List<BookItem>> {
    private static final String LOG_TAG = MainActivity.class.getSimpleName();
    private static final int LOADER_ID = 1;
    private static final String BOOKS_URL = "https://www.googleapis.com/books/v1/volumes?q=";
    @BindView(R.id.search_text)
    EditText searchText;
    @BindView(R.id.search_button)
    Button searchButton;
    @BindView(R.id.list_view)
    ListView listView;
    @BindView(R.id.empty_view)
    TextView emptyView;
    @BindView(R.id.progress_bar)
    ProgressBar progressBar;
    private String searchUrl;
    private BooksAdapter booksAdapter;
    private LoaderManager loaderManager;
    private boolean isClicked = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        booksAdapter = new BooksAdapter(this, new ArrayList<BookItem>());
        listView.setEmptyView(emptyView);
        listView.setAdapter(booksAdapter);
        progressBar.setVisibility(View.GONE);
        if (isConnected()) {
            loaderManager = getLoaderManager();
            loaderManager.initLoader(LOADER_ID, null, this);
        } else {
            if (isClicked == false) {
                emptyView.setText(R.string.populate_list);
            } else
                emptyView.setText(R.string.no_connection);
        }

        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isClicked = true;
                if (isConnected())
                    search();
                else
                    Toast.makeText(MainActivity.this, R.string.no_connection, Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public Loader<List<BookItem>> onCreateLoader(int id, Bundle args) {
        return new BookLoader(this, searchUrl);
    }

    @Override
    public void onLoadFinished(Loader<List<BookItem>> loader, List<BookItem> data) {
        progressBar.setVisibility(View.GONE);
        if (isClicked == true)
            emptyView.setText(R.string.not_found);
        else
            emptyView.setText(R.string.populate_list);
        booksAdapter.clear();
        if (data != null) {
            booksAdapter.addAll(data);
            String foundItems = getResources().getString(R.string.found) + data.size();
            Toast.makeText(this, foundItems, Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onLoaderReset(Loader<List<BookItem>> loader) {
        loader.reset();
    }

    private void search() {
        emptyView.setVisibility(View.GONE);
        progressBar.setVisibility(View.VISIBLE);
        String keywords = searchText.getText().toString();
        keywords = keywords.trim();
        keywords = keywords.replaceAll(" ", "+");
        searchUrl = null;
        searchUrl = BOOKS_URL + keywords;
        Log.e(MainActivity.class.getSimpleName(), searchUrl);
        loaderManager.restartLoader(LOADER_ID, null, this);
    }

    private boolean isConnected() {
        ConnectivityManager cm = (ConnectivityManager) getBaseContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        boolean isConnected = activeNetwork != null && activeNetwork.isConnectedOrConnecting();
        return isConnected;
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBoolean(getString(R.string.isClicked), isClicked);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        isClicked = savedInstanceState.getBoolean(getString(R.string.isClicked));
    }
}
