package com.example.projectactivityrx;

import android.annotation.SuppressLint;
import android.arch.lifecycle.ViewModel;
import android.content.Context;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.squareup.moshi.JsonAdapter;
import com.squareup.moshi.Moshi;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import io.reactivex.subjects.ReplaySubject;




public class LocationViewModel extends ViewModel {


    private LocationManager locationManager;
    private LocationListener locationListener;
    private RequestQueue requestQueue;
    private ReplaySubject<List<Location>> locationList;



    public LocationViewModel() {


    }

    public ReplaySubject<List<Location>> getLocationList() {
        if(locationList==null){
            locationList=ReplaySubject.create();
        }
        return locationList;
    }

    public void setRequestQueue(Context context) {
        this.requestQueue = Volley.newRequestQueue(context);
    }


    @SuppressLint("MissingPermission")
    public void setLocationUpdate(long timeInterval, float distance) {

        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, timeInterval, distance, locationListener);

    }

    @SuppressLint("MissingPermission")
    public void removeLocationUpdate(){
        locationManager.removeUpdates(locationListener);
    }


    public void setLocationManager(LocationManager locationManager){

        this.locationManager = locationManager;
        createLocationListener();

    }


    private void createLocationListener(){
        this.locationListener = new LocationListener() {
            public void onLocationChanged(android.location.Location location) {

                //HTTP REQUEST
                Log.d("LOCATION", "changed");
                madeHTTPrequest();

            }

            public void onStatusChanged(String provider, int status, Bundle extras) {
            }

            public void onProviderEnabled(String provider) {
            }

            public void onProviderDisabled(String provider) {
            }
        };
    }




    private void madeHTTPrequest(){


        String url="https://maps.googleapis.com/maps/api/place/nearbysearch/json?location=44.498948,11.320113&radius=1500&type=museum&key=...";


        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                response -> {


                    Log.d("Response", response.toString());
                    List<Location> list= new ArrayList<>();


                    Moshi moshi= new Moshi.Builder().build();

                    JsonAdapter<Candidates> adapter = moshi.adapter(Candidates.class);

                    try {

                        list= adapter.fromJson(response).getLocations();

                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    locationList.onNext(list);

                }, e -> {

            Log.d("CONNECTION", "error");
            locationList.onError(new Throwable());


        }

        );

        // Add the request to the RequestQueue.
        requestQueue.add(stringRequest);
    }


}
