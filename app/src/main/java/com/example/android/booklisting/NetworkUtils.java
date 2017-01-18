package com.example.android.booklisting;

import android.net.Uri;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Scanner;

/**
 * Created by Sogekingu on 1/15/17.
 */

public class NetworkUtils {

    final static String GOOGLE_BOOKS_URL =
            "https://www.googleapis.com/books/v1/volumes";

    final static String PARAM_QUERY = "q";

    final static String PARAM_RESULTS = "maxResults";

    final static String PARAM_RESULT_NUM = "10";

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
