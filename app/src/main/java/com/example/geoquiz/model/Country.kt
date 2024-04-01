package com.example.geoquiz.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

data class Country(val flagId: Int, val name: String, val capital: String, val population: Int)

@Serializable
data class Country1(
    @SerialName(value = "population")
    val population: Long
)

@Serializable
data class Name(
    @SerialName("common")
    val common: String
)
@Serializable
data class MarsPhoto(
    val id: String,
    @SerialName(value = "img_src")
    val imgSrc: String
)