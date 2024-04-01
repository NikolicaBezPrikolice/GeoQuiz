package com.example.geoquiz.data

import com.example.geoquiz.model.Country
import com.example.geoquiz.model.Country1
import com.example.geoquiz.network.GeoQuizApiService

interface GeoQuizRepository{
    suspend fun getCountryName(): List<Country1>
}

class NetworkGeoQuizRepository(private val geoQuizApiService: GeoQuizApiService):GeoQuizRepository{
    override suspend fun getCountryName(): List<Country1> = geoQuizApiService.getName()

}