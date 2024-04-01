package com.example.geoquiz.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import com.example.geoquiz.R
import androidx.lifecycle.viewmodel.compose.viewModel

@Composable
fun FlagGame(
    onConfirmButtonClicked: (Int) -> Unit,
    modifier: Modifier = Modifier,
    viewModel: GeoQuizViewModel = viewModel(factory = GeoQuizViewModel.Factory),
) {
    val flagGameUiState by viewModel.uiState.collectAsState()

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = modifier.fillMaxSize(),
    ) {
        Text(
            text = "Which one is the flag of",
            modifier = Modifier.padding(dimensionResource(id = R.dimen.padding_small)),
            style = MaterialTheme.typography.headlineMedium
        )
        Text(
            text = flagGameUiState.currentCountry,
            modifier = Modifier.padding(dimensionResource(id = R.dimen.padding_small)),
            style = MaterialTheme.typography.displayLarge.copy(
                textDecoration = TextDecoration.Underline
            )
        )
        Image(
            painterResource(id = viewModel.flagIds[0]),
            contentDescription = null,
            modifier = Modifier
                .size(250.dp)
                .clip(MaterialTheme.shapes.small)
                .clickable {
                    viewModel.checkImageClicked(viewModel.flagIds[0])
                })
        Spacer(modifier = Modifier.height(16.dp))
        Image(painter = painterResource(id = viewModel.flagIds[1]),
            contentDescription = null,
            modifier = Modifier
                .size(250.dp)
                .clickable {
                    viewModel.checkImageClicked(viewModel.flagIds[1])
                }
        )
    }
    if (flagGameUiState.isGameOver) {
        FinalScoreDialog(
            score = flagGameUiState.score,
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
        title = { Text(text = "congratulations") },
        text = { Text(text = score.toString()) },
        modifier = modifier,
        confirmButton = {
            TextButton(onClick = {onConfirmButtonClicked(score)}) {
                Text(text = "end game")
            }
        }
    )
}