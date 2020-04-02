package com.example.gps;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DBHelper extends SQLiteOpenHelper {
    final String LOG_TAG = "myLogs";
    private SQLiteDatabase mDB;

    public DBHelper(Context context) {
        // конструктор суперкласса
        super(context, "myDB", null, 2);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        Log.d(LOG_TAG, "--- onCreate database ---");
        // создаем таблицу с полями
        db.execSQL("create table mytable ("
                + "id integer primary key autoincrement,"
                + "location text,"
                + "latitude text,"
                + "longitude text"+ ");");
        db.execSQL("create table mytask ("
                + "id integer primary key autoincrement,"
                + "location text,"
                + "task text "+");");
//                +" FOREIGN KEY(location) REFERENCES mytable(location)"+ ");");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("create table myplace ("
                + "id integer primary key autoincrement,"
                + "location text,"
                + "latitude text,"
                + "longitude text"+ ");");
        db.execSQL("create table mytask ("
                + "id integer primary key autoincrement,"
                + "location text,"
                + "task text, "
        +" FOREIGN KEY(location) REFERENCES myplace(location)"+ ");");
        db.execSQL("DROP TABLE mytable;");
    }
}
