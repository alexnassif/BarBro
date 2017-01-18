/*
 * Copyright (C) 2016 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.example.raymond.barbro.utilities;

import android.net.Uri;
import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Scanner;

/**
 * These utilities will be used to communicate with the weather servers.
 */
public class NetworkUtils {

    final static String GITHUB_BASE_URL =
            "http://addb.absolutdrinks.com";

    //http://addb.absolutdrinks.com/drinks/withtype/vodka/?apiKey=c7ca8a9924d84d159a4f1e864456be9c
    final static String API_KEY = "c7ca8a9924d84d159a4f1e864456be9c";

    final static String PARAM_QUERY = "apiKey";

    /*
     * The sort field. One of stars, forks, or updated.
     * Default: results are sorted by best match if no field is specified.
     */
    final static String drinks = "drinks";
    final static String withType = "withtype";

    /**
     * Builds the URL used to query Github.
     *
     * @param drinkType The keyword that will be queried for.
     * @return The URL to use to query the weather server.
     */
    public static URL buildUrl(String drinkType) {
        Uri builtUri = Uri.parse(GITHUB_BASE_URL).buildUpon()
                .appendPath(drinks)
                .appendPath(withType)
                .appendPath(drinkType)
                .appendPath("")
                .appendQueryParameter(PARAM_QUERY, API_KEY)
                .appendQueryParameter("start", "0")
                .appendQueryParameter("pageSize", "4000")
                .build();

        URL url = null;
        try {
            url = new URL(builtUri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        return url;
    }

    public static URL buildAllDrinksUrl() {

        URL url = null;
        try {
            url = new URL("http://addb.absolutdrinks.com/drinks/?apiKey=c7ca8a9924d84d159a4f1e864456be9c&start=0&pageSize=3637");
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        return url;
    }

    /**
     * This method returns the entire result from the HTTP response.
     *
     * @param url The URL to fetch the HTTP response from.
     * @return The contents of the HTTP response.
     * @throws IOException Related to network and stream reading
     */
    public static String getResponseFromHttpUrl(URL url) throws IOException {
        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
        try {
            InputStream in = urlConnection.getInputStream();

            Scanner scanner = new Scanner(in);
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
}