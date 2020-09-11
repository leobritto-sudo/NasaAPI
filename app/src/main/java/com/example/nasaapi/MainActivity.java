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
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;

import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class MainActivity extends AppCompatActivity{

    private Button btnApod, btnEarth, btnEarthUser, btnExit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        btnApod = findViewById(R.id.btnApod);
        btnEarth = findViewById(R.id.btnEarth);
        btnEarthUser = findViewById(R.id.btnEarthUser);
        btnExit = findViewById(R.id.btnExit);
        }

        public void btEarth(View v){
            finish();
            Intent intent = new Intent(this, Earth.class);
            startActivity(intent);
        }

        public void btEarthUser(View v){
            finish();
            Intent intent = new Intent(this, EarthUser.class);
            startActivity(intent);
        }

        public void btApod(View v){
            finish();
            Intent intent = new Intent(this, APOD.class);
            startActivity(intent);
        }

        public void Exit(View v) {
            finish();
            System.exit(0);
        }
    }
