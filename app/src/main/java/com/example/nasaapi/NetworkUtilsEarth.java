package com.example.nasaapi;

import android.net.Uri;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class NetworkUtilsEarth {
    private static final String LOG_TAG = NetworkUtils.class.getSimpleName();
    private static final String NASA_URL = "https://api.nasa.gov/planetary/earth/imagery?";
    private static final String API_KEY = "api_key";
    private static final String DIM = "dim";
    private static final String QUERY_PARAM = "WJq7npsb0pcaFoWI0xem1yATVBo9r5qKRetubm6T";
    static String buscaFotos(String queryLon, String queryLat, String queryDate) {
        HttpURLConnection urlConnection = null;
        BufferedReader reader = null;
        String imageJSONString = null;
        try {
            Uri builtURI = Uri.parse(NASA_URL).buildUpon()
                    .appendQueryParameter("lon", queryLon)
                    .appendQueryParameter("lat", queryLat)
                    .appendQueryParameter(DIM, "0.15")
                    .appendQueryParameter("date", queryDate)
                    .appendQueryParameter(API_KEY, QUERY_PARAM)
                    .build();
            URL requestURL = new URL(builtURI.toString());
            urlConnection = (HttpURLConnection) requestURL.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();
            InputStream inputStream = urlConnection.getInputStream();
            reader = new BufferedReader(new InputStreamReader(inputStream));
            StringBuilder builder = new StringBuilder();
            String linha;
            while ((linha = reader.readLine()) != null) {
                builder.append(linha);
                builder.append("\n");
            }
            if (builder.length() == 0) {
                return null;
            }
            imageJSONString = builder.toString();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        Log.d(LOG_TAG, imageJSONString);
        return imageJSONString;
    }
}
