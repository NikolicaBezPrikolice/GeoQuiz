package com.example.geoquiz.ui


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

class HighScoreViewModel(private val highScoreRepository: HighScoreRepository):ViewModel(){
    private val _uiState = MutableStateFlow(HighScoreUiState())
    val uiState: StateFlow<HighScoreUiState> = _uiState.asStateFlow()
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
    fun getScoreByName(name: String) =highScoreRepository.getScoreStream(name)

    suspend fun deleteAllItems() {
        highScoreRepository.deleteAllScores()
    }

    suspend fun updateOneScore(existingScore: StateFlow<HighScoreUiState>, newScoreValue: Int) {

            val updatedScore =HighScore(existingScore.value.score!!.id,existingScore.value.score!!.name,newScoreValue)
        highScoreRepository.updateScore(updatedScore)

    }
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

data class HighScoreUiState(val score: HighScore? = null)