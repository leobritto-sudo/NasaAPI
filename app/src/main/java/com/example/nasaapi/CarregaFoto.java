package com.example.nasaapi;

import android.content.Context;

import androidx.annotation.Nullable;
import androidx.loader.content.AsyncTaskLoader;

public class CarregaFoto extends AsyncTaskLoader<String> {
    private String mQueryString;
    CarregaFoto(Context context, String queryString) {
        super(context);
        mQueryString = queryString;
    }
    @Override
    protected void onStartLoading() {
        super.onStartLoading();
        forceLoad();
    }
    @Nullable
    @Override
    public String loadInBackground() {
        return NetworkUtils.buscaInfoFotos(mQueryString);
    }
}
