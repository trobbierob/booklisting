package com.example.android.booklisting;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
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
import android.widget.ProgressBar;
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
    private ProgressBar mLoadingIndicator;
    private String authors = "";
    private String title;
    private String description;
    private JSONArray authorsArray;
    private TextView mEmptyView;
    public String jsonString;
    public URL bookQueryUrl;
    private ListView listView;
    private ArrayList<HashMap<String, String>> bookList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mLoadingIndicator = (ProgressBar) findViewById(R.id.progress_bar);
        mSearchEditText = (EditText) findViewById(R.id.editText_query);
        bookList = new ArrayList<>();
        mEmptyView = (TextView) findViewById(R.id.empty);
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
            ConnectivityManager cm = (ConnectivityManager)
                    MainActivity.this.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
            if (activeNetwork != null) { // connected to the internet
                bookList.clear();
                new BookQueryTask().execute();
            } else { // not connected to the internet
                Toast.makeText(getBaseContext(), R.string.check_connection,
                        Toast.LENGTH_SHORT).show();
            }
        }
        return super.onOptionsItemSelected(item);
    }

    public class BookQueryTask extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            mLoadingIndicator.setVisibility(View.VISIBLE);
            String bookQuery = mSearchEditText.getText().toString();

            if (bookQuery.equals(null) || bookQuery.equals("")){
                mEmptyView.setVisibility(View.VISIBLE);
                listView.setVisibility(View.INVISIBLE);

            } else {
                mEmptyView.setVisibility(View.INVISIBLE);
                listView.setVisibility(View.VISIBLE);
                bookQueryUrl = NetworkUtils.buildURL(bookQuery);
            }
        }

        @Override
        protected Void doInBackground(Void... urls) {
            if (bookQueryUrl != null) {
                try {
                    jsonString = NetworkUtils.getResponseFromHttpUrl(bookQueryUrl);
                    JSONObject jsonBookRootObject = new JSONObject(jsonString);
                    JSONArray itemsArray = jsonBookRootObject.optJSONArray(getString(R.string.items));

                    for (int i = 0; i < itemsArray.length(); i++) {
                        JSONObject jsonObject = itemsArray.getJSONObject(i);
                        JSONObject volumeInfo = jsonObject.getJSONObject(getString(R.string.volume_info));

                        if (volumeInfo.has(getString(R.string.title))) {
                            title = volumeInfo.optString(getString(R.string.title));
                        } else {
                            title = getString(R.string.no_title_available);
                        }

                        if (volumeInfo.has(getString(R.string.authors))) {
                            authorsArray = volumeInfo.getJSONArray(getString(R.string.authors));
                            for (int j = 0; j < authorsArray.length(); j++) {
                                String authorsNames = authorsArray.getString(j);
                                authors = authors.concat(authorsNames + "   ");
                            }
                        } else {
                            authors = getString(R.string.no_authors_available);
                        }

                        if (volumeInfo.has(getString(R.string.description))) {
                            description = volumeInfo.optString(getString(R.string.description));
                        } else {
                            description = getString(R.string.no_descrip_available);
                        }

                        HashMap<String, String> book = new HashMap<>();

                        book.put(getString(R.string.title), title);
                        book.put(getString(R.string.description), description);
                        book.put(getString(R.string.authors), authors);
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
                Log.e(TAG, getString(R.string.json_server_error));
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            mLoadingIndicator.setVisibility(View.INVISIBLE);
            if (!bookList.equals(null)) {
                ListAdapter adapter = new SimpleAdapter(MainActivity.this, bookList,
                        R.layout.list_item, new String[]{getString(R.string.title),
                        getString(R.string.description), getString(R.string.authors)},
                        new int[]{R.id.title, R.id.description, R.id.authors});
                listView.setAdapter(adapter);
            } else {
                listView.setEmptyView(findViewById(R.id.empty));
            }
        }
    }
}