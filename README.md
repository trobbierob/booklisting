# Book Listing
Seventh Project for Udacity's Android Basics Program

## Description

Book Listing allows a user to get a list of published books on a given topic. It uses the Google Books Api in order to fetch results and display them to the user.

## Prerequisites

* Android SDK v25

## Screenshots

![](https://github.com/trobbierob/booklisting/blob/master/screenshots/screenshot1.png)<br />
![](https://github.com/trobbierob/booklisting/blob/master/screenshots/screenshot2.png)

## Specifications

* App contains a ListView which becomes populated with list items.
  * When there are more list items than fit on the screen, the app allows scrolling through the list.
* The content should not be cut off if the device is rotated to landscape mode.
* The user can enter a word or phrase to serve as a search query. The app fetches book data related to the query via an HTTP request from the Google Books API, using a class such as HttpUriRequest or HttpURLConnection.
* The network call occurs off the UI thread using an AsyncTask or similar threading object.
* The app checks whether the device is connected to the internet and responds appropriately. The result of the request is validated to account for a bad server response or lack of server response.
* The JSON response is parsed correctly, and relevant information is stored in the app.
* When there is no data to display, the app shows a default TextView that informs the user how to populate the list.
* The use of external libraries for core functionality will not be permitted to complete this project.

## Sample Code
~~~
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
~~~
