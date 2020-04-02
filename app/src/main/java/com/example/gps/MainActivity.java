package com.example.gps;

import android.Manifest;
import android.app.Notification;
import android.app.NotificationManager;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.location.Location;

import android.util.Log;
import android.view.View;
import android.widget.*;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.core.content.ContextCompat;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.tasks.OnSuccessListener;



public class MainActivity extends AppCompatActivity {

    private static final int MY_PERMISSIONS_REQUEST_ACCESS_COARSE_LOCATION = 1;
    private NotificationManagerCompat notificationManager;

    Button fetch, btnTask;
    Button btnAdd;
    Button addTask;
    TextView user_location1, user_location2;
    private FusedLocationProviderClient mFusedLocationClient;
    DBHelper dbHelper;
    EditText loc, nameTask;
    Double latitude;
    Double longitude;
    final String LOG_TAG = "myLogs";
    Button btnMyPl;
    String [] places = new String[10];




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        notificationManager = NotificationManagerCompat.from(this);
        dbHelper = new DBHelper(this);
        fetch = findViewById(R.id.fetch_location);
        user_location1 = findViewById(R.id.user_location);
        user_location2 = findViewById(R.id.ul2);
        btnAdd =  findViewById(R.id.btnAdd);
        loc =  findViewById ( R.id.location );
        addTask = findViewById ( R.id.addTask );
        nameTask = findViewById ( R.id.name );
        final Spinner spinner =  findViewById(R.id.spinner);

        btnTask=findViewById ( R.id.button );
        btnTask.setOnClickListener ( new View.OnClickListener () {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, MyTask.class));
            }
        } );
        btnMyPl =  findViewById(R.id.read);
        btnMyPl.setOnClickListener( new View.OnClickListener () {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, MP.class));
            }
        } );
        addTask.setOnClickListener ( new View.OnClickListener () {
            @Override
            public void onClick(View v) {
                ContentValues cv = new ContentValues();
                String task= nameTask.getText ().toString ();
                String locat= spinner.getSelectedItem().toString ();
                SQLiteDatabase db = dbHelper.getWritableDatabase();
                Log.d(LOG_TAG, "--- Insert in mytask: ---");
                cv.put ( "task", task );
                cv.put ( "location", locat );
                // вставляем запись и получаем ее ID
                long rowID = db.insert("mytask", null, cv);
                Log.d(LOG_TAG, "row inserted, ID = " + rowID);

            }
        } );

        btnAdd.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                sendOnChannel1();
                ContentValues cv = new ContentValues();
                String locat = loc.getText().toString();
                String latit= latitude.toString ();
                String longi = longitude.toString ();

                SQLiteDatabase db = dbHelper.getWritableDatabase();
                Log.d(LOG_TAG, "--- Insert in mytable: ---");
                // подготовим данные для вставки в виде пар: наименование столбца - значение

                cv.put("location", locat);
                cv.put("latitude", latit );
                cv.put ( "longitude", longi );
                // вставляем запись и получаем ее ID
                long rowID = db.insert("myplace", null, cv);
                Log.d(LOG_TAG, "row inserted, ID = " + rowID);
                spinner.setAdapter ( getPlace () );
                // заголовок
                spinner.setPrompt("Мои места");
              //  spinner.setSelection(2);
                spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener () {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view,
                                               int position, long id){

                        spinner.setSelection ( position );
                    }
                    public void onNothingSelected(AdapterView<?> parent) {
                    }
                });


            }
        });
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

        fetch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                fetchLocation();


            }
        });

    }

    private void fetchLocation() {


        if (ContextCompat.checkSelfPermission(MainActivity.this,
                Manifest.permission.ACCESS_COARSE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

            // Permission is not granted
            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(MainActivity.this,
                    Manifest.permission.ACCESS_COARSE_LOCATION)) {
                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.

                new AlertDialog.Builder(this)
                        .setTitle("Required Location Permission")
                        .setMessage("You have to give this permission to acess this feature")
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                ActivityCompat.requestPermissions(MainActivity.this,
                                        new String[]{Manifest.permission.ACCESS_COARSE_LOCATION},
                                        MY_PERMISSIONS_REQUEST_ACCESS_COARSE_LOCATION);
                            }
                        })
                        .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.dismiss();
                            }
                        })
                        .create()
                        .show();


            } else {
                // No explanation needed; request the permission
                ActivityCompat.requestPermissions(MainActivity.this,
                        new String[]{Manifest.permission.ACCESS_COARSE_LOCATION},
                        MY_PERMISSIONS_REQUEST_ACCESS_COARSE_LOCATION);

                // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
                // app-defined int constant. The callback method gets the
                // result of the request.
            }
        } else {
            // Permission has already been granted
            mFusedLocationClient.getLastLocation()
                    .addOnSuccessListener(this, new OnSuccessListener<Location>() {
                        @Override
                        public void onSuccess(Location location) {
                            // Got last known location. In some rare situations this can be null.
                            if (location != null) {
                                // Logic to handle location object
                                 latitude = location.getLatitude();
                                 longitude = location.getLongitude();

                                user_location1.setText("="+latitude);
                                user_location2.setText ("\nLongitude = " + longitude);


                            }
                        }
                    });

        }

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
    public  ArrayAdapter<String> getPlace(){


        DBHelper dbHelper;
        dbHelper = new DBHelper(this);
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        for(int i =0; i<10; i++){
            places[i]=" ";
        }

        Cursor c = db.query("myplace", null, null, null, null, null, null);

        // ставим позицию курсора на первую строку выборки
        // если в выборке нет строк, вернется false
        if (c.moveToFirst()) {

            // определяем номера столбцов по имени в выборке
            int idColIndex = c.getColumnIndex("id");
            int  location = c.getColumnIndex("location");


            do {
                places[c.getInt(idColIndex)]=c.getString(location);



            } while (c.moveToNext());

        } else {
            Log.d ( LOG_TAG , "0 rows" );
            places[0]="Добавьте места";
        }
        c.close();

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, places);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        return adapter;
    }
    public void sendOnChannel1() {


        Notification notification = new NotificationCompat.Builder(this, App.CHANNEL_1_ID)
                .setSmallIcon(R.drawable.ic_launcher_background)
                .setContentTitle("Напонимание")
                .setContentText("Покормить любимого ❤")
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setCategory(NotificationCompat.CATEGORY_MESSAGE)
                .build();

        notificationManager.notify(1, notification);
    }


}