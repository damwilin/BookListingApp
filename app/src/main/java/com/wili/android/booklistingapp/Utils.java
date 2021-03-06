package com.wili.android.booklistingapp;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;

import static com.wili.android.booklistingapp.R.id.authors;

/**
 * Created by Damian on 5/30/2017.
 */

public class Utils {
    private static final String KEY_ITEMS = "items";
    private static final String KEY_VOLUMEINFO = "volumeInfo";
    private static final String KEY_AUTHORS = "authors";
    private static final String KEY_TITLE = "title";
    private static final String KEY_SUBTITLE = "subtitle";
    private static final String KEY_PUBLISHED_DATE = "publishedDate";
    private static final String KEY_DESCRIPTION = "description";
    private static final String KEY_IMAGE_LINKS = "imageLinks";
    private static final String KEY_SMALL_THUMBNAIL = "smallThumbnail";
    private static final String LOG_TAG = Utils.class.getSimpleName();

    private Utils() {
    }

    public static ArrayList<BookItem> fetchData(String requestURL, Context context) {
        URL url = createURL(requestURL);
        String jsonResponse = null;
        try {
            jsonResponse = makeHttpRequest(url);
        } catch (IOException e) {
            Log.e(LOG_TAG, "Error creating jsonResponse", e);
        }
        return extractFeaturesFromJson(jsonResponse, context);
    }

    private static Bitmap createBitmap(String requestURL) {
        URL url = createURL(requestURL);
        Bitmap bm = null;
        try {
            bm = BitmapFactory.decodeStream(url.openConnection().getInputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return bm;
    }


    /**
     * Create URL from given param
     *
     * @param stringURL
     * @return
     */
    private static URL createURL(String stringURL) {
        URL url = null;
        try {
            url = new URL(stringURL);
        } catch (MalformedURLException e) {
            Log.e(LOG_TAG, "Error with creating URL", e);
        }
        return url;
    }

    /**
     * @param url make HTTP request to given URL
     * @return jsonResponse
     */
    private static String makeHttpRequest(URL url) throws IOException {
        String jsonResponse = "";
        if (url == null)
            return jsonResponse;
        HttpURLConnection urlConnection = null;
        InputStream inputStream = null;
        try {
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setReadTimeout(10000);
            urlConnection.setConnectTimeout(15000);
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();
            if (urlConnection.getResponseCode() == 200) {
                inputStream = urlConnection.getInputStream();
                jsonResponse = readFromStream(inputStream);
            }
        } catch (IOException e) {
            Log.e(LOG_TAG, "Problem retrieving the JSON Result");
        } finally {
            if (urlConnection != null)
                urlConnection.disconnect();
            if (inputStream != null)
                inputStream.close();
        }
        return jsonResponse;
    }

    /**
     * @param inputStream
     * @return jsonResponse
     * @throws IOException
     */
    private static String readFromStream(InputStream inputStream) throws IOException {
        StringBuilder output = new StringBuilder();
        if (inputStream != null) {
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream, Charset.forName("UTF-8"));
            BufferedReader reader = new BufferedReader(inputStreamReader);
            String line = reader.readLine();
            while (line != null) {
                output.append(line);
                line = reader.readLine();
            }
        }
        return output.toString();
    }

    private static ArrayList<BookItem> extractFeaturesFromJson(String jsonResponse, Context context) {
        ArrayList<BookItem> bookList = new ArrayList<BookItem>();
        if (TextUtils.isEmpty(jsonResponse))
            return null;

        try {
            JSONObject root = new JSONObject(jsonResponse);
            JSONArray items = root.getJSONArray(KEY_ITEMS);
            if (items == null)
                return null;
            for (int i = 0; i < items.length(); i++) {
                JSONObject currItem = items.getJSONObject(i);
                JSONObject volumeInfo = currItem.getJSONObject(KEY_VOLUMEINFO);
                StringBuilder authors = new StringBuilder();
                if (volumeInfo.has(KEY_AUTHORS)) {
                    JSONArray authorsArray = volumeInfo.getJSONArray(KEY_AUTHORS);
                    for (int a = 0; a < authorsArray.length(); a++) {
                        authors.append(authorsArray.getString(a));
                        authors.append(" ");
                    }
                } else
                    authors.append("Author N/A");

                String title = volumeInfo.optString(KEY_TITLE);
                String subtitle = volumeInfo.optString(KEY_SUBTITLE);
                String pubDate = volumeInfo.optString(KEY_PUBLISHED_DATE);
                String description = volumeInfo.optString(KEY_DESCRIPTION);
                JSONObject imageLinks = volumeInfo.getJSONObject(KEY_IMAGE_LINKS);
                String thumbnailURL = imageLinks.optString(KEY_SMALL_THUMBNAIL);
                Bitmap thumbnailBm = createBitmap(thumbnailURL);
                bookList.add(new BookItem(title, subtitle, authors.toString().trim(), pubDate, thumbnailBm, description));
            }
        } catch (JSONException e) {
            Log.e(LOG_TAG, "Problem parsing the json results", e);
        }
        return bookList;
    }
}
