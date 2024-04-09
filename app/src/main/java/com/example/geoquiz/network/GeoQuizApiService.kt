package com.example.geoquiz.network

import com.example.geoquiz.model.Country
import retrofit2.http.GET

interface GeoQuizApiService {
    @GET("all")
    suspend fun getCountries(): List<Country>
}