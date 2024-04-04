package com.example.geoquiz.ui

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.geoquiz.data.HighScore
import com.example.geoquiz.R
import kotlinx.coroutines.launch

@Composable
fun HighScoresList(highScores: List<HighScore>,
                   onClickButton: ()->(Unit),
                   highScoreViewModel: HighScoreViewModel= viewModel(factory = HighScoreViewModel.Factory),
                   ) {
    var deleteConfirmationRequired by rememberSaveable { mutableStateOf(false) }
    val coroutineScope= rememberCoroutineScope()
    Column(modifier = Modifier.padding(top = 80.dp)) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Button(
                onClick = { deleteConfirmationRequired = true
                },
                modifier = Modifier
                    .padding(end = 16.dp, bottom = 16.dp)
                ) {
                Text(stringResource(id = R.string.delete_scores))
            }
            Button(
                onClick = { onClickButton() },
                modifier = Modifier
                    .padding(end = 16.dp, bottom = 16.dp)
            ) {
                Text(stringResource(id = R.string.back_to_main_menu))
            }
        }
        LazyColumn(modifier = Modifier.padding(top=10.dp)) {
            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceAround
                ) {
                    Text(
                        text = stringResource(id = R.string.position)
                    )
                    Divider(
                        modifier = Modifier
                            .height(32.dp)
                            .fillMaxHeight()
                            .width(1.dp),
                        color = Color.White
                    )
                    Text(
                        text = stringResource(id = R.string.nickname),
                        modifier = Modifier.width(70.dp)
                    )
                    Divider(
                        modifier = Modifier
                            .height(32.dp)
                            .fillMaxHeight()
                            .width(1.dp),
                        color = Color.White
                    )
                    Text(
                        text = stringResource(id = R.string.score),
                        modifier = Modifier.padding(end = 16.dp)
                    )
                }
                Divider(
                    color = Color.White,
                    thickness = 1.dp,
                    modifier = Modifier.fillMaxWidth()
                )
            }
            items(highScores.size) { index ->
                val score=highScores[index]
                HighScoreItem(position = index+1, score=score)
                Divider(
                    color = Color.White,
                    thickness = 1.dp,
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }
        if(deleteConfirmationRequired){
            DeleteDialog(
                onConfirmButtonClicked = {deleteConfirmationRequired = false
                    coroutineScope.launch {
                        highScoreViewModel.deleteAllItems()
                    } },
                onDeleteButtonClicked = { deleteConfirmationRequired = false
                   })
        }
    }
}

@Composable
fun HighScoreItem(position: Int, score: HighScore) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .border(width = 1.dp, MaterialTheme.colorScheme.onSurface)
            .padding(20.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceAround
    ) {
        Text(
            text = "$position.",
        )
        Text(text = score.name,
            Modifier.width(70.dp)
            )
        Text(text = score.score.toString())

    }
}

@Composable
private fun DeleteDialog(
    onConfirmButtonClicked: () -> (Unit),
    onDeleteButtonClicked:()->(Unit),
    modifier: Modifier = Modifier
) {

    AlertDialog(
        onDismissRequest = {
        },
        title = { Text(text = stringResource(id = R.string.confirmation)) },
        modifier = modifier,
        dismissButton = {
            TextButton(onClick = { onDeleteButtonClicked() }) {
                Text(text = stringResource(id = R.string.no))
            }
        },
        confirmButton = {
            TextButton(onClick = { onConfirmButtonClicked() }) {
                Text(text = stringResource(id = R.string.yes))
            }
        }
    )
}
