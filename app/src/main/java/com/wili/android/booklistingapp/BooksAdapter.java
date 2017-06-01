package com.wili.android.booklistingapp;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Damian on 5/30/2017.
 */

public class BooksAdapter extends ArrayAdapter<BookItem> {
    private View listView;

    public BooksAdapter(Activity context, @NonNull ArrayList<BookItem> booksList) {
        super(context, 0, booksList);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        listView = convertView;
        if (listView == null)
            listView = LayoutInflater.from(getContext()).inflate(R.layout.book_item, parent, false);
        BookItem currBook = getItem(position);
        ViewHolder holder = new ViewHolder(listView);
        holder.authors.setText(currBook.getAuthors());
        holder.title.setText(currBook.getTitle());
        if (!currBook.getSubtitle().isEmpty())
            holder.subtitle.setText(currBook.getSubtitle());
        else
            holder.subtitle.setVisibility(View.GONE);
        holder.publishedDate.setText(currBook.getPublishedDate());
        holder.thumbnail.setImageBitmap(currBook.getThumbnail());
        return listView;
    }

    static class ViewHolder {
        @BindView(R.id.thumbnail)
        ImageView thumbnail;
        @BindView(R.id.title)
        TextView title;
        @BindView(R.id.subtitle)
        TextView subtitle;
        @BindView(R.id.authors)
        TextView authors;
        @BindView(R.id.published_date)
        TextView publishedDate;

        public ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
