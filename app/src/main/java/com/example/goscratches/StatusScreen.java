package com.example.goscratches;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.Random;

public class StatusScreen extends AppCompatActivity {


    final private int REQUEST_CODE_ASK_PERMISSIONS = 123;
    LocationManager lm;
    myLocationListener locationListener;
    EditText etName, etAge, etCherry, etDistance, etSuperCherry;
    shareRef sharedRef;
    Button buSetEdit;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_status_screen);

        CheckUserPermissions();// Check for GPS permissions

        defineViews();// initialise EditText, Button and SharedPreferences

        collectData();// Collect sharedRef data and set data in the views

    }
    @Override
    protected void onRestart() {
        super.onRestart();
        collectData();// Collect sharedRef data and set data in the views
//        Toast.makeText(this,"onRestart",Toast.LENGTH_LONG).show();
    }

    @Override
    protected void onResume() {
        super.onResume();
        collectData();// Collect sharedRef data and set data in the views
//        Toast.makeText(this,"onResume",Toast.LENGTH_LONG).show();
    }

    void defineViews(){
        etName = (EditText)findViewById(R.id.etName);
        etAge = (EditText)findViewById(R.id.etAge);
        etCherry = (EditText)findViewById(R.id.etCherry);
        etDistance = (EditText)findViewById(R.id.etDistance);
        etSuperCherry = (EditText)findViewById(R.id.etSuperCherry);
        buSetEdit = (Button)findViewById(R.id.buSetEdit);
        sharedRef = new shareRef(this);
    }
    void collectData(){
        StoredData sd = sharedRef.loadData();
        etName.setText(sd.Name);
        etAge.setText(String.valueOf(sd.Age));
        etCherry.setText(String.valueOf(sd.Cherry));
        etDistance.setText(String.valueOf(sd.Distance));
        etSuperCherry.setText(String.valueOf(sd.SuperCherry));
    }
    void CheckUserPermissions() {
        if (Build.VERSION.SDK_INT >= 23) {
            if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) !=
                    PackageManager.PERMISSION_GRANTED ||
                    ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) !=
                            PackageManager.PERMISSION_GRANTED) {
                requestPermissions(
                        new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION},
                        REQUEST_CODE_ASK_PERMISSIONS);
                return;
            }
        }

        getLocation();// init the contact list

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case REQUEST_CODE_ASK_PERMISSIONS:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    getLocation();// init the contact list
                } else {
                    // Permission Denied
                    Toast.makeText(this, "you denied location access", Toast.LENGTH_SHORT)
                            .show();
                }
                break;
            default:
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }

    @SuppressLint("MissingPermission")
    void getLocation() {
        locationListener = new myLocationListener(this);
        lm =(LocationManager)this.getSystemService(LOCATION_SERVICE);
        myLocationListener.location = lm.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        lm.requestLocationUpdates(LocationManager.GPS_PROVIDER,5000,10, locationListener);
        generateCherries();

    }

    public void setEdit(View view) {

        if(buSetEdit.getText().toString().equalsIgnoreCase("Edit")){
            etName.setEnabled(true);
            etAge.setEnabled(true);
            buSetEdit.setText("save");
        }
        else{
            sharedRef.saveData(etName.getText().toString(), Integer.parseInt(etAge.getText().toString()));
            etName.setEnabled(false);
            etAge.setEnabled(false);
            buSetEdit.setText("edit");
            Toast.makeText(this, "Data saved successfully", Toast.LENGTH_LONG).show();
        }
    }

    void generateCherries(){
        MapsActivity.cherryContents.clear();
        int cherries = new Random().nextInt(30);
//        cherries = 0;
//        Toast.makeText(this, String.valueOf(cherries), Toast.LENGTH_LONG).show();
        int superCherries = new Random().nextInt(20);
//        int superCherries = 40;
//        Toast.makeText(this, String.valueOf(superCherries), Toast.LENGTH_LONG).show();

        Random latRandom = new Random();
        Random lonRandom = new Random();
        Random cherryRandom = new Random();

        double latitude;
        double longitude;
        int cherryValue;

        double la = myLocationListener.location.getLatitude();
        double lo = myLocationListener.location.getLongitude();

        for(int i=0; i<cherries; i++) {
            latitude = latRandom.nextInt(1000000);
            longitude = lonRandom.nextInt(1000000);
            if(latitude%2 == 0)
                latitude = la + (latitude/100000000);
            else
                latitude = la - (latitude/100000000);
            if(longitude%2 == 0)
                longitude = lo - (longitude/100000000);
            else
                longitude = lo + (longitude/100000000);

            cherryValue = cherryRandom.nextInt(9) + 1;

            MapsActivity.cherryContents.add(new CherryContent("cherry", cherryValue, R.drawable.cherry, latitude, longitude));
        }

        for(int i=0; i<superCherries; i++) {
            latitude = latRandom.nextInt(5000000);
            longitude = lonRandom.nextInt(5000000);
            if(latitude<100000)
                latitude = latitude + 100000;
            if(longitude<100000)
                longitude = longitude + 100000;

            if(latitude%2 != 0)
                latitude = la + (latitude/10000000);
            else
                latitude = la - (latitude/10000000);
            if(longitude%2 != 0)
                longitude = lo + (longitude/10000000);
            else
                longitude = lo - (longitude/10000000);

            cherryValue = (cherryRandom.nextInt(9) + 1) * 50;

            MapsActivity.cherryContents.add(new CherryContent("super cherry", cherryValue, R.drawable.cherry, latitude, longitude));
            Log.d("cherry debugging", String.valueOf(MapsActivity.cherryContents.size()));
        }

    }



    public void nextLayout(View view) {

        Intent intent = new Intent(this, MapsActivity.class);
        startActivity(intent);
    }
}