package com.wili.android.booklistingapp;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.text.TextUtils;
import android.util.Log;

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

/**
 * Created by Damian on 5/30/2017.
 */

public class Utils {
    public static final String LOG_TAG = Utils.class.getSimpleName();

    private Utils() {
    }

    public static ArrayList<BookItem> fetchData(String requestURL) {
        URL url = createURL(requestURL);
        String jsonResponse = null;
        try {
            jsonResponse = makeHttpRequest(url);
        } catch (IOException e) {
            Log.e(LOG_TAG, "Error creating jsonResponse", e);
        }
        return extractFeaturesFromJson(jsonResponse);
    }

    public static Bitmap createBitmap(String requestURL) {
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

    private static ArrayList<BookItem> extractFeaturesFromJson(String jsonResponse) {
        ArrayList<BookItem> bookList = new ArrayList<BookItem>();
        if (TextUtils.isEmpty(jsonResponse))
            return null;

        try {
            JSONObject root = new JSONObject(jsonResponse);
            JSONArray items = root.getJSONArray("items");
            for (int i = 0; i < items.length(); i++) {
                JSONObject currItem = items.getJSONObject(i);
                JSONObject volumeInfo = currItem.getJSONObject("volumeInfo");
                JSONArray authorsArray = volumeInfo.getJSONArray("authors");
                StringBuilder authors = new StringBuilder();
                for (int a = 0; a < authorsArray.length(); a++) {
                    authors.append(authorsArray.getString(a));
                }
                String title = volumeInfo.getString("title");
                String subtitle = volumeInfo.optString("subtitle");
                int pubDate = volumeInfo.getInt("publishedDate");
                String description = volumeInfo.getString("description");
                JSONObject imageLinks = volumeInfo.getJSONObject("imageLinks");
                String thumbnailURL = volumeInfo.getString("smallThumbnail");
                bookList.add(new BookItem(title, subtitle, authors.toString(), pubDate, thumbnailURL, description));
            }
        } catch (JSONException e) {
            Log.e(LOG_TAG, "Problem parsing the json results", e);
        }
        return bookList;
    }
}
