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
import androidx.compose.material3.Button
import androidx.compose.material3.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.geoquiz.data.HighScore

@Composable
fun HighScoresList(highScores: List<HighScore>,
                   onClickButton: ()->(Unit)) {
    Column(modifier = Modifier.padding(top = 80.dp)) {
        Button(
            onClick = { onClickButton() },
            modifier = Modifier
                .align(Alignment.End)
                .padding(end = 16.dp, bottom = 16.dp)
        ) {
            Text("Back to Main Menu")
        }
        LazyColumn(modifier = Modifier.padding(top=10.dp)) {
            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceAround
                ) {
                    Text(
                        text = "Position"
                    )
                    Divider(
                        modifier = Modifier
                            .height(32.dp)
                            .fillMaxHeight()
                            .width(1.dp),
                        color = Color.White
                    )
                    Text(
                        text = "Name",
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
                        text = "Score",
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
