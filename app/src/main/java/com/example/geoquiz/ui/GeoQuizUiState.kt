package com.example.geoquiz.ui

data class GeoQuizUiState(
    val currentCountryIndex: Int = 0,
    val currentCountry: String = "",
    val flag: String = "",
    val isGameOver: Boolean = false,
    val countryCount: Int = 0,
    val score: Int = 0,
)
