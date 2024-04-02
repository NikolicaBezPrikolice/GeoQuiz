package com.example.geoquiz.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.geoquiz.GeoQuizApplication
import com.example.geoquiz.data.HighScore
import com.example.geoquiz.data.HighScoreRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn

class HighScoreViewModel(private val highScoreRepository: HighScoreRepository):ViewModel(){

    suspend fun saveHighScore(name: String, score: Int) {
        val highScore = HighScore(name = name, score = score)
        highScoreRepository.insertScore(highScore)
    }

    val highScoreUiState: StateFlow<HighScoresUiState> =
        highScoreRepository.getAllScoresStream().map { HighScoresUiState(it) }
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5_000L),
                initialValue = HighScoresUiState()
            )

    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val application = (this[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY] as GeoQuizApplication)
                val highScoreRepository = application.container.highScoreRepository
                HighScoreViewModel(highScoreRepository = highScoreRepository)
            }
        }
    }
}

data class HighScoresUiState(val highScoreList: List<HighScore> = listOf())