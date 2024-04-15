package com.example.geoquiz.ui

data class GeoQuizUiState(
    val currentCountryIndex: Int = 0,
    val currentCountry: String = "",
    val flag: String = "",
    val isGame1Over: Boolean = false,
    val isGame2Over: Boolean = false,
    val countryCount: Int = 0,
    val score: Int = 0,
)
