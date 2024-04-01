package com.example.geoquiz.data

import com.example.geoquiz.model.Country
import com.example.geoquiz.network.GeoQuizApiService
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import retrofit2.Retrofit
import retrofit2.http.GET
import retrofit2.http.Path

interface AppContainer{
    val geoQuizRepository: GeoQuizRepository
}

class DefaultAppContainer: AppContainer{
    private val BASE_URL=
        "https://restcountries.com/v3.1/"

    private val retrofit= Retrofit.Builder()
        .addConverterFactory(Json.asConverterFactory("application/json".toMediaType()))
        .baseUrl(BASE_URL)
        .build()



       private val retrofitService: GeoQuizApiService by lazy{
            retrofit.create(GeoQuizApiService::class.java)
    }
    override val geoQuizRepository: GeoQuizRepository by lazy{
        NetworkGeoQuizRepository(retrofitService)
    }


}