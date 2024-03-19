package com.example.geoquiz

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.geoquiz.ui.GeoQuizViewModel
import com.example.geoquiz.ui.MainMenu
import com.example.geoquiz.ui.theme.GeoQuizTheme
import androidx.lifecycle.viewmodel.compose.viewModel


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
                    GeoQuizApp()
                    //Game()
                }
            }
        }
    }
}

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun GeoQuizApp(viewModel: GeoQuizViewModel = viewModel()) {
    val geqQuizUiState by viewModel.uiState.collectAsState()
    Scaffold(topBar = { GeoQuizTopAppBar() }) {
        MainMenu(nicknameInput = viewModel.nicknameInput , onNickNameInputChanged = {viewModel.updateNicknameInput(it)} )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GeoQuizTopAppBar(modifier: Modifier = Modifier) {
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
                    text = "GeoQuiz",
                    style = MaterialTheme.typography.displayLarge
                )
            }
        },
        modifier = modifier
    )
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
                .clip(MaterialTheme.shapes.small)
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

    }
}