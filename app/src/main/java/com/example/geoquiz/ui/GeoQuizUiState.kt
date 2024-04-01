package com.example.geoquiz.ui

import androidx.annotation.DrawableRes
import com.example.geoquiz.data.DataSource.countryList

data class GeoQuizUiState(
    val currentCountryIndex: Int = 0,
    val currentCountry: String = countryList[currentCountryIndex].name,
    @DrawableRes val flag: Int = countryList[currentCountryIndex].flagId,
    val isGameOver: Boolean = false,
    val countryCount: Int = 0,
    val score: Int = 0,
    val population: Long=0
)
