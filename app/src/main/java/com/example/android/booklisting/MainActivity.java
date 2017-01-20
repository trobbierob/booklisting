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

    TextView mErrorMessage;

    public String jsonString;
    public URL bookQueryUrl;

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

    //private void makeBookSearchQuery() {
        //String bookQuery = mSearchEditText.getText().toString();
        //URL bookQueryURL = NetworkUtils.buildURL(bookQuery);
        //try{
            //jsonString = NetworkUtils.getResponseFromHttpUrl(bookQueryURL);
        //}  catch (IOException e) {

        //}
        //mUrlDisplay.setText(bookQueryURL.toString());
        //new BookQueryTask().execute(jsonString);
    //}

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int menuItemSelected = item.getItemId();
        if (menuItemSelected == R.id.action_bar_search) {
            new BookQueryTask().execute();
            //makeBookSearchQuery();
            Toast.makeText(getBaseContext(), "Search was clicked", Toast.LENGTH_SHORT).show();
        }
        return super.onOptionsItemSelected(item);
    }

    public class BookQueryTask extends AsyncTask<Void, Void, Void>{

        @Override
        protected void onPreExecute() {
            String bookQuery = mSearchEditText.getText().toString();
            //URL bookQueryURL = NetworkUtils.buildURL(bookQuery);
            bookQueryUrl = NetworkUtils.buildURL(bookQuery);
        }

        @Override
        protected Void doInBackground(Void... urls) {

            Log.v(TAG, "bookQueryUrl is: " + bookQueryUrl);

            if (bookQueryUrl != null) {
                try {
                    jsonString = NetworkUtils.getResponseFromHttpUrl(bookQueryUrl);
                    Log.v(TAG, "jsonString is: " + jsonString);

                    JSONObject jsonObject = new JSONObject(jsonString);
                    Log.v(TAG, "jsonObject is " + jsonObject);
                } catch (IOException e) {

                } catch (JSONException e) {

                }

            } else {

            }


            /*
            if (urls.length == 0) {
                //return null;
            }

            String bookQueryString = urls[0];

            URL bookRequestUrl = NetworkUtils.buildURL(bookQueryString);


            try {


                String jsonBookResponse = NetworkUtils
                        .getResponseFromHttpUrl(bookRequestUrl);
                Log.v(TAG, "jsonBoookReponse is " + jsonBookResponse);

                JSONObject root = new JSONObject(jsonBookResponse);
                Log.v(TAG, "rootUrl is " + root);

                JSONObject kind = root.getJSONObject("volumeInfo");
                Log.v(TAG, "kind is " + kind);

                JSONArray itemsArray = root.getJSONArray("items");
                Log.v(TAG, "itemsArray is " + itemsArray);

                */
                /*

                String jsonBookResponse = NetworkUtils
                        .getResponseFromHttpUrl(bookRequestUrl);

                Log.v(TAG, "jsonBoookReponse is " + jsonBookResponse);

                JSONObject rootUrl = new JSONObject(jsonBookResponse);

                Log.v(TAG, "rootUrl is " + rootUrl);

                JSONArray itemsArray = rootUrl.getJSONArray("items");

                Log.v(TAG, "itemsArray is " + itemsArray);

                 */

                /*
                for (int i = 0; i < itemsArray.length(); i++) {

                    JSONObject volumeInfo = itemsArray.getJSONObject(4);

                    Log.v(TAG, "volumeInfo is " + volumeInfo);

                    String title = volumeInfo.getString("title");

                    Log.v(TAG, "title is " + title);

                    HashMap<String, String> book = new HashMap<>();

                    book.put("title", title);

                    bookList.add(book);
                }
                */

            //} catch (final JSONException e) {
                //e.printStackTrace();
                //return null;
            //} catch (IOException e) {
                //e.printStackTrace();
                //return null;
            //}

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