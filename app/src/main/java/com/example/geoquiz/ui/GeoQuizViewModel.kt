package com.example.geoquiz.ui

import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.geoquiz.GeoQuizApplication
import com.example.geoquiz.data.GeoQuizRepository
import com.example.geoquiz.data.MAX_NO_OF_COUNTRIES
import com.example.geoquiz.data.SCORE_DECREASE
import com.example.geoquiz.data.SCORE_INCREASE
import com.example.geoquiz.model.Country
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.io.IOException


class GeoQuizViewModel(private val geoQuizRepository: GeoQuizRepository) : ViewModel() {
    private val _uiState = MutableStateFlow(GeoQuizUiState())
    val uiState: StateFlow<GeoQuizUiState> = _uiState.asStateFlow()
    var nicknameInput by mutableStateOf("")
        private set
    private lateinit var currentCountry: Country
    private var usedFlagsCountries: MutableSet<Country> = mutableSetOf()
    private var usedCapitalsCountries: MutableSet<Country> = mutableSetOf()
    var flags: MutableList<String> = mutableListOf()
    //val flagIds: MutableList<String> by lazy { getRandomFlags() }
    var countriesList: MutableList<String> = mutableListOf()
    var capitals: MutableList<String> = mutableListOf()
    val clickedButtonIndex: MutableMap<Int, Boolean> = mutableMapOf()
    private val _clickedFlagId = mutableStateOf<String?>(null)
    val clickedFlagId: State<String?> = _clickedFlagId
    private val _countries = mutableStateOf<List<Country>>(emptyList())
    val countries: State<List<Country>> = _countries
    fun updateClickedFlagId(flagId: String?) {
        _clickedFlagId.value = flagId
    }
    fun updateNicknameInput(input: String) {
        nicknameInput = input
    }

    init {
        viewModelScope.launch {
            getCountries()
            initializeMap()
            resetGame()
            getRandomCountriesAndCapitals()
            getRandomFlags()
        }
    }

    suspend fun getCountries() {
        try {
            _countries.value = geoQuizRepository.getCountries()
        } catch (e: IOException) {
            e.printStackTrace()
        } catch (e: HttpException) {
            e.printStackTrace()
        }
    }

    private fun resetGame() {
        usedFlagsCountries.clear()
        usedCapitalsCountries.clear()
        val newCountry = pickRandomCountry(usedFlagsCountries, 0)
        _uiState.value = GeoQuizUiState(
            currentCountry = newCountry.name.common,
            currentCountryIndex = countries.value.indexOfFirst { it == newCountry },
            flag = countries.value.first { it == newCountry }.flag.png,
            isGameOver = false,
        )
    }

    private fun updateGameState(updatedScore: Int) {
        if (usedFlagsCountries.size == MAX_NO_OF_COUNTRIES) {
            _uiState.update { currentState ->
                currentState.copy(
                    isGameOver = true,
                    score = updatedScore
                )
            }
        } else {
            val newCountry = pickRandomCountry(usedFlagsCountries, 0)
            _uiState.update { currentState ->
                currentState.copy(
                    currentCountry = newCountry.name.common,
                    currentCountryIndex = countries.value.indexOfFirst { it == newCountry },
                    flag = countries.value.first { it == newCountry }.flag.png,
                    score = updatedScore
                )
            }
        }
    }

    fun checkImageClicked(clickedFlagId: String) {

        if (clickedFlagId == countries.value[getCountryIndex()].flag.png) {
            val updatedScore = _uiState.value.score.plus(SCORE_INCREASE)
            updateGameState(updatedScore)
        } else {
            val updatedScore = _uiState.value.score.minus(SCORE_DECREASE)
            updateGameState(updatedScore)
        }
        flags = getRandomFlags()
    }

    private fun pickRandomCountry(usedCountries: MutableSet<Country>, game: Int): Country {
        if (game == 0) {
            currentCountry = countries.value.random()
            return if (usedCountries.contains(currentCountry)) {
                pickRandomCountry(usedCountries, 0)
            } else {
                usedCountries.add(currentCountry)
                return currentCountry
            }
        } else {
            val country = countries.value.random()
            return if (usedCountries.contains(country)) {
                pickRandomCountry(usedCountries, 1)
            } else {
                usedCountries.add(country)
                return country
            }
        }
    }

    fun getCountryIndex(): Int {
        return countries.value.indexOfFirst { it == currentCountry }
    }


    private fun getRandomFlags(): MutableList<String> {
        flags.clear()
        flags.add(uiState.value.flag)
        val filteredFlags =
            countries.value.filter { it.flag.png != uiState.value.flag }.map { it.flag.png }
        val randomFlags = filteredFlags.shuffled().take(2)
        flags.addAll(randomFlags)
        flags.shuffle()
        return flags
    }

    private fun getRandomCountriesAndCapitals() {
        countriesList.clear()
        capitals.clear()
        for (i in 0 until MAX_NO_OF_COUNTRIES) {
            val country = pickRandomCountry(usedCapitalsCountries, 1)
            countriesList.add(country.name.common)
            capitals.add(country.capital?.get(0) ?: "/")
        }
        countriesList.shuffle()
        capitals.shuffle()
    }

    fun checkCapitalClicked(capital: String, button: Int) {
        currentCountry =
            countries.value.find { it.name.common == countriesList[uiState.value.countryCount] }!!
        if (capital == (currentCountry.capital?.getOrNull(0) ?: "/")) {
            val updatedScore = _uiState.value.score.plus(SCORE_INCREASE)
            clickedButtonIndex[button] = true
            clickedButtonIndex[uiState.value.countryCount] = true
            updateConnectGameState(updatedScore)
        } else {
            val updatedScore = _uiState.value.score.minus(SCORE_DECREASE)
            updateConnectGameState(updatedScore)
        }
    }

    private fun updateConnectGameState(updatedScore: Int) {
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

    private fun initializeMap() {
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

    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val application =
                    (this[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY] as GeoQuizApplication)
                val geoQuizRepository = application.container.geoQuizRepository
                GeoQuizViewModel(geoQuizRepository = geoQuizRepository)
            }
        }
    }
}