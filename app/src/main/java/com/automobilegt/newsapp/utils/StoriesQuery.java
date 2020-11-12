package com.automobilegt.newsapp.utils;

import android.text.TextUtils;

import com.automobilegt.newsapp.model.Story;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public class StoriesQuery {

    public StoriesQuery() {
    }

    public static List<Story> fetchStoryData(String requestUrl) {
        URL url = createUrl(requestUrl);
        String jsonResponse = null;
        try {
            jsonResponse = makeHttpRequest(url);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return extractFeatureFromJson(jsonResponse);
    }

    private static URL createUrl(String requestUrl) {
        URL url = null;
        try {
            url = new URL(requestUrl);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        return url;
    }

    private static String makeHttpRequest(URL url) throws IOException {
        String jsonResponse = "";
        if (url == null) {
            return jsonResponse;
        }
        HttpURLConnection httpURLConnection = null;
        InputStream inputStream = null;
        try {
            httpURLConnection = (HttpURLConnection) url.openConnection();
            httpURLConnection.setReadTimeout(10000 /* milliseconds */);
            httpURLConnection.setConnectTimeout(15000 /* milliseconds */);
            httpURLConnection.setRequestMethod("GET");
            httpURLConnection.connect();
            if (httpURLConnection.getResponseCode() == 200) {
                inputStream = httpURLConnection.getInputStream();
                jsonResponse = readFromStream(inputStream);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (httpURLConnection != null) {
                httpURLConnection.disconnect();
            }
            if (inputStream != null) {
                inputStream.close();
            }
        }
        return jsonResponse;
    }

    private static String readFromStream(InputStream inputStream) throws IOException {
        StringBuilder output = new StringBuilder();
        if (inputStream != null) {
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream, StandardCharsets.UTF_8);
            BufferedReader reader = new BufferedReader(inputStreamReader);
            String line = reader.readLine();
            while (line != null) {
                output.append(line);
                line = reader.readLine();
            }
        }
        return output.toString();
    }

    private static List<Story> extractFeatureFromJson(String storiesJSON) {
        if (TextUtils.isEmpty(storiesJSON)) {
            return null;
        }
        List<Story> stories = new ArrayList<>();
        try {
            JSONObject baseJsonResponse = new JSONObject(storiesJSON);
            JSONObject storiesJSONKeyResponse = baseJsonResponse.getJSONObject("response");
            JSONArray storiesJSONKeyResults = storiesJSONKeyResponse.getJSONArray("results");
            for (int i = 0; i < storiesJSONKeyResults.length(); i++) {
                JSONObject currentStory = storiesJSONKeyResults.getJSONObject(i);
                String title = currentStory.getString("webTitle");
                String section = currentStory.getString("sectionName");
                String webUrl = currentStory.getString("webUrl");
                String publicationDate = currentStory.getString("webPublicationDate");
                JSONArray JSONArrayShowTags = currentStory.getJSONArray("tags");
                JSONObject storiesKeyShowTags = JSONArrayShowTags.getJSONObject(0);
                String author = storiesKeyShowTags.getString("webTitle");
                Story story = new Story(title, section, webUrl, publicationDate, author);
                stories.add(story);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return stories;
    }
}