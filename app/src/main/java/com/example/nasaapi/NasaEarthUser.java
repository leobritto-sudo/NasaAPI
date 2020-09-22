package com.example.nasaapi;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.AnimationDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.IOException;
import java.io.InputStream;

public class NasaEarthUser extends AppCompatActivity {

    ImageView ivNasa;
    AnimationDrawable animation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nasa_earth_user);

        ivNasa = findViewById(R.id.ivNasa);

        animation = (AnimationDrawable) ivNasa.getDrawable();
        animation.start();

        Bundle bundle = getIntent().getExtras();
        String urlLink = bundle.getString("url");
        if(urlLink != null) {

            NasaEarthUser.LoadImage loadImage = new NasaEarthUser.LoadImage(ivNasa);
            loadImage.execute(urlLink);
        }else{


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

        public LoadImage(ImageView ivNasa) {
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