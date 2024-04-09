package com.example.geoquiz.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Country(
    @SerialName("name")
    val name: Name,
    @SerialName("capital")
    val capital: List<String>? = null,
    @SerialName("flags")
    val flag: Flags,
    @SerialName("population")
    val population: Long
)


@Serializable
data class Name(
    @SerialName("common")
    val common: String,
)

@Serializable
data class Flags(
    @SerialName("png")
    val png: String
)