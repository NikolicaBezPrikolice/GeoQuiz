package com.example.geoquiz.ui

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import kotlinx.coroutines.launch
import com.example.geoquiz.R
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

@SuppressLint("CoroutineCreationDuringComposition")
@Composable
fun ConnectGame(
    score: Int,
    name: String,
    modifier: Modifier = Modifier,
    onConfirmButtonClicked: (Int) -> Unit,
    viewModel: GeoQuizViewModel = viewModel(factory = GeoQuizViewModel.Factory),
    highScoreViewModel: HighScoreViewModel = viewModel(factory = HighScoreViewModel.Factory)
) {

    val connectGameUiState by viewModel.uiState.collectAsState()
    val highScoreUiState by highScoreViewModel.gotScore.collectAsState()
    val coroutineScope = rememberCoroutineScope()
    Column(
        verticalArrangement = Arrangement.SpaceEvenly,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier.fillMaxSize()
    ) {
        Text(
            text = stringResource(id = R.string.connect_capital),
            modifier = Modifier.padding(
                top = 70.dp,
                start = dimensionResource(id = R.dimen.padding_small),
                end = dimensionResource(id = R.dimen.padding_small),
                bottom = dimensionResource(id = R.dimen.padding_medium)
            ),
            style = MaterialTheme.typography.headlineMedium,
            textAlign = TextAlign.Center
        )
        Row(
            horizontalArrangement = Arrangement.Center,
            modifier = Modifier.verticalScroll(rememberScrollState())
        ) {
            Column(
                modifier = Modifier
                    .weight(1f)
                    .align(Alignment.CenterVertically)
            ) {
                for (i in viewModel.countriesList.indices) {
                    Button(
                        onClick = { },
                        colors = ButtonDefaults.buttonColors(
                            when {
                                viewModel.clickedButtonIndex[i] == true -> Color.Green
                                connectGameUiState.countryCount == i -> Color.Yellow

                                viewModel.clickedButtonIndex[i] == false && i > connectGameUiState.countryCount -> MaterialTheme.colorScheme.primary
                                else -> Color.Red
                            }
                        ),
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(dimensionResource(id = R.dimen.padding_small))
                    ) {
                        Text(
                            text = viewModel.countriesList[i], modifier = Modifier.padding(
                                dimensionResource(id = R.dimen.padding_small)
                            )
                        )
                    }
                }
            }
            Column(
                modifier = Modifier
                    .weight(1f)
                    .align(Alignment.CenterVertically)
            ) {
                for (i in viewModel.capitals.indices) {
                    Button(
                        onClick = {
                            if (viewModel.clickedButtonIndex[i + 5] != true) {
                                viewModel.checkCapitalClicked(viewModel.capitals[i], i + 5)
                            }
                        },
                        colors = ButtonDefaults.buttonColors(
                            if (viewModel.clickedButtonIndex[i + 5] == true) Color.Green
                            else MaterialTheme.colorScheme.primary
                        ),
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(dimensionResource(id = R.dimen.padding_small))
                    ) {
                        Text(
                            text = viewModel.capitals[i], modifier = Modifier.padding(
                                dimensionResource(id = R.dimen.padding_small)
                            )
                        )
                    }
                }

            }
        }
    }
    if (connectGameUiState.isGameOver) {
        highScoreViewModel.getScore(name)
        FinalScoreDialog(
            score = score + connectGameUiState.score,
            nickname = name,
            onConfirmButtonClicked = {
                onConfirmButtonClicked.invoke(score + connectGameUiState.score)
                if (highScoreUiState != null) {
                    coroutineScope.launch {
                        withContext(Dispatchers.IO) {
                            highScoreViewModel.updateOneScore(
                                highScoreUiState!!,
                                score + connectGameUiState.score
                            )
                        }
                    }
                } else {
                    coroutineScope.launch {
                        highScoreViewModel.saveHighScore(name, score + connectGameUiState.score)
                    }
                }
            }
        )
    }
}

@Composable
private fun FinalScoreDialog(
    score: Int,
    nickname: String,
    onConfirmButtonClicked: (Int) -> (Unit),
    modifier: Modifier = Modifier
) {
    AlertDialog(
        onDismissRequest = {
        },
        title = { Text(text = stringResource(id = R.string.nickname) + ":  " + nickname) },
        text = { Text(text = stringResource(id = R.string.final_score) + ":  " + score.toString()) },
        modifier = modifier,
        confirmButton = {
            TextButton(onClick = {
                onConfirmButtonClicked(score)
            }) {
                Text(text = stringResource(id = R.string.end_game))
            }
        }
    )
}