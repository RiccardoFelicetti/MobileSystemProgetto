package com.example.projectactivitykot

import retrofit2.Response
import retrofit2.http.GET

interface RetrofitService{

    @GET("/maps/api/place/nearbysearch/json?location=44.498948,11.320113&radius=1500&type=museum&key=...")

    suspend fun getLocations() : Response<Candidates>
}