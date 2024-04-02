package com.example.geoquiz.data

import kotlinx.coroutines.flow.Flow

class OfflineHighScoreRepository(private val highScoreDao: HighScoreDao):HighScoreRepository {
    override fun getAllScoresStream(): Flow<List<HighScore>> = highScoreDao.getAllScores()

    override suspend fun insertScore(highScore: HighScore) = highScoreDao.insert(highScore)

}