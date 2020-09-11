package com.example.nasaapi;

import android.content.Context;

import androidx.annotation.Nullable;
import androidx.loader.content.AsyncTaskLoader;

public class CarregaFotoEarth extends AsyncTaskLoader<String> {
    private String mQueryString;
    private String latQueryString;
    private String lonQueryString;
    CarregaFotoEarth(Context context, String queryString, String queryLat, String queryLon) {
        super(context);
        mQueryString = queryString;
        latQueryString = queryLat;
        lonQueryString = queryLon;
    }
    @Override
    protected void onStartLoading() {
        super.onStartLoading();
        forceLoad();
    }
    @Nullable
    @Override
    public String loadInBackground() {
        return NetworkUtilsEarth.buscaFotos(lonQueryString, latQueryString, mQueryString);
    }
}
