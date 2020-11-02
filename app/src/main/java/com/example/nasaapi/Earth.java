package com.example.nasaapi;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.loader.app.LoaderManager;
import androidx.loader.content.Loader;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.location.Geocoder;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONObject;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class Earth extends AppCompatActivity implements LoaderManager.LoaderCallbacks<String>  {

    private static final String TAG = "EARTH";
    private static final String KEYSTRING_DATE_ = "date_key";
    TextView txt1;
    AutoCompleteTextView editLat;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_earth);

        editLat = findViewById(R.id.editLat);
        txt1 = findViewById(R.id.txt1);
        if (getSupportLoaderManager().getLoader(0) != null) {
            getSupportLoaderManager().initLoader(0, null,  this);
        }

        if(savedInstanceState != null){
            String savedDate = savedInstanceState.getString(KEYSTRING_DATE_);
            txt1.setText(savedDate);
        }else{

        }

        DataBaseHelper dataBaseHelper = new DataBaseHelper(Earth.this);
        ArrayList<String> allAddresses = dataBaseHelper.getAllAddresses();
        ArrayAdapter<String> addAllAddresses = new ArrayAdapter<String>(Earth.this, android.R.layout.simple_list_item_1, allAddresses);
        editLat.setAdapter(addAllAddresses);
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstance) {
        savedInstance.putString(KEYSTRING_DATE_, txt1.getText().toString());

        super.onSaveInstanceState(savedInstance);
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
        Geolocation gl = new Geolocation();
        List<android.location.Address> addresses = geocoder.getFromLocationName(editLat.getText().toString(), 1);
        String address = editLat.getText().toString();

        Address ad;

        try {
            ad = new Address(-1, address);
        }catch(Exception e){
            Toast.makeText(Earth.this, "Erro ao adicionar", Toast.LENGTH_SHORT).show();
            ad = new Address(-1, "error");
        }

        DataBaseHelper dataBaseHelper = new DataBaseHelper(Earth.this);

        boolean b = dataBaseHelper.addAddress(ad);
        Toast.makeText(Earth.this, "Success = " + b, Toast.LENGTH_SHORT).show();

        double lat = addresses.get(0).getLatitude();
        double lon = addresses.get(0).getLongitude();
        gl.setLatitude(lat);
        gl.setLongitude(lon);
        String queryLat = String.valueOf(gl.getLatitude());
        String queryLon = String.valueOf(gl.getLongitude());
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

                ImageModel imageModel;

                try {
                    imageModel = new ImageModel(-1, URL);
                    imageModel.setURL(URL);
                }catch(Exception e){
                    Toast.makeText(Earth.this, "Erro ao adicionar", Toast.LENGTH_SHORT).show();
                    imageModel = new ImageModel(-1, "error");
                }

                DataBaseHelper dataBaseHelper = new DataBaseHelper(Earth.this);
                dataBaseHelper.addOne(imageModel);

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