package com.example.geoquiz.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface HighScoreDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(score: HighScore)
    @Query("Select * from high_scores ORDER BY score DESC")
    fun getAllScores(): Flow<List<HighScore>>
}