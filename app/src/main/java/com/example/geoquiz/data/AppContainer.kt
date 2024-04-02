package com.example.geoquiz.data

import android.content.Context

interface AppContainer{
    val highScoreRepository: HighScoreRepository
}
class AppDataContainer(private val context: Context): AppContainer {
    override val highScoreRepository: HighScoreRepository by lazy{
     OfflineHighScoreRepository(GeoQuizDatabase.getDatabase(context).highScoreDao())
    }
}