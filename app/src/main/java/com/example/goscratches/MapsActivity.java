package com.example.goscratches;

import androidx.fragment.app.FragmentActivity;

import android.content.Context;
import android.location.Location;
import android.os.Bundle;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;
import java.util.Random;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    //list of cherry and super cherry
    static ArrayList<CherryContent> cherryContents = new ArrayList<>();
    private GoogleMap mMap;
    shareRef sharedRef;
    Context context;
    int zoomFlag = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        sharedRef = new shareRef(shareRef.context);
        context = this;

//        LoadCherry();
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // Add a marker in Sydney and move the camera
        myThread th = new myThread();
        th.start();
    }
    Location oldLocation;
    class myThread extends Thread{
        myThread(){
            oldLocation = new Location("start");
            oldLocation.setLatitude(0);
            oldLocation.setLongitude(0);
        }
        @Override
        public void run() {
            double distance = 0;

            while(true){
                try {
                    Thread.sleep(5000);
                    if(oldLocation.distanceTo(myLocationListener.location) == 0 )
                        continue;
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                distance = oldLocation.distanceTo(myLocationListener.location);
                if(distance>10000){
                    distance = 0;
                }
                sharedRef.saveData(distance);
                oldLocation = myLocationListener.location;


                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        mMap.clear();
                        if(myLocationListener.location != null) {
                            LatLng mark = new LatLng(myLocationListener.location.getLatitude(), myLocationListener.location.getLongitude());
                            mMap.addMarker(new MarkerOptions().position(mark).title("I'm here").icon(BitmapDescriptorFactory.fromResource(R.drawable.pointer)));

                            int i=0;
                            while( i<cherryContents.size() ){
                                CherryContent contents = cherryContents.get(i);
                                LatLng cherries = new LatLng(contents.location.getLatitude(), contents.location.getLongitude());
                                if(contents.name.equalsIgnoreCase("cherry")) {
                                    mMap.addMarker(new MarkerOptions().position(cherries).title(contents.name)
                                            .snippet(contents.value + " cherries")
                                            .icon(BitmapDescriptorFactory.fromResource(R.drawable.cherry)));
                                }
                                else{
                                    mMap.addMarker(new MarkerOptions().position(cherries).title(contents.name)
                                            .snippet(contents.value + " cherries")
                                            .icon(BitmapDescriptorFactory.fromResource(R.drawable.super_cherry)));
                                }

                                if(myLocationListener.location.distanceTo(contents.location)<30 && contents.name.equalsIgnoreCase("super cherry")){
                                    sharedRef.saveData(1,cherryContents.get(i).value);
                                    cherryContents.remove(i);
                                    Toast.makeText(context, "super cherry collected", Toast.LENGTH_LONG).show();
                                    i--;
                                }
                                else if(myLocationListener.location.distanceTo(contents.location)<20 && contents.name.equalsIgnoreCase("cherry")){
                                    sharedRef.saveData(cherryContents.get(i).value);
                                    Toast.makeText(context, "cherry collected", Toast.LENGTH_LONG).show();
                                    cherryContents.remove(i);
                                    i--;
                                }
                                i++;
                            }
                            if(zoomFlag == 0){
                                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(mark,16));
                                zoomFlag = 1;
                            }
                            else {
                                mMap.moveCamera(CameraUpdateFactory.newLatLng(mark));
                            }
                        }
                    }
                });

            }
        }
    }



//    void LoadCherry(){
//        cherryContents.add(new CherryContent("cherry", 5, R.drawable.cherry, false, 22.576881666666665, 88.37330666666666));
//        cherryContents.add(new CherryContent("cherry", 5,  R.drawable.cherry, false, 22.577565, 88.37289333333334));
//        cherryContents.add(new CherryContent("cherry", 5,  R.drawable.cherry, false, 22.577575, 88.3725555));
//        cherryContents.add(new CherryContent("cherry", 5,  R.drawable.cherry, false, 22, 88));
//        cherryContents.add(new CherryContent("cherry", 5,  R.drawable.cherry, false, 22, 89));
//        double d = cherryContents.get(3).location.distanceTo(cherryContents.get(4).location);
//        Toast.makeText(this, d + "", Toast.LENGTH_LONG).show();
//    }
}