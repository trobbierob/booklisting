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
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = MainActivity.class.getSimpleName();

    private EditText mSearchEditText;

    private TextView mUrlDisplay;

    private TextView mResultsTextView;

    private String authors = "";

    TextView mErrorMessage;

    public String jsonString;
    public URL bookQueryUrl;

    private ListView listView;
    private ArrayList<HashMap<String, String>> bookList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mSearchEditText = (EditText) findViewById(R.id.editText_query);

        bookList = new ArrayList<>();
        listView = (ListView) findViewById(R.id.list);
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
            bookList.clear();
            new BookQueryTask().execute();
            Toast.makeText(getBaseContext(), "Search was clicked", Toast.LENGTH_SHORT).show();
        }
        return super.onOptionsItemSelected(item);
    }

    public class BookQueryTask extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            String bookQuery = mSearchEditText.getText().toString();
            bookQueryUrl = NetworkUtils.buildURL(bookQuery);
        }

        @Override
        protected Void doInBackground(Void... urls) {

            if (bookQueryUrl != null) {
                try {
                    jsonString = NetworkUtils.getResponseFromHttpUrl(bookQueryUrl);
                    JSONObject jsonBookRootObject = new JSONObject(jsonString);
                    JSONArray itemsArray = jsonBookRootObject.optJSONArray("items");

                    for (int i = 0; i < itemsArray.length(); i++) {
                        JSONObject jsonObject = itemsArray.getJSONObject(i);
                        JSONObject volumeInfo = jsonObject.getJSONObject("volumeInfo");
                        String title = volumeInfo.optString("title").toString();
                        JSONArray authorsArray = volumeInfo.getJSONArray("authors");
                        String description = volumeInfo.optString("description").toString();

                        for (int j = 0; j < authorsArray.length(); j++) {
                            String authorsNames = authorsArray.getString(j);
                            authors = authors.concat(authorsNames + "   ");
                        }

                        HashMap<String, String> book = new HashMap<>();
                        book.put("title", title);
                        book.put("description", description);
                        book.put("authors", authors);
                        bookList.add(book);
                        /*
                            @param authors is assigned an empty String so that the previous
                            book's author names do not concatenate into the next book
                         */
                        authors = "";
                    }

                } catch (IOException e) {
                    Log.e(TAG, "IOException at " + e);
                } catch (JSONException e) {
                    Log.e(TAG, "JSONException at " + e);
                }

            } else {
                Log.e(TAG, "Could not get json from server.");
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            ListAdapter adapter = new SimpleAdapter(MainActivity.this, bookList,
                    R.layout.list_item, new String[]{"title", "description", "authors"},
                    new int[]{R.id.title, R.id.description, R.id.authors});
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