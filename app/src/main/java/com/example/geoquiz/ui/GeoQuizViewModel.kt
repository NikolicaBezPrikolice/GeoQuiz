package com.example.geoquiz.ui

import android.content.ContentValues.TAG
import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.geoquiz.GeoQuizApplication
import com.example.geoquiz.data.DataSource.countryList
import com.example.geoquiz.data.GeoQuizRepository
import com.example.geoquiz.data.MAX_NO_OF_COUNTRIES
import com.example.geoquiz.data.NetworkGeoQuizRepository
import com.example.geoquiz.data.SCORE_INCREASE
import com.example.geoquiz.model.Country
import com.example.geoquiz.model.Country1
import com.example.geoquiz.model.MarsPhoto
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class GeoQuizViewModel(private val geoQuizRepository: GeoQuizRepository) : ViewModel() {
    private val _uiState = MutableStateFlow(GeoQuizUiState())
    val uiState: StateFlow<GeoQuizUiState> = _uiState.asStateFlow()

    var nicknameInput by mutableStateOf("")
        private set

    private lateinit var currentCountry: Country
    private var usedCountries: MutableSet<Country> = mutableSetOf()
    var flags: MutableList<Int> = mutableListOf()
    val flagIds: List<Int> by lazy { getRandomFlags() }
    var countries: MutableList<String> = mutableListOf()
    var capitals: MutableList<String> = mutableListOf()
    val clickedButtonIndex: MutableMap<Int, Boolean> = mutableMapOf()
    fun updateNicknameInput(input: String) {
        nicknameInput = input
    }
    private val _countryData = MutableLiveData<Country1>()
    val countryData: LiveData<Country1> = _countryData

    fun getCountryData() {
        viewModelScope.launch {
            try {
                val country = geoQuizRepository.getCountryName()
                _uiState.value=(GeoQuizUiState(population = country[0].population))
            } catch (e: Exception) {
                // Handle the error, e.g., show an error message
                Log.e(TAG, "Error getting country data", e)
            }
        }
    }
    init {
        //initializeMap()
        //resetGame()
        //getRandomCountriesAndCapitals()
        getCountryData()
    }


    private fun getCountryNameById() {
        viewModelScope.launch {
            try {
                val result = geoQuizRepository.getCountryName()
                _uiState.value = GeoQuizUiState(population = result[0].population)
            } catch (e: Exception) {
                // Handle the error, e.g., log it or show a toast
                Log.e(TAG, "Error fetching country name", e)
            }
        }
    }

    companion object{
        val Factory: ViewModelProvider.Factory=viewModelFactory{
            initializer {
                val application=(this[APPLICATION_KEY] as GeoQuizApplication)
                val geoQuizRepository=application.container.geoQuizRepository
                GeoQuizViewModel(geoQuizRepository=geoQuizRepository)
            }
        }
    }

    fun resetGame() {
        usedCountries.clear()
        val newCountry = pickRandomCountry()
        _uiState.value = GeoQuizUiState(
            currentCountry = newCountry.name,
            currentCountryIndex = countryList.indexOfFirst { it == newCountry },
            flag = countryList.first { it == newCountry }.flagId,
            isGameOver = false,
        )
    }

    fun updateGameState(updatedScore: Int) {
        if (usedCountries.size == 3) {
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
        flags.add(countryList.filter { it.flagId!=uiState.value.flag }.random().flagId)
        flags.shuffle()
        return flags
    }

    fun getRandomCountriesAndCapitals() {
        countries.clear()
        capitals.clear()
        for (i in 0 until 2) {
            currentCountry = countryList.random()
            countries.add(currentCountry.name)
            capitals.add(currentCountry.capital)
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
            val updatedScore = _uiState.value.score.plus(2)
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
        for (i in 0 until 4) {
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