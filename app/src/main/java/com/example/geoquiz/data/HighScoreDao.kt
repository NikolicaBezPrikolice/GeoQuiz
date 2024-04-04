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
    @Query("Select * from high_scores ORDER BY score DESC LIMIT 20")
    fun getAllScores(): Flow<List<HighScore>>
    @Query("DELETE FROM high_scores")
    suspend fun delete()

    @Query("SELECT * from high_scores WHERE name= :name")
    fun getItem(name: String): Flow<HighScore>

    @Query("UPDATE high_scores SET score = :newScore WHERE name = :name")
    fun updateScoreByName(name: String, newScore: Int)

}