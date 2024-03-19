package com.example.geoquiz.data

import com.example.geoquiz.R
import com.example.geoquiz.model.Country

object DataSource {
    val countryList= listOf(
        Country(R.drawable.flag_of_armenia, "Armenia", "Yerevan", 5000000),
        Country(R.drawable.flag_of_ethiopia, "Ethiopia", "Adis Ababa", 100000000),
        Country(R.drawable.mexico, "Mexico", "Mexico City", 150000000),
        Country(R.drawable.flag_of_serbia_svg, "Serbia", "Belgrade", 7000000)
    )
}