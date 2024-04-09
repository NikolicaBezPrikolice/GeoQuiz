package com.example.geoquiz.ui

import android.annotation.SuppressLint
import android.content.res.Configuration
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.AlertDialog
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
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import com.example.geoquiz.R
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import coil.request.ImageRequest

import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun FlagGame(
    name: String,
    onConfirmButtonClicked: (Int) -> Unit,
    modifier: Modifier = Modifier,
    viewModel: GeoQuizViewModel = viewModel(factory = GeoQuizViewModel.Factory),
) {
    val flagGameUiState by viewModel.uiState.collectAsState()

    val isLandscape = LocalConfiguration.current.orientation == Configuration.ORIENTATION_LANDSCAPE

    if (isLandscape) {
        LandscapeFlagLayout(
            modifier = modifier,
            viewModel = viewModel
        )
    } else {
        PortraitFlagLayout(
            modifier = modifier,
            viewModel = viewModel
        )
    }
    if (flagGameUiState.isGameOver) {
        FinalScoreDialog(
            nickname = name,
            score = flagGameUiState.score,
            onConfirmButtonClicked = onConfirmButtonClicked
        )
    }
}


@SuppressLint("CoroutineCreationDuringComposition")
@Composable
fun PortraitFlagLayout(
    modifier: Modifier = Modifier,
    viewModel: GeoQuizViewModel,
) {
    val flagGameUiState by viewModel.uiState.collectAsState()

    val coroutineScope = rememberCoroutineScope()

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = modifier.fillMaxSize(),
    ) {
        Text(
            text = stringResource(id = R.string.guess_flag),
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
        for (flagId in viewModel.flags) {
            val borderColor =
                if (viewModel.clickedFlagId.value == flagId && flagId == viewModel.countries.value[viewModel.getCountryIndex()].flag.png) {
                    Color.Green
                } else if (viewModel.clickedFlagId.value == flagId && flagId != viewModel.countries.value[viewModel.getCountryIndex()].flag.png) {
                    Color.Red
                } else {
                    Color.Transparent
                }
            AsyncImage(
                model = ImageRequest.Builder(context = LocalContext.current).data(flagId)
                    .crossfade(true).build(),
                placeholder = painterResource(R.drawable.loading_img),
                error = painterResource(R.drawable.ic_broken_image),
                contentDescription = null,
                modifier = Modifier.size(180.dp)
                    .clickable {
                        coroutineScope.launch {
                            viewModel.updateClickedFlagId(flagId)
                            delay(500)
                            viewModel.updateClickedFlagId(null)
                            viewModel.checkImageClicked(flagId)
                        }

                    }
                    .border(2.dp, borderColor)
            )

        }
    }


}

@Composable
fun LandscapeFlagLayout(
    modifier: Modifier = Modifier,
    viewModel: GeoQuizViewModel,
) {
    val flagGameUiState by viewModel.uiState.collectAsState()

    val coroutineScope = rememberCoroutineScope()
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = modifier.fillMaxSize(),
    ) {
        Row(
            horizontalArrangement = Arrangement.Center,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                text = stringResource(id = R.string.guess_flag),
                modifier = Modifier.padding(dimensionResource(id = R.dimen.padding_medium)),
                style = MaterialTheme.typography.headlineMedium
            )

            Text(
                text = flagGameUiState.currentCountry,
                modifier = Modifier.padding(dimensionResource(id = R.dimen.padding_medium)),
                style = MaterialTheme.typography.displayLarge.copy(
                    textDecoration = TextDecoration.Underline
                )
            )
        }
        Row(
            horizontalArrangement = Arrangement.SpaceEvenly,
            modifier = Modifier.fillMaxWidth()
        ) {
            for (flagId in viewModel.flags) {
                val borderColor =
                    if (viewModel.clickedFlagId.value == flagId && flagId == viewModel.countries.value[viewModel.getCountryIndex()].flag.png) {
                        Color.Green
                    } else if (viewModel.clickedFlagId.value == flagId && flagId != viewModel.countries.value[viewModel.getCountryIndex()].flag.png) {
                        Color.Red
                    } else {
                        Color.Transparent
                    }
                AsyncImage(
                    model = ImageRequest.Builder(context = LocalContext.current).data(flagId)
                        .crossfade(true).build(),
                    error = painterResource(R.drawable.ic_broken_image),
                    placeholder = painterResource(R.drawable.loading_img),
                    contentDescription = null,
                    modifier = Modifier.size(180.dp)
                        .clickable {
                            coroutineScope.launch {
                                viewModel.updateClickedFlagId(flagId)
                                delay(500)
                                viewModel.updateClickedFlagId(null)
                                viewModel.checkImageClicked(flagId)
                            }

                        }
                        .border(2.dp, borderColor)
                )

            }
        }
    }

}


@Composable
private fun FinalScoreDialog(
    nickname: String,
    score: Int,
    onConfirmButtonClicked: (Int) -> (Unit),
    modifier: Modifier = Modifier
) {

    AlertDialog(
        onDismissRequest = {
        },
        title = { Text(text = stringResource(id = R.string.nickname)+":  "+ nickname) },
        text = { Text(text = stringResource(id = R.string.score)+":  "+score.toString()) },
        modifier = modifier,
        confirmButton = {
            TextButton(onClick = { onConfirmButtonClicked(score) }) {
                Text(text = stringResource(id = R.string.next_game))
            }
        }
    )
}