package com.example.nasaapi;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.loader.app.LoaderManager;
import androidx.loader.content.Loader;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.location.Address;
import android.location.Geocoder;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;

import org.json.JSONObject;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class Earth extends AppCompatActivity implements LoaderManager.LoaderCallbacks<String>  {

    private static final String TAG = "EARTH";
    TextView txt1;
    EditText editLat;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_earth);

        editLat = findViewById(R.id.editLat);
        txt1 = findViewById(R.id.txt1);
        if (getSupportLoaderManager().getLoader(0) != null) {
            getSupportLoaderManager().initLoader(0, null,  this);
        }
    }

    public void selectDate(View view){
        Calendar cal = Calendar.getInstance();
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH);
        int day = cal.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog dialog = new DatePickerDialog(
                Earth.this,
                android.R.style.Theme_Holo_Light_Dialog_MinWidth,
                mDate,
                year,month,day);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.show();
    }


    private DatePickerDialog.OnDateSetListener mDate = new DatePickerDialog.OnDateSetListener(){
        @Override
        public void onDateSet(DatePicker view, int year, int month, int day) {
            month = month + 1;
            Log.d(TAG, "onDateSet: dd/mm/yyyy: " + day + "/" + month + "/" + year);

            String date = day + "/" + month + "/" + year;
            txt1.setText(date);
        }
    };

    public void Back(View v) {
        finish();
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    public void buscarImage(View view) throws ParseException, IOException {

        InputMethodManager inputManager = (InputMethodManager)
                getSystemService(Context.INPUT_METHOD_SERVICE);
        if (inputManager != null) {
            inputManager.hideSoftInputFromWindow(view.getWindowToken(),
                    InputMethodManager.HIDE_NOT_ALWAYS);
        }

        ConnectivityManager connMgr = (ConnectivityManager)
                getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = null;
        if (connMgr != null) {
            networkInfo = connMgr.getActiveNetworkInfo();
        }
        if (networkInfo != null && networkInfo.isConnected()) {
            getAddress();
        }
        else {

        }
    }

    private void getAddress() throws IOException, ParseException {
        Geocoder geocoder = new Geocoder(this);
        List<Address> addresses = geocoder.getFromLocationName(editLat.getText().toString(), 1);
        String queryLat = String.valueOf(addresses.get(0).getLatitude());
        String queryLon = String.valueOf(addresses.get(0).getLongitude());
        Bundle queryBundle = new Bundle();
        String dateNasa = txt1.getText().toString();
        SimpleDateFormat formatoOrigem = new SimpleDateFormat("dd/MM/yyyy");
        Date data = formatoOrigem.parse(dateNasa);
        SimpleDateFormat formatoDestino = new SimpleDateFormat("yyyy-MM-dd");
        String queryString = formatoDestino.format(data);
        queryBundle.putString("queryString", queryString);
        queryBundle.putString("queryLat", queryLat);
        queryBundle.putString("queryLon", queryLon);
        getSupportLoaderManager().restartLoader(0, queryBundle, this);
    }

    @NonNull
    @Override
    public Loader<String> onCreateLoader(int id, @Nullable Bundle args) {
        String queryString = "";
        String queryLat = "";
        String queryLon = "";
        if (args != null) {
            queryString = args.getString("queryString");
            queryLat = args.getString("queryLat");
            queryLon = args.getString("queryLon");
        }
        return new CarregaFotoEarth(this, queryString, queryLat, queryLon);
    }
    @Override
    public void onLoadFinished(@NonNull Loader<String> loader, String data) {
        try {
            JSONObject jsonObject = new JSONObject(data);

            String URL = jsonObject.getString("url");

            if (URL != null) {

                Intent intent = new Intent(this, NasaEarth.class);
                intent.putExtra("url", URL);
                startActivity(intent);
                finish();

            } else {
                txt1.setText("Sem resultados, tente novamente");
            }
        } catch (Exception e) {
            txt1.setText("Sem resultados, tente novamente");
            e.printStackTrace();
        }
    }
    @Override
    public void onLoaderReset(@NonNull Loader<String> loader) {
        // obrigatório implementar, nenhuma ação executada
    }
}