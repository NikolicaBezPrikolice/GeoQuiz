package com.example.geoquiz

import android.annotation.SuppressLint
import androidx.annotation.StringRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.geoquiz.ui.ConnectGame
import com.example.geoquiz.ui.ErrorScreen
import com.example.geoquiz.ui.FlagGame
import com.example.geoquiz.ui.GeoNetUiState
import com.example.geoquiz.ui.GeoQuizViewModel
import com.example.geoquiz.ui.MainMenu
import com.example.geoquiz.ui.HighScoreViewModel
import com.example.geoquiz.ui.HighScoresList
import com.example.geoquiz.ui.LoadingScreen
import kotlinx.coroutines.launch


enum class GeoQuizScreen(@StringRes val title: Int) {
    Menu(title = R.string.app_name),
    Flags(title = R.string.flag_game),
    Connect(title = R.string.connect_game),
    HighScores(title = R.string.high_scores)
}

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun GeoQuizApp(
    viewModel: GeoQuizViewModel = viewModel(factory = GeoQuizViewModel.Factory),
    highScoreViewModel: HighScoreViewModel = viewModel(factory = HighScoreViewModel.Factory),
    navController: NavHostController = rememberNavController(),
) {
    val backStackEntry by navController.currentBackStackEntryAsState()
    val currentScreen = GeoQuizScreen.valueOf(
        backStackEntry?.destination?.route ?: GeoQuizScreen.Menu.name
    )
    val highScoresUiState by highScoreViewModel.highScoresUiState.collectAsState()
    val coroutineScope = rememberCoroutineScope()
    Scaffold(topBar = { GeoQuizTopAppBar(currentScreen = currentScreen) }) { innerPadding ->
        val uiState by viewModel.uiState.collectAsState()
        NavHost(
            navController = navController,
            startDestination = GeoQuizScreen.Menu.name,
            modifier = Modifier
        ) {
            composable(route = GeoQuizScreen.Menu.name) {
                if (uiState.isGame2Over) {
                    viewModel.initializeMap()
                    viewModel.resetGame()
                    viewModel.getRandomCountriesAndCapitals()
                    viewModel.getRandomFlags()
                }
                when (viewModel.geoNetUiState) {
                    is GeoNetUiState.Loading -> LoadingScreen(modifier = Modifier.fillMaxSize())
                    is GeoNetUiState.Success -> MainMenu(
                        nicknameInput = viewModel.nicknameInput,
                        onNickNameInputChanged = { viewModel.updateNicknameInput(it) },
                        onStartButtonClicked = {
                            navController.navigate(GeoQuizScreen.Flags.name)
                        },
                        onHighScoresButtonClicked = {
                            navController.navigate(GeoQuizScreen.HighScores.name)
                        }
                    )

                    is GeoNetUiState.Error -> ErrorScreen(retryAction = {
                        coroutineScope.launch {
                            try {
                                viewModel.getCountries()
                                viewModel.initializeMap()
                                viewModel.resetGame()
                                viewModel.getRandomCountriesAndCapitals()
                                viewModel.getRandomFlags()
                            } catch (e: Exception) {
                                viewModel.geoNetUiState = GeoNetUiState.Error
                            }
                        }
                    }, modifier = Modifier.fillMaxSize())
                }

            }
            composable(route = GeoQuizScreen.Connect.name) {
                ConnectGame(
                    score = uiState.score,
                    name = viewModel.nicknameInput,
                    onConfirmButtonClicked = {
                        navController.navigate(GeoQuizScreen.Menu.name)

                    },
                    viewModel = viewModel
                )
            }
            composable(route = GeoQuizScreen.Flags.name) {
                FlagGame(
                    name = viewModel.nicknameInput, onConfirmButtonClicked = { score ->
                        // Update the total score when navigating to the next game
                        viewModel.updateScore(score)
                        navController.navigate(GeoQuizScreen.Connect.name)
                    },
                    viewModel = viewModel
                )
            }
            composable(route = GeoQuizScreen.HighScores.name) {
                HighScoresList(highScores = highScoresUiState.highScoreList,
                    onClickButton = { navController.navigate(GeoQuizScreen.Menu.name) }
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GeoQuizTopAppBar(
    currentScreen: GeoQuizScreen,
    modifier: Modifier = Modifier
) {
    CenterAlignedTopAppBar(
        title = {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Image(
                    painter = painterResource(id = R.drawable.logo),
                    contentDescription = null,
                    modifier = Modifier
                        .size(dimensionResource(id = R.dimen.image_size))
                        .padding(dimensionResource(id = R.dimen.padding_small))
                )
                Text(
                    text = stringResource(id = currentScreen.title),
                    style = MaterialTheme.typography.displayLarge
                )
            }
        },
        colors = TopAppBarDefaults.mediumTopAppBarColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer
        ),
        modifier = modifier
    )
}


