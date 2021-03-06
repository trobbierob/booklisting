package com.example.android.booklisting;

import android.net.Uri;
import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Scanner;

public class NetworkUtils {

    private static final String TAG = MainActivity.class.getSimpleName();

    final static String GOOGLE_BOOKS_URL =
            "https://www.googleapis.com/books/v1/volumes";

    final static String PARAM_QUERY = "q";

    final static String PARAM_RESULTS = "maxResults";

    final static String PARAM_RESULT_NUM = "5";


    public static URL buildURL(String bookSearchQuery) {
        Uri builtUri = Uri.parse(GOOGLE_BOOKS_URL).buildUpon()
                .appendQueryParameter(PARAM_QUERY, bookSearchQuery)
                .appendQueryParameter(PARAM_RESULTS, PARAM_RESULT_NUM)
                .build();



        URL url = null;
        try {
            url = new URL(builtUri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        return url;
    }

    public static String getResponseFromHttpUrl(URL url) throws IOException {

        Log.v(TAG, "the url is: " + url);

        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
        try {
            InputStream inputStream = urlConnection.getInputStream();

            Scanner scanner = new Scanner(inputStream);
            scanner.useDelimiter("\\A");

            boolean hasInput = scanner.hasNext();
            if (hasInput) {
                return scanner.next();
            } else {
                return null;
            }
        } finally {
            urlConnection.disconnect();
        }
    }
}
