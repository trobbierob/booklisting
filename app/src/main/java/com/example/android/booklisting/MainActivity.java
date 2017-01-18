package com.example.android.booklisting;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;

public class MainActivity extends AppCompatActivity {

    private EditText mSearchEditText;

    private TextView mUrlDisplay;

    private TextView mResultsTextView;

    TextView mErrorMessage;

    private ListView listView;
    ArrayList<HashMap<String, String>> bookList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mSearchEditText = (EditText) findViewById(R.id.editText_query);

        bookList = new ArrayList<>();
        listView = (ListView) findViewById(R.id.list);

        //these values were removed once we changed the main layout to a ListView
        //mUrlDisplay = (TextView) findViewById(R.id.textView_of_url);
        //mResultsTextView = (TextView) findViewById(R.id.search_results);
        //mErrorMessage = (TextView) findViewById(R.id.tv_error_message_display);
    }

    private void makeBookSearchQuery() {
        String bookQuery = mSearchEditText.getText().toString();
        URL bookQueryURL = NetworkUtils.buildURL(bookQuery);
        mUrlDisplay.setText(bookQueryURL.toString());
        new BookQueryTask().execute(bookQueryURL);
    }

    private void showJsonDataView() {
        mErrorMessage.setVisibility(View.INVISIBLE);
        mResultsTextView.setVisibility(View.VISIBLE);
    }

    private void showErrorMessage() {
        mResultsTextView.setVisibility(View.INVISIBLE);
        mErrorMessage.setVisibility(View.VISIBLE);
    }




    public class BookQueryTask extends AsyncTask<String, Void, String[]> {

        @Override
        protected String[] doInBackground(String... urls) {

            if (urls.length == 0) {
                return null;
            }

            String location = urls[0];

            URL bookRequestUrl = NetworkUtils.buildURL(location);

            try {
                String jsonBookResponse = NetworkUtils
                        .getResponseFromHttpUrl(bookRequestUrl);

                String[] simpleJsonBookData = BookJsonUtils
                        .getStringsFromJson(MainActivity.this, jsonBookResponse);

                return simpleJsonBookData;

            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }

        // COMPLETED (7) Override the onPostExecute method to display the results of the network request
        @Override
        protected void onPostExecute(String[] bookData) {
            if (bookData != null) {

                for (String bookString : bookData) {
                    mResultsTextView.append((bookString) + "\n\n\n");
                }
            }
        }
    }

    /*
    public class BookQueryTask extends AsyncTask<URL, Void, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(URL... urls) {

            URL searchUrl = urls[0];
            String bookSearchResults = null;
            try {
                bookSearchResults = NetworkUtils.getResponseFromHttpUrl(searchUrl);
            } catch (IOException e) {
                e.printStackTrace();
            }
            return bookSearchResults;
        }

        @Override
        protected void onPostExecute(String bookSearchResults) {
            if (bookSearchResults != null && !bookSearchResults.equals("")) {
                showJsonDataView();
                mResultsTextView.setText(bookSearchResults);
            } else {
                showErrorMessage();

            }
        }
    }

    */

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int menuItemSelected = item.getItemId();
        if (menuItemSelected == R.id.action_bar_search) {
            makeBookSearchQuery();
            Toast.makeText(getBaseContext(), "Search", Toast.LENGTH_SHORT).show();
        }
        return super.onOptionsItemSelected(item);
    }
}