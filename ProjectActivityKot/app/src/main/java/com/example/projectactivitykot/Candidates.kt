package com.example.projectactivitykot

import com.squareup.moshi.Json

data class Candidates(@field:Json(name="results")var locations: List<Location>)