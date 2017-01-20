package com.example.android.booklisting;

import android.net.Uri;
import android.util.Log;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.Scanner;

/**
 * Created by Sogekingu on 1/15/17.
 */

public class NetworkUtils {

    private static final String TAG = MainActivity.class.getSimpleName();

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




    public String makeServiceCall(String reqUrl) {
        String response = null;

        try{
            URL url = new URL(reqUrl);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");

            InputStream in = new BufferedInputStream(conn.getInputStream());
            response = convertStreamToString(in);
        } catch (MalformedURLException e) {

        } catch (ProtocolException e) {

        } catch (IOException e) {

        } catch (Exception e) {

        }
        return response;
    }

    private String convertStreamToString(InputStream is) {
        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        StringBuilder sb = new StringBuilder();

        String line;
        try {
            while ((line = reader.readLine()) != null) {
                sb.append(line).append('\n');
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                is.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return sb.toString();
    }


















}
