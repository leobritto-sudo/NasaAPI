package com.example.nasaapi;

import android.net.Uri;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class NetworkUtils {
    private static final String LOG_TAG = NetworkUtils.class.getSimpleName();
    private static final String NASA_URL = "https://api.nasa.gov/planetary/apod?";
    private static final String API_KEY = "api_key";
    private static final String QUERY_PARAM = "wendpmstPhaVN9CEoHXwGafr8GWSsvmWMmdHYwQf";
    static String buscaInfoFotos(String queryString) {
        HttpURLConnection urlConnection = null;
        BufferedReader reader = null;
        String imageJSONString = null;
        try {
            // Construção da URI de Busca
            Uri builtURI = Uri.parse(NASA_URL).buildUpon()
                    .appendQueryParameter(API_KEY, QUERY_PARAM)
                    .appendQueryParameter("date", queryString)
                    .build();
            // Converte a URI para a URL.
            URL requestURL = new URL(builtURI.toString());
            // Abre a conexão de rede
            urlConnection = (HttpURLConnection) requestURL.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();
            // Busca o InputStream.
            InputStream inputStream = urlConnection.getInputStream();
            // Cria o buffer para o input stream
            reader = new BufferedReader(new InputStreamReader(inputStream));
            // Usa o StringBuilder para receber a resposta.
            StringBuilder builder = new StringBuilder();
            String linha;
            while ((linha = reader.readLine()) != null) {
                // Adiciona a linha a string.
                builder.append(linha);
                builder.append("\n");
            }
            if (builder.length() == 0) {
                // se o stream estiver vazio não faz nada
                return null;
            }
            imageJSONString = builder.toString();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            // fecha a conexão e o buffer.
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
        // escreve o Json no log
        Log.d(LOG_TAG, imageJSONString);
        return imageJSONString;
    }
}
