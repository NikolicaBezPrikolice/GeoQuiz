package com.example.geoquiz.ui


import android.content.ContentValues.TAG
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.geoquiz.GeoQuizApplication
import com.example.geoquiz.data.HighScore
import com.example.geoquiz.data.HighScoreRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class HighScoreViewModel(private val highScoreRepository: HighScoreRepository) : ViewModel() {

    var gotScore = MutableStateFlow<HighScore?>(null)

    fun getScore(name: String) {
        viewModelScope.launch {
            try {
                highScoreRepository.getScoreStream(name).collect {
                    gotScore.value = it
                }
            } catch (e: Exception) {
                Log.e(TAG, "Error getting score for $name: ${e.message}")
            }
        }
    }

    suspend fun saveHighScore(name: String, score: Int) {
        val highScore = HighScore(name = name, score = score)
        highScoreRepository.insertScore(highScore)
    }

    val highScoresUiState: StateFlow<HighScoresUiState> =
        highScoreRepository.getAllScoresStream()
            .map { HighScoresUiState(it) }
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5_000L),
                initialValue = HighScoresUiState()
            )

    suspend fun deleteAllItems() {
        highScoreRepository.deleteAllScores()
    }

    suspend fun updateOneScore(existingScore: HighScore, newScoreValue: Int) {

        val updatedScore = HighScore(existingScore.id, existingScore.name, newScoreValue)
        highScoreRepository.updateScore(updatedScore)

    }

    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val application =
                    (this[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY] as GeoQuizApplication)
                val highScoreRepository = application.container.highScoreRepository
                HighScoreViewModel(highScoreRepository = highScoreRepository)
            }
        }
    }
}

data class HighScoresUiState(val highScoreList: List<HighScore> = listOf())

data class HighScoreUiState(val score: HighScore? = null)