package com.example.geoquiz.ui

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.example.geoquiz.data.DataSource.countryList
import com.example.geoquiz.data.MAX_NO_OF_COUNTRIES
import com.example.geoquiz.data.SCORE_INCREASE
import com.example.geoquiz.model.Country
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class GeoQuizViewModel : ViewModel() {
    private val _uiState = MutableStateFlow(GeoQuizUiState())
    val uiState: StateFlow<GeoQuizUiState> = _uiState.asStateFlow()

    var nicknameInput by mutableStateOf("")
        private set

    private lateinit var currentCountry: Country
    private var usedCountries: MutableSet<Country> = mutableSetOf()
    var flags: MutableList<Int> = mutableListOf()
    val flagIds: List<Int> by lazy { getRandomFlags() }
    fun updateNicknameInput(input: String) {
        nicknameInput = input
    }

    init {
        resetGame()
    }

    /*
     * Re-initializes the game data to restart the game.
     */
    fun resetGame() {
        usedCountries.clear()
        val newCountry = pickRandomCountry()
        _uiState.value = GeoQuizUiState(
            currentCountry = newCountry.name,
            currentCountryIndex = countryList.indexOfFirst { it == newCountry },
            flag = countryList.first { it == newCountry }.flagId
        )
    }

    fun updateGameState(updatedScore: Int) {
        if (usedCountries.size == MAX_NO_OF_COUNTRIES) {
            //Last round in the game, update isGameOver to true, don't pick a new word
            _uiState.update { currentState ->
                currentState.copy(
                    isGameOver = true,
                    score = updatedScore
                )
            }
        } else {
            val newCountry = pickRandomCountry()
            _uiState.update { currentState ->
                currentState.copy(
                    currentCountry = newCountry.name,
                    countryCount = currentState.countryCount.inc(),
                    currentCountryIndex = countryList.indexOfFirst { it == newCountry },
                    flag = countryList.first { it == newCountry }.flagId,
                    score = updatedScore
                )
            }
        }
    }

    fun checkImageClicked(clickedFlagId: Int) {
        if (clickedFlagId == countryList[getCountryIndex()].flagId) {
            val updatedScore = _uiState.value.score.plus(SCORE_INCREASE)
            updateGameState(updatedScore)
        } else {
            val updatedScore = _uiState.value.score.plus(2)
            updateGameState(updatedScore)
        }
        flags = getRandomFlags()
    }

    private fun pickRandomCountry(): Country {
        currentCountry = countryList.random()
        return if (usedCountries.contains(currentCountry)) {
            pickRandomCountry()
        } else {
            usedCountries.add(currentCountry)
            return currentCountry
        }
    }

    private fun getCountryIndex(): Int {
        return countryList.indexOfFirst { it == currentCountry }
    }


    fun getRandomFlags(): MutableList<Int> {
        flags.clear()
        flags.add(uiState.value.flag)
        flags.add(countryList.random().flagId)
        flags.shuffle()
        return flags
    }

}