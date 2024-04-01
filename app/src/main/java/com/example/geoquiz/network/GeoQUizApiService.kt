package com.example.geoquiz.network

import com.example.geoquiz.model.Country
import com.example.geoquiz.model.Country1
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.create
import retrofit2.http.GET
import retrofit2.http.Path


interface  GeoQuizApiService{
    @GET("all")
    suspend fun getName(): List<Country1>
}

