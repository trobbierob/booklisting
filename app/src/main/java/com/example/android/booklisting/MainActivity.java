package com.example.android.booklisting;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
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
import org.json.JSONObject;

import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = MainActivity.class.getSimpleName();

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
        mUrlDisplay = (TextView) findViewById(R.id.textView_of_url);
        //mResultsTextView = (TextView) findViewById(R.id.search_results);
        //mErrorMessage = (TextView) findViewById(R.id.tv_error_message_display);
    }

    private void makeBookSearchQuery() {
        String bookQuery = mSearchEditText.getText().toString();
        URL bookQueryURL = NetworkUtils.buildURL(bookQuery);
        mUrlDisplay.setText(bookQueryURL.toString());
        new BookQueryTask().execute(bookQuery);
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

    public class BookQueryTask extends AsyncTask<String, Void, Void> {

        @Override
        protected Void doInBackground(String... urls) {

            if (urls.length == 0) {
                //return null;
            }

            String bookQueryString = urls[0];

            URL bookRequestUrl = NetworkUtils.buildURL(bookQueryString);

            try {
                String jsonBookResponse = NetworkUtils
                        .getResponseFromHttpUrl(bookRequestUrl);

                JSONObject rootUrl = new JSONObject(jsonBookResponse);
                JSONArray itemsArray = rootUrl.getJSONArray("items");

                Log.v(TAG, "jsonBookResponse is " + jsonBookResponse);

                for (int i = 0; i < itemsArray.length(); i++) {

                    JSONObject volumeInfo = itemsArray.getJSONObject(4);
                    String title = volumeInfo.getString("title");

                    HashMap<String, String> book = new HashMap<>();

                    book.put("title", title);

                    bookList.add(book);
                }

            } catch (Exception e) {
                e.printStackTrace();
                //return null;
            }
            return null;
        }

        // COMPLETED (7) Override the onPostExecute method to display the results of the network request
        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);

            ListAdapter adapter = new SimpleAdapter(MainActivity.this, bookList,
                    R.layout.list_item, new String[]{"title"},
                    new int[]{R.id.title});
            listView.setAdapter(adapter);
        }
    }

    private void showJsonDataView() {
        mErrorMessage.setVisibility(View.INVISIBLE);
        mResultsTextView.setVisibility(View.VISIBLE);
    }

    private void showErrorMessage() {
        mResultsTextView.setVisibility(View.INVISIBLE);
        mErrorMessage.setVisibility(View.VISIBLE);
    }

}