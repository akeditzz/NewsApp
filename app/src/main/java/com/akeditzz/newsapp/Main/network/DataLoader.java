package com.akeditzz.newsapp.Main.network;

import android.content.AsyncTaskLoader;
import android.content.Context;
import android.text.TextUtils;
import android.util.Log;

import com.akeditzz.newsapp.Main.model.ContributorModel;
import com.akeditzz.newsapp.Main.model.NewsModel;
import com.akeditzz.newsapp.R;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;

public class DataLoader extends AsyncTaskLoader<ArrayList<NewsModel>> {
    private final static String TAG = DataLoader.class.getName();
    private final String urlNews;

    public DataLoader(Context context, String url) {
        super(context);
        this.urlNews = url;
    }

    @Override
    protected void onStartLoading() {
        forceLoad();
    }

    @Override
    public ArrayList<NewsModel> loadInBackground() {
        return getData();
    }

    /**
     * api call
     *
     * @return arraylist of newsmdel
     */
    private ArrayList<NewsModel> getData() {
        // Create URL object
        URL url = createUrl(urlNews);

        // Perform HTTP request to the URL and receive a JSON response back
        String jsonResponse = "";
        try {
            jsonResponse = makeHttpRequest(url);
        } catch (IOException e) {
        }
        Log.e(TAG, jsonResponse);
        return extractNewsData(jsonResponse);
    }

    /**
     * Method to parse responseJSON
     *
     * @param jsonResponse string from response
     * @return parsed data into list
     */
    private ArrayList<NewsModel> extractNewsData(String jsonResponse) {
        ArrayList<NewsModel> newsList = new ArrayList<>();
        if (!TextUtils.isEmpty(jsonResponse)) {
            try {
                JSONObject jsonObject = new JSONObject(jsonResponse);
                JSONArray jsonArray = jsonObject.optJSONObject(getContext().getString(R.string.api_parameter_response)).optJSONArray(getContext().getString(R.string.api_parameter_result));
                if (jsonArray.length() > 0) {
                    for (int i = 0; i < jsonArray.length(); i++) {
                        ArrayList<ContributorModel> contributorModelList = new ArrayList<>();
                        JSONArray contributorArray = jsonArray.optJSONObject(i).optJSONArray(getContext().getString(R.string.api_parameter_tags));
                        if (contributorArray.length() > 0) {
                            for (int j = 0; j < contributorArray.length(); j++) {
                                contributorModelList.add(new ContributorModel(contributorArray.optJSONObject(j)
                                        .optString(getContext().getString(R.string.api_parameter_webTitle))));
                            }
                        }

                        newsList.add(new NewsModel(jsonArray.optJSONObject(i).optString(getContext().getString(R.string.api_parameter_sectionName))
                                , jsonArray.optJSONObject(i).optString(getContext().getString(R.string.api_parameter_webPublicationDate))
                                , jsonArray.optJSONObject(i).optString(getContext().getString(R.string.api_parameter_webTitle))
                                , jsonArray.optJSONObject(i).optString(getContext().getString(R.string.api_parameter_webUrl))
                                , contributorModelList));
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return newsList;
    }

    /**
     * Returns new URL object from the given string URL.
     */
    private URL createUrl(String stringUrl) {
        URL url = null;
        try {
            url = new URL(stringUrl);
        } catch (MalformedURLException exception) {
            Log.e(TAG, getContext().getString(R.string.error_url), exception);
            return null;
        }
        return url;
    }

    /**
     * Make an HTTP request to the given URL and return a String as the response.
     */
    private String makeHttpRequest(URL url) throws IOException {
        String jsonResponse = "";
        HttpURLConnection urlConnection = null;
        InputStream inputStream = null;
        try {
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod(getContext().getString(R.string.requestMethod_GET));
            urlConnection.setReadTimeout(10000 /* milliseconds */);
            urlConnection.setConnectTimeout(15000 /* milliseconds */);
            urlConnection.connect();
            if (urlConnection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                inputStream = urlConnection.getInputStream();
                jsonResponse = readFromStream(inputStream);
            }

        } catch (IOException e) {
            // TODO: Handle the exception
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (inputStream != null) {
                // function must handle java.io.IOException here
                inputStream.close();
            }
        }
        return jsonResponse;
    }

    /**
     * Convert the {@link InputStream} into a String which contains the
     * whole JSON response from the server.
     */
    private String readFromStream(InputStream inputStream) throws IOException {
        StringBuilder output = new StringBuilder();
        if (inputStream != null) {
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream, Charset.forName(getContext().getString(R.string.charset_UTF_8)));
            BufferedReader reader = new BufferedReader(inputStreamReader);
            String line = reader.readLine();
            while (line != null) {
                output.append(line);
                line = reader.readLine();
            }
        }
        return output.toString();
    }

}
