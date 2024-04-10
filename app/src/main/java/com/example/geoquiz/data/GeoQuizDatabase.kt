package com.example.geoquiz.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [HighScore::class], version = 1, exportSchema = false)
abstract class GeoQuizDatabase : RoomDatabase() {

    abstract fun highScoreDao(): HighScoreDao

    companion object {
        @Volatile
        private var Instance: GeoQuizDatabase? = null

        fun getDatabase(context: Context): GeoQuizDatabase {
            return Instance ?: synchronized(this) {
                Room.databaseBuilder(context, GeoQuizDatabase::class.java, "highScore_Database")
                    .fallbackToDestructiveMigration()
                    .build()
                    .also { Instance = it }
            }
        }
    }
}