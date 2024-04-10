package com.example.geoquiz.data

import android.content.Context
import com.example.geoquiz.network.GeoQuizApiService
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import retrofit2.Retrofit

interface AppContainer {
    val highScoreRepository: HighScoreRepository
    val geoQuizRepository: GeoQuizRepository
}

class AppDataContainer(private val context: Context) : AppContainer {
    private val BASE_URL =
        "https://restcountries.com/v3.1/"

    private val json = Json { ignoreUnknownKeys = true }

    private val retrofit = Retrofit.Builder()
        .addConverterFactory(json.asConverterFactory("application/json".toMediaType()))
        .baseUrl(BASE_URL)
        .build()

    private val retrofitService: GeoQuizApiService by lazy {
        retrofit.create(GeoQuizApiService::class.java)
    }

    override val geoQuizRepository: GeoQuizRepository by lazy {
        NetworkGeoQuizRepository(retrofitService)
    }

    override val highScoreRepository: HighScoreRepository by lazy {
        OfflineHighScoreRepository(GeoQuizDatabase.getDatabase(context).highScoreDao())
    }
}