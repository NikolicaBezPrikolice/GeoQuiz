package com.example.geoquiz.ui

import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.example.geoquiz.data.DataSource.countryList
import com.example.geoquiz.data.MAX_NO_OF_COUNTRIES
import com.example.geoquiz.data.SCORE_DECREASE
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
    private var usedFlagsCountries: MutableSet<Country> = mutableSetOf()
    private var usedCapitalsCountries: MutableSet<Country> = mutableSetOf()
    var flags: MutableList<Int> = mutableListOf()
    val flagIds: List<Int> by lazy { getRandomFlags() }
    var countries: MutableList<String> = mutableListOf()
    var capitals: MutableList<String> = mutableListOf()
    val clickedButtonIndex: MutableMap<Int, Boolean> = mutableMapOf()
    private val _clickedFlagId = mutableStateOf<Int?>(null)
    val clickedFlagId: State<Int?> = _clickedFlagId

    fun updateClickedFlagId(flagId: Int?) {
        _clickedFlagId.value = flagId
    }
    fun updateNicknameInput(input: String) {
        nicknameInput = input
    }

    init {
        initializeMap()
        resetGame()
        getRandomCountriesAndCapitals()
    }

    fun resetGame() {
        usedFlagsCountries.clear()
        usedCapitalsCountries.clear()
        val newCountry = pickRandomCountry(usedFlagsCountries,0)
        _uiState.value = GeoQuizUiState(
            currentCountry = newCountry.name,
            currentCountryIndex = countryList.indexOfFirst { it == newCountry },
            flag = countryList.first { it == newCountry }.flagId,
            isGameOver = false,
        )
    }

    fun updateGameState(updatedScore: Int) {
        if (usedFlagsCountries.size == MAX_NO_OF_COUNTRIES) {
            _uiState.update { currentState ->
                currentState.copy(
                    isGameOver = true,
                    score = updatedScore
                )
            }
        } else {
            val newCountry = pickRandomCountry(usedFlagsCountries,0)
            _uiState.update { currentState ->
                currentState.copy(
                    currentCountry = newCountry.name,
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
            val updatedScore = _uiState.value.score.minus(SCORE_DECREASE)
            updateGameState(updatedScore)
        }
        flags = getRandomFlags()
    }

    private fun pickRandomCountry(usedCountries: MutableSet<Country>,game: Int): Country {
        if(game==0) {
            currentCountry = countryList.random()
            return if (usedCountries.contains(currentCountry)) {
                pickRandomCountry(usedCountries,0)
            } else {
                usedCountries.add(currentCountry)
                return currentCountry
            }
        }
        else{
            val country= countryList.random()
            return if (usedCountries.contains(country)) {
                pickRandomCountry(usedCountries,1)
            } else {
                usedCountries.add(country)
                return country
            }
        }
    }

    fun getCountryIndex(): Int {
        return countryList.indexOfFirst { it == currentCountry }
    }


    fun getRandomFlags(): MutableList<Int> {
        flags.clear()
        flags.add(uiState.value.flag)
        val filteredFlags = countryList.filter { it.flagId != uiState.value.flag }.map { it.flagId }
        val randomFlags = filteredFlags.shuffled().take(2)
        flags.addAll(randomFlags)
        flags.shuffle()
        return flags
    }

    fun getRandomCountriesAndCapitals() {
        countries.clear()
        capitals.clear()
        for (i in 0 until MAX_NO_OF_COUNTRIES) {
            val country = pickRandomCountry(usedCapitalsCountries,1)
            countries.add(country.name)
            capitals.add(country.capital)
        }
        countries.shuffle()
        capitals.shuffle()
    }

    fun checkCapitalClicked(capital: String, button: Int) {
        currentCountry = countryList.find { it.name == countries[uiState.value.countryCount] }!!
        if (capital == currentCountry.capital) {
            val updatedScore = _uiState.value.score.plus(SCORE_INCREASE)
            clickedButtonIndex[button] = true
            clickedButtonIndex[uiState.value.countryCount] = true
            updateConnectGameState(updatedScore)
        } else {
            val updatedScore = _uiState.value.score.minus(SCORE_DECREASE)
            updateConnectGameState(updatedScore)
        }

    }

    fun updateConnectGameState(updatedScore: Int) {
        if (_uiState.value.countryCount == MAX_NO_OF_COUNTRIES - 1) {
            _uiState.update { currentState ->
                currentState.copy(
                    countryCount = currentState.countryCount.inc(),
                    score = updatedScore,
                    isGameOver = true,
                )
            }
        } else {

            _uiState.update { currentState ->
                currentState.copy(
                    countryCount = currentState.countryCount.inc(),
                    score = updatedScore
                )
            }
        }
    }

    fun initializeMap() {
        for (i in 0 until 10) {
            clickedButtonIndex[i] = false
        }
    }

    fun updateScore(score: Int) {
        _uiState.update { currentState ->
            currentState.copy(
                score = score,
            )
        }
    }


}