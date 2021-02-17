package com.example.goscratches;


import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.NonNull;

public class myLocationListener implements LocationListener {
    public static Location location;
    Context context;
    public  myLocationListener(Context context){
        this.context = context;
    }
    @Override
    public void onLocationChanged(@NonNull Location location) {
        String Location = "long : " + String.valueOf(location.getLongitude()) + ", lat : " + String.valueOf(location.getLatitude());
        Toast.makeText(context, Location, Toast.LENGTH_LONG).show();
        this.location = location;
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
        Toast.makeText(context, "GPS status is changed", Toast.LENGTH_LONG).show();
    }

    @Override
    public void onProviderEnabled(@NonNull String provider) {
        Toast.makeText(context, "GPS is enabled", Toast.LENGTH_LONG).show();
    }

    @Override
    public void onProviderDisabled(@NonNull String provider) {
        Toast.makeText(context, "GPS is disabled", Toast.LENGTH_LONG).show();
    }
}

