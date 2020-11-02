package com.example.nasaapi;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.loader.app.LoaderManager;
import androidx.loader.content.Loader;

import android.Manifest;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import org.json.JSONObject;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class EarthUser extends AppCompatActivity implements LoaderManager.LoaderCallbacks<String> {

    private static final String TAG = "EARTH_USER";
    private static final String KEYSTRING_DATE_ = "date_key";
    FusedLocationProviderClient fusedLocationProviderClient;
    TextView txt1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_earth_user);

        txt1 = findViewById(R.id.txt1);

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
        if (getSupportLoaderManager().getLoader(0) != null) {
            getSupportLoaderManager().initLoader(0, null, this);
        }

        if(savedInstanceState != null){
            String savedDate = savedInstanceState.getString(KEYSTRING_DATE_);
            txt1.setText(savedDate);
        }else{

        }
    }

    public void onSaveInstanceState(Bundle savedInstance) {
        savedInstance.putString(KEYSTRING_DATE_, txt1.getText().toString());

        super.onSaveInstanceState(savedInstance);
    }

    public void selectDate(View view) {
        Calendar cal = Calendar.getInstance();
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH);
        int day = cal.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog dialog = new DatePickerDialog(
                EarthUser.this,
                android.R.style.Theme_Holo_Light_Dialog_MinWidth,
                mDate,
                year, month, day);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.show();
    }


    private DatePickerDialog.OnDateSetListener mDate = new DatePickerDialog.OnDateSetListener() {
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

    public void buscarImage(View view) throws ParseException {
        String dateNasa = txt1.getText().toString();
        SimpleDateFormat formatoOrigem = new SimpleDateFormat("dd/MM/yyyy");
        Date data = formatoOrigem.parse(dateNasa);
        SimpleDateFormat formatoDestino = new SimpleDateFormat("yyyy-MM-dd");
        String queryString = formatoDestino.format(data);

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
        if (networkInfo != null && networkInfo.isConnected()
                && queryString.length() != 0) {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                getLocation();
            } else {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 44);
            }
        } else {
            if (queryString.length() == 16) {
                txt1.setText("DATA VAZIA, INFORME UMA");
            } else {
                txt1.setText("Verifique sua conexão");
            }
        }
    }

    private void getLocation() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        fusedLocationProviderClient.getLastLocation().addOnCompleteListener(new OnCompleteListener<Location>() {
            @Override
            public void onComplete(@NonNull Task<Location> task) {
                Location location = task.getResult();
                if (location != null) {
                    try {
                        Geocoder geocoder = new Geocoder(EarthUser.this, Locale.getDefault());
                        Geolocation gl = new Geolocation();
                        List<Address> addresses = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
                        String dateNasa = txt1.getText().toString();
                        SimpleDateFormat formatoOrigem = new SimpleDateFormat("dd/MM/yyyy");
                        Date data = formatoOrigem.parse(dateNasa);
                        SimpleDateFormat formatoDestino = new SimpleDateFormat("yyyy-MM-dd");
                        String queryString = formatoDestino.format(data);
                        double lat = addresses.get(0).getLatitude();
                        double lon = addresses.get(0).getLongitude();
                        gl.setLatitude(lat);
                        gl.setLongitude(lon);
                        String queryLat = String.valueOf(gl.getLatitude());
                        String queryLon = String.valueOf(gl.getLongitude());
                        Bundle queryBundle = new Bundle();
                        queryBundle.putString("queryString", queryString);
                        queryBundle.putString("queryLat", queryLat);
                        queryBundle.putString("queryLon", queryLon);
                        getSupportLoaderManager().restartLoader(0, queryBundle, EarthUser.this);

                    } catch (IOException | ParseException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
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
                    Toast.makeText(EarthUser.this, "Erro ao adicionar", Toast.LENGTH_SHORT).show();
                    imageModel = new ImageModel(-1, "error");
                }

                DataBaseHelper dataBaseHelper = new DataBaseHelper(EarthUser.this);

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