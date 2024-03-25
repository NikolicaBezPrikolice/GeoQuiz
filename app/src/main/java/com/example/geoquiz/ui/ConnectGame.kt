package com.example.geoquiz.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.viewmodel.compose.viewModel

@Composable
fun ConnectGame(
    score: Int,
    modifier: Modifier = Modifier,
    onConfirmButtonClicked: (Int) -> Unit,
    viewModel: GeoQuizViewModel = viewModel()
) {

    val connectGameUiState by viewModel.uiState.collectAsState()

    Row(modifier = Modifier.fillMaxSize(), horizontalArrangement = Arrangement.Center) {
        Column(
            modifier = Modifier
                .weight(1f)
                .align(Alignment.CenterVertically)
        ) {
            Button(
                onClick = { },
                colors = ButtonDefaults.buttonColors(if (viewModel.clickedButtonIndex[0] == true) Color.Green else if (connectGameUiState.countryCount == 0) Color.Yellow else if (viewModel.clickedButtonIndex[0] == false) Color.Red else MaterialTheme.colorScheme.primary),
                //enabled = false,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(text = viewModel.countries[0])
            }
            Button(
                onClick = { },
                colors = ButtonDefaults.buttonColors(if (viewModel.clickedButtonIndex[1] == true) Color.Green else if (connectGameUiState.countryCount == 1) Color.Yellow else if (viewModel.clickedButtonIndex[1] == false) Color.Red else MaterialTheme.colorScheme.primary),
                // enabled=false,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(text = viewModel.countries[1])
            }
        }
        Column(
            modifier = Modifier
                .weight(1f)
                .align(Alignment.CenterVertically)
        ) {
            Button(
                onClick = {
                    if (viewModel.clickedButtonIndex[2] != true) viewModel.checkCapitalClicked(
                        viewModel.capitals[0],
                        2
                    )
                },
                colors = ButtonDefaults.buttonColors(if (viewModel.clickedButtonIndex[2] == true) Color.Green else MaterialTheme.colorScheme.primary),
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(text = viewModel.capitals[0])
            }
            Button(
                onClick = {
                    if (viewModel.clickedButtonIndex[3] != true) viewModel.checkCapitalClicked(
                        viewModel.capitals[1],
                        3
                    )
                },
                colors = ButtonDefaults.buttonColors(if (viewModel.clickedButtonIndex[3] == true) Color.Green else MaterialTheme.colorScheme.primary),
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(text = viewModel.capitals[1])
            }
        }
    }
    if (connectGameUiState.isGameOver) {
        FinalScoreDialog(
            score = score+connectGameUiState.score,
            onConfirmButtonClicked = onConfirmButtonClicked
        )
    }
}

@Composable
private fun FinalScoreDialog(
    score: Int,
    onConfirmButtonClicked: (Int)->(Unit),
    modifier: Modifier = Modifier
) {

    AlertDialog(
        onDismissRequest = {
            // Dismiss the dialog when the user clicks outside the dialog or on the back
            // button. If you want to disable that functionality, simply use an empty
            // onCloseRequest.
        },
        title = { Text(text = "Connect game score:") },
        text = { Text(text = score.toString()) },
        modifier = modifier,
        confirmButton = {
            TextButton(onClick = {onConfirmButtonClicked(score)}) {
                Text(text = "next game")
            }
        }
    )
}