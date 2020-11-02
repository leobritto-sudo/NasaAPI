package com.example.nasaapi;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import java.util.ArrayList;

public class DataBaseHelper extends SQLiteOpenHelper {
    public static final String ADDRESS_TABLE = "ADDRESS_TABLE";
    public static final String IMG_TABLE = "IMG_TABLE";
    public static final String COLUMN_URL = "URL";
    public static final String COLUMN_ADDRESS = "ADDRESS";
    public static final String COLUMN_ID = "ID";
    public static final int VERSION = 1;

    public DataBaseHelper(@Nullable Context context) {
        super(context, "nasaimg.db", null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String st = "CREATE TABLE " + ADDRESS_TABLE + " (" + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + COLUMN_ADDRESS + " TEXT)";
        String str = "CREATE TABLE " + IMG_TABLE + " (" + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + COLUMN_URL + " TEXT)";
        db.execSQL(st);
        db.execSQL(str);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public String selectURL(int id){
        SQLiteDatabase db = getReadableDatabase();
        String st = "SELECT URL FROM IMG_TABLE WHERE ID = " + id;
        Cursor cur = db.rawQuery(st, null);
        String url = "";
        if(cur.moveToFirst()){
            url = cur.getString(cur.getColumnIndex(COLUMN_URL));
        }
        return url;
    }

    public int selectId(String url){
        url = '"' + url + '"';
        SQLiteDatabase db = getReadableDatabase();
        String st = "SELECT ID FROM IMG_TABLE WHERE URL = " + url;
        Cursor cur = db.rawQuery(st, null);
        int id = 0;
        if(cur.moveToFirst()){
            id = cur.getInt(cur.getColumnIndex(COLUMN_ID));
        }
        return id;
    }

    public ArrayList<String> getAllAddresses(){
        ArrayList<String> allAddresses = new ArrayList<String>();
        String st = "SELECT DISTINCT " + COLUMN_ADDRESS + " FROM " + ADDRESS_TABLE;
        Cursor cursor = getReadableDatabase().rawQuery(st, null);

        if(cursor.getCount() > 0){
            cursor.moveToFirst();

            while(!cursor.isAfterLast()){
                allAddresses.add(cursor.getString(0));
                cursor.moveToNext();
            }
        }
        cursor.close();
        return allAddresses;
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

    public boolean addAddress(Address address){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();

        cv.put(COLUMN_ADDRESS, address.getAdress());

        long insert = db.insert(ADDRESS_TABLE, null, cv);
        if(insert == -1){
            return false;
        }else{
            return true;
        }
    }
}
