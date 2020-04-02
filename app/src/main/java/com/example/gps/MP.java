package com.example.gps;

import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;

import java.util.ArrayList;
import java.util.Map;

public class MP extends AppCompatActivity {
    private static final int MY_PERMISSIONS_REQUEST_ACCESS_COARSE_LOCATION = 1;

    ArrayList<Map<String, Object>> data;
    Map<String, Object> m;
    DBHelper dbHelper;
    final String LOG_TAG = "myLogs";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        data = new ArrayList<Map<String, Object>> ();
        dbHelper = new DBHelper(this);
        super.onCreate ( savedInstanceState );
        setContentView ( R.layout.activity_mp );

        SQLiteDatabase db = dbHelper.getWritableDatabase();
        String [] places = new String[10];

        Log.d(LOG_TAG, "--- Rows in mytable: ---");
        // делаем запрос всех данных из таблицы mytable, получаем Cursor
        Cursor c = db.query("myplace", null, null, null, null, null, null);

        // ставим позицию курсора на первую строку выборки
        // если в выборке нет строк, вернется false
        if (c.moveToFirst()) {

            // определяем номера столбцов по имени в выборке
            int idColIndex = c.getColumnIndex("id");
            int  location = c.getColumnIndex("location");
            int latitude = c.getColumnIndex("latitude");
            int longitude = c.getColumnIndex ("longitude");

            do {
                places[c.getInt(idColIndex)]="location = " + c.getString(location)+" "+
                        ", latitude = " + c.getString(latitude)+
                        ", longitude = "+ c.getString ( longitude );

                // получаем значения по номерам столбцов и пишем все в лог
                Log.d(LOG_TAG,
                        "ID = " + c.getInt(idColIndex) +
                                ", location = " + c.getString(location) +
                                ", latitude = " + c.getString(latitude)+
                                ", longitude = "+ c.getString ( longitude ));
                // переход на следующую строку
                // а если следующей нет (текущая - последняя), то false - выходим из цикла
            } while (c.moveToNext());

        } else
            Log.d(LOG_TAG, "0 rows");
        c.close();
        ListView lvMain = (ListView) findViewById(R.id.lvMain);
        CustomArrayAdapter listAdapter = new CustomArrayAdapter(this, places);
        lvMain.setAdapter(listAdapter);

    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if(requestCode == MY_PERMISSIONS_REQUEST_ACCESS_COARSE_LOCATION){
            if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                //abc
            }else{

            }
        }
    }
}
