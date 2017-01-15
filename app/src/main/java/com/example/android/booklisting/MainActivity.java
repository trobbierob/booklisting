package com.example.android.booklisting;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.net.URL;

public class MainActivity extends AppCompatActivity {

    private EditText mSearchEditText;

    private TextView mUrlDisplay;

    private TextView mResultsTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mSearchEditText = (EditText) findViewById(R.id.editText_query);

        mUrlDisplay = (TextView) findViewById(R.id.textView_of_url);

        mResultsTextView = (TextView) findViewById(R.id.search_results);
    }

    private void makeBookSearchQuery() {

        String bookQuery = mSearchEditText.getText().toString();
        URL bookQueryURL = Network.buildURL(bookQuery);
        mUrlDisplay.setText(bookQueryURL.toString());
        new BookQueryTask().execute(bookQueryURL);

    }

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
                bookSearchResults = Network.getResponseFromHttpUrl(searchUrl);
            } catch (IOException e) {
                e.printStackTrace();
            }

            return bookSearchResults;
        }

        @Override
        protected void onPostExecute(String bookSearchResults) {
            if (bookSearchResults != null && !bookSearchResults.equals("")) {
                //showJsonDataView();
                mResultsTextView.setText(bookSearchResults);
            } else {

            }
        }

    }






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
