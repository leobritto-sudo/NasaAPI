package com.example.nasaapi;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.io.IOException;
import java.io.InputStream;

public class NasaPic extends AppCompatActivity {

    private TextView txt2;
    private TextView txt3;
    private ImageView ivNasa;
    private Button btn2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nasa_pic);
        txt2 = findViewById(R.id.txtLocal);
        txt3 = findViewById(R.id.txt3);
        ivNasa = findViewById(R.id.ivNasa);
        btn2 = findViewById(R.id.btn2);

        SharedPreferences prefURL = getSharedPreferences("url", Context.MODE_PRIVATE);
        String urlLink = prefURL.getString("url", "não encontrado");
        if(urlLink != null) {

            LoadImage loadImage = new LoadImage(ivNasa);
            loadImage.execute(urlLink);
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

    private class LoadImage extends AsyncTask<String, Void, Bitmap> {
        ImageView imageView;
        public LoadImage(ImageView ivNasa){
            this.imageView = ivNasa;
        }

        @Override
        protected Bitmap doInBackground(String... strings) {
            String urlLink = strings[0];
            Bitmap bitmap = null;
            try {
                InputStream inputStream = new java.net.URL(urlLink).openStream();
                bitmap = BitmapFactory.decodeStream(inputStream);
            } catch (IOException e) {
                e.printStackTrace();
            }
            return bitmap;
        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {
            ivNasa.setImageBitmap(bitmap);
        }
    }
}