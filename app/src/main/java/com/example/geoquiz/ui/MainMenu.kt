package com.example.geoquiz.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.example.geoquiz.R

@Composable
fun MainMenu(
    nicknameInput: String,
    onNickNameInputChanged:(String)->Unit,
    modifier: Modifier = Modifier) {

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
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier.fillMaxHeight()
        ) {
            TextField(
                value = nicknameInput,
                onValueChange = onNickNameInputChanged,
                singleLine = true,
                keyboardOptions = KeyboardOptions.Default.copy(
                    keyboardType = KeyboardType.Text,
                    imeAction = ImeAction.Default
                ),
                label = { Text("Nickname") },
                modifier = modifier
            )
            Spacer(modifier = Modifier.padding(20.dp))
            Button(
                onClick = {
                    // Start new game logic
                },
                enabled = nicknameInput.isNotBlank()
            ) {
                Text(text = "Start new game")
            }
            Spacer(modifier = Modifier.padding(20.dp))
            Button(onClick = { /*TODO*/ }) {
                Text(text = "High scores")
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