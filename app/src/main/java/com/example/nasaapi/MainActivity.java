package com.example.nasaapi;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.loader.app.LoaderManager;
import androidx.loader.content.Loader;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
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
    Animation scaleUp, scaleDown;

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        btnApod = findViewById(R.id.btnApod);
        btnEarth = findViewById(R.id.btnEarth);
        btnEarthUser = findViewById(R.id.btnEarthUser);
        btnExit = findViewById(R.id.btnExit);

        scaleUp = AnimationUtils.loadAnimation(this, R.anim.scale_up);
        scaleDown = AnimationUtils.loadAnimation(this, R.anim.scale_down);

        btnApod.setOnTouchListener(new View.OnTouchListener(){
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if(event.getAction() == MotionEvent.ACTION_DOWN){
                    btnApod.startAnimation(scaleUp);
                    Intent intent = new Intent(v.getContext(), APOD.class);
                    startActivity(intent);
                    finish();
                }else if(event.getAction() == MotionEvent.ACTION_UP){
                    btnApod.startAnimation(scaleDown);
                }
                return true;
            }
        });

        btnEarthUser.setOnTouchListener(new View.OnTouchListener(){
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if(event.getAction() == MotionEvent.ACTION_DOWN){
                    btnEarthUser.startAnimation(scaleUp);
                    Intent intent = new Intent(v.getContext(), EarthUser.class);
                    startActivity(intent);
                    finish();
                }else if(event.getAction() == MotionEvent.ACTION_UP){
                    btnEarthUser.startAnimation(scaleDown);
                }
                return true;
            }
        });

        btnEarth.setOnTouchListener(new View.OnTouchListener(){
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if(event.getAction() == MotionEvent.ACTION_DOWN){
                    btnEarth.startAnimation(scaleUp);
                    Intent intent = new Intent(v.getContext(), Earth.class);
                    startActivity(intent);
                    finish();
                }else if(event.getAction() == MotionEvent.ACTION_UP){
                    btnEarth.startAnimation(scaleDown);
                }
                return true;
            }
        });

        btnExit.setOnTouchListener(new View.OnTouchListener(){
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if(event.getAction() == MotionEvent.ACTION_DOWN){
                    btnExit.startAnimation(scaleUp);
                    System.exit(0);
                }else if(event.getAction() == MotionEvent.ACTION_UP){
                    btnExit.startAnimation(scaleDown);
                }
                return true;
            }
        });
        }
    }
