package com.example.projectactivitykot

import android.annotation.SuppressLint
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import android.location.LocationListener
import android.location.LocationManager
import android.os.Bundle
import android.util.Log
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class LocationViewModel () : ViewModel() {

    private lateinit var locationList: MutableLiveData<List<Location>>
    private lateinit var locationManager: LocationManager
    private lateinit var locationListener : LocationListener
    private lateinit var  error: MutableLiveData<Boolean>
    private lateinit var dataLoading: MutableLiveData<Boolean>



    fun getLocationList() : MutableLiveData<List<Location>>{

        if(!::locationList.isInitialized){
            locationList = MutableLiveData()

        }
        return locationList
    }

    fun getError() : MutableLiveData<Boolean>{

        if(!::error.isInitialized){
            error = MutableLiveData()

        }
        return error
    }


    fun getDataLoading(): MutableLiveData<Boolean>{

        if(!::dataLoading.isInitialized){
            dataLoading = MutableLiveData()

        }
        return dataLoading
    }

    @SuppressLint("MissingPermission")
    fun setLocationUpdate(timeInterval: Long, distance: Float){

        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, timeInterval, distance, locationListener)

    }


    fun removeLocationUpdate(){
        locationManager.removeUpdates(locationListener)
    }


    fun setLocationManager(locationManager: LocationManager){

        this.locationManager =locationManager
        createLocationListener()
    }


    fun createLocationListener(){
        this.locationListener = object : LocationListener {
            override fun onLocationChanged(location: android.location.Location) {

                //HTTP REQUEST
                Log.d("LOCATION", "changed")

                val service = RetrofitFactory.makeRetrofitService()

                CoroutineScope(Dispatchers.IO).launch {

                    val response = service.getLocations()

                    withContext(Dispatchers.Main) {

                        if (response.isSuccessful) {

                            //post on livedata
                            dataLoading.postValue(false)

                            locationList.postValue(response.body()!!.locations)

                        } else {
                            //post on error
                            dataLoading.postValue(false)
                            error.postValue(true)
                        }
                    }
                }
            }

            override fun onStatusChanged(provider: String, status: Int, extras: Bundle) {}

            override fun onProviderEnabled(provider: String) {}

            override fun onProviderDisabled(provider: String) {}
        }
    }

}


