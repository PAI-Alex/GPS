package com.example.gps;

import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import androidx.annotation.NonNull;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.view.View;

import java.util.ArrayList;
import java.util.Map;

public class My_Places extends AppCompatActivity {
    private static final int MY_PERMISSIONS_REQUEST_ACCESS_COARSE_LOCATION = 1;
    final String ATTRIBUTE_NAME_TEXT = "Место";
    final String ATTRIBUTE_NAME_IMAGE = "координаты";
    ListView lvSimple;
    SimpleAdapter sAdapter;
    private static final int CM_DELETE_ID = 1;
    ArrayList<Map<String, Object>> data;
    Map<String, Object> m;
    DBHelper dbHelper;
    final String LOG_TAG = "myLogs";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        data = new ArrayList<Map<String, Object>> ();
        dbHelper = new DBHelper(this);
        super.onCreate ( savedInstanceState );
        setContentView ( R.layout.activity_my_pl );
        lvSimple = (ListView) findViewById(R.id.lvSimple);
        lvSimple.setAdapter(sAdapter);
        registerForContextMenu(lvSimple);
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        Log.d(LOG_TAG, "--- Rows in mytable: ---");
        // делаем запрос всех данных из таблицы mytable, получаем Cursor
        Cursor c = db.query("mytable", null, null, null, null, null, null);

        // ставим позицию курсора на первую строку выборки
        // если в выборке нет строк, вернется false
        if (c.moveToFirst()) {

            // определяем номера столбцов по имени в выборке
            int idColIndex = c.getColumnIndex("id");
            int  location = c.getColumnIndex("location");
            int latitude = c.getColumnIndex("latitude");
            int longitude = c.getColumnIndex ("longitude");

            do {
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
        Toolbar toolbar = findViewById ( R.id.toolbar );
        setSupportActionBar ( toolbar );

        FloatingActionButton fab = findViewById ( R.id.fab );
        fab.setOnClickListener ( new View.OnClickListener () {
            @Override
            public void onClick(View view) {
                Snackbar.make ( view , "Replace with your own action" , Snackbar.LENGTH_LONG )
                        .setAction ( "Action" , null ).show ();
            }
        } );

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
