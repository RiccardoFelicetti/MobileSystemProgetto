package com.example.projectactivityld;

import android.Manifest;
import android.annotation.SuppressLint;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.squareup.moshi.JsonAdapter;
import com.squareup.moshi.Moshi;
import com.squareup.moshi.Types;

import org.xml.sax.Locator;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class LocationViewModel extends ViewModel {

    private MutableLiveData<List<Location>> locationList;
    private LocationManager locationManager;
    private static  LocationListener locationListener;
    private RequestQueue requestQueue;
    private  MutableLiveData<Boolean> error;
    private MutableLiveData<Boolean> dataloading;


     public LocationViewModel() {


    }


    public LiveData<Boolean> getDataLoading(){

         if (dataloading == null) {
            dataloading = new MutableLiveData<>();
        }
        return dataloading;

    }

     public LiveData<Boolean> getError(){

         if(error == null){
            error= new MutableLiveData<>();
        }
        return error;
     }

     public LiveData<List<Location>> getLocationList() {

         if (locationList == null) {
            locationList = new MutableLiveData<>();
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


          dataloading.postValue(true);

          StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                    response -> {

                        List<Location> list = new ArrayList<>();

                        Moshi moshi = new Moshi.Builder().build();

                        JsonAdapter<Candidates> adapter = moshi.adapter(Candidates.class);

                        dataloading.postValue(false);

                        try {

                            list = adapter.fromJson(response).getLocations();

                        } catch (IOException e) {
                            e.printStackTrace();
                        }

                        locationList.postValue(list);

                }, e -> {

                    Log.d("CONNECTION", "error");
                    dataloading.postValue(false);
                    error.postValue(true);
          }

        );

        // Add the request to the RequestQueue.
        requestQueue.add(stringRequest);
    }


}
