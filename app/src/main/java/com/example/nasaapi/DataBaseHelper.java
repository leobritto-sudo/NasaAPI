package com.example.nasaapi;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class DataBaseHelper extends SQLiteOpenHelper {
    public static final String IMG_TABLE = "IMG_TABLE";
    public static final String COLUMN_URL = "URL";
    public static final String COLUMN_ID = "ID";

    public DataBaseHelper(@Nullable Context context) {
        super(context, "nasaimg.db", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String st = "CREATE TABLE " + IMG_TABLE + " (" + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + COLUMN_URL + " TEXT)";
        db.execSQL(st);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public boolean addOne(ImageModel imageModel){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();

        cv.put(COLUMN_URL, imageModel.getURL());

        long insert = db.insert(IMG_TABLE, null, cv);
        if(insert == -1){
            return false;
        }else{
            return true;
        }
    }
}
