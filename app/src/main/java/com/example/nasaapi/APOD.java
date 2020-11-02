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
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class APOD extends AppCompatActivity implements LoaderManager.LoaderCallbacks<String> {

    private static final String TAG = "APOD";
    private static final String KEYSTRING_DATE_ = "date_key";
    private TextView txt1, txt;
    private Button btn1;
    private Button btnExit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_a_p_o_d);

        txt1 = findViewById(R.id.txt1);
        txt = findViewById(R.id.txtLocal);
        btn1 = findViewById(R.id.btnEarth);
        btnExit = findViewById(R.id.btnExit);
        if (getSupportLoaderManager().getLoader(0) != null) {
            getSupportLoaderManager().initLoader(0, null,  this);
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

    public void selectDate(View view){
        Calendar cal = Calendar.getInstance();
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH);
        int day = cal.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog dialog = new DatePickerDialog(
                APOD.this,
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
            Bundle queryBundle = new Bundle();
            queryBundle.putString("queryString", queryString);
            getSupportLoaderManager().restartLoader(0, queryBundle, this);
        }
        else {
            if (queryString.length() == 16) {
                txt1.setText("DATA VAZIA, INFORME UMA");
            } else {
                txt1.setText("Verifique sua conexão");
            }
        }
    }
    @NonNull
    @Override
    public Loader<String> onCreateLoader(int id, @Nullable Bundle args) {
        String queryString = "";
        if (args != null) {
            queryString = args.getString("queryString");
        }
        return new CarregaFoto(this, queryString);
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
                    Toast.makeText(APOD.this, "Erro ao adicionar", Toast.LENGTH_SHORT).show();
                    imageModel = new ImageModel(-1, "error");
                }

                DataBaseHelper dataBaseHelper = new DataBaseHelper(APOD.this);

                boolean b = dataBaseHelper.addOne(imageModel);
                Toast.makeText(this, "Success = " + b, Toast.LENGTH_SHORT).show();


                Intent intent = new Intent(this, NasaPic.class);
                SharedPreferences prefURL = getSharedPreferences("url", Context.MODE_PRIVATE);
                SharedPreferences.Editor ed = prefURL.edit();
                ed.putString("url", URL);
                ed.apply();
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