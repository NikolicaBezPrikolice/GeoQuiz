package com.example.geoquiz.data

import kotlinx.coroutines.flow.Flow

interface HighScoreRepository {
    fun getAllScoresStream(): Flow<List<HighScore>>

    suspend fun insertScore(highScore: HighScore)
}