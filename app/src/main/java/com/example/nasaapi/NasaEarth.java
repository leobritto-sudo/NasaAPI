package com.example.nasaapi;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

public class NasaEarth extends AppCompatActivity {

    ImageView ivNasa;
    TextView txt3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nasa_earth);

        txt3 = findViewById(R.id.txt3);
        ivNasa = findViewById(R.id.ivNasa);

        Bundle bundle = getIntent().getExtras();
        String URL = bundle.getString("url");
        if(URL != null) {

            Uri uriimg = Uri.parse(URL);
            Glide.with(this)
                    .load(uriimg)
                    .into(ivNasa);
        }else{

            txt3.setText(R.string.txt3_no_results);

        }

    }

    public void share(View view){

        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("text/plain");
        Bundle bundle = getIntent().getExtras();
        String ShareBody = bundle.getString("url");
        String shareSub = "Sei lá chupingaaa";
        intent.putExtra(Intent.EXTRA_SUBJECT, shareSub);
        intent.putExtra(Intent.EXTRA_TEXT, ShareBody);
        startActivity(Intent.createChooser(intent,"Share using"));
    }

    public void back(View view){
        finish();
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }
}