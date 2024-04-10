package com.example.geoquiz.data

import com.example.geoquiz.model.Country
import com.example.geoquiz.network.GeoQuizApiService

interface GeoQuizRepository {
    suspend fun getCountries(): List<Country>
}

class NetworkGeoQuizRepository(private val geoQuizApiService: GeoQuizApiService) :
    GeoQuizRepository {
    override suspend fun getCountries(): List<Country> = geoQuizApiService.getCountries()

}