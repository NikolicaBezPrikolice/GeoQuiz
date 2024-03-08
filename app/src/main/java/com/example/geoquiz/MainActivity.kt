package com.example.geoquiz

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.geoquiz.ui.theme.GeoQuizTheme


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            GeoQuizTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    FrontPage(name = "Geo Quiz")
                }
            }
        }
    }
}

@Composable
fun FrontPage(name: String, modifier: Modifier = Modifier) {
    var nickNameInput by remember { mutableStateOf("") }
    Box(
        modifier = modifier.fillMaxSize(),
        contentAlignment = Alignment.TopCenter
    ) {
        // Background image
        Image(
            painter = painterResource(id = R.drawable.adc62a35c875858dcce4b600ffee2cea),
            contentDescription = null,
            alpha = 0.5F,
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize()
        )
        // Column to hold the text
        Column(
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.fillMaxHeight()
        ) {
            Text(
                text = name,
                fontSize = 50.sp,
                modifier = Modifier.padding(20.dp)
            )
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center,
                modifier = Modifier.fillMaxHeight()
            ) {
                TextField(
                    value = nickNameInput,
                    onValueChange = { nickNameInput=it },
                    singleLine = true,
                    keyboardOptions = KeyboardOptions.Default.copy(
                        keyboardType = KeyboardType.Text,
                        imeAction = ImeAction.Default),
                    label = { Text("Nickname") },
                    modifier = modifier
                )
                Spacer(modifier = Modifier.padding(20.dp))
                Button(
                    onClick = {
                        // Start new game logic
                    },
                    enabled= nickNameInput.isNotBlank()
                ) {
                    Text(text = "Start new game")
                }
                Spacer(modifier = Modifier.padding(20.dp))
                Button(onClick = { /*TODO*/ }) {
                    Text(text = "High scores")
                }
            }
        }
        Column(
            verticalArrangement = Arrangement.Bottom,
            horizontalAlignment = Alignment.End,
            modifier = Modifier
                .fillMaxSize()
        ) {
            Text(
                text = "© 2024 Nikola Tabaš",
                modifier = Modifier.padding(
                    bottom = 16.dp, end = 16.dp
                )
            )

        }
    }
}

@Composable
fun Game(modifier: Modifier = Modifier) {
    var flag1 by remember { mutableStateOf((1..4).random()) }
    var flag2 by remember {
        mutableStateOf((1..4).random().takeIf { it != flag1 } ?: (1..4).random())
    }

    val image1 = when (flag1) {
        1 -> R.drawable.flag_of_serbia_svg
        2 -> R.drawable.flag_of_armenia
        3 -> R.drawable.flag_of_ethiopia
        else -> R.drawable.mexico
    }
    val image2 = when (flag2) {
        1 -> R.drawable.flag_of_serbia_svg
        2 -> R.drawable.flag_of_armenia
        3 -> R.drawable.flag_of_ethiopia
        else -> R.drawable.mexico
    }
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = modifier.fillMaxSize(),
    ) {
        Image(painterResource(id = image1),
            contentDescription = null,
            modifier = Modifier
                .size(250.dp)
                .clickable {
                    flag1 = (1..4).random()
                    var flag2Candidate: Int
                    do {
                        flag2Candidate = (1..4).random()
                    } while (flag2Candidate == flag1)
                    flag2 = flag2Candidate
                })
        Spacer(modifier = Modifier.height(16.dp))
        Image(painter = painterResource(id = image2),
            contentDescription = null,
            modifier = Modifier
                .size(250.dp)
                .clickable {
                    flag1 = (1..4).random()
                    var flag2Candidate: Int
                    do {
                        flag2Candidate = (1..4).random()
                    } while (flag2Candidate == flag1)
                    flag2 = flag2Candidate
                }
        )
    }
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    GeoQuizTheme {
        FrontPage("Geo QUiz")
    }
}