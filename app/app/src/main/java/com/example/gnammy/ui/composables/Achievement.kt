package com.example.gnammy.ui.composables

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Composable
@Preview
fun Achievement() {
    Row (
        modifier = Modifier
            .aspectRatio(2f / 1f)
            .fillMaxSize()
            .clip(RoundedCornerShape(20.dp))
            .background(color = MaterialTheme.colorScheme.primary)
            .padding(5.dp)
    ){
        ImageWithPlaceholder(
            uri = null,
            size = Size.Sm,
            description = "propic",
            modifier =
            Modifier
                .fillMaxWidth(1f/3f)
                .fillMaxHeight()
                .padding(5.dp)
                .aspectRatio(1f)
                .clip(shape = CircleShape))

        Box(
            modifier = Modifier.fillMaxSize().padding(20.dp)
        ) {
            Text(
                text = "Le tue ricette sono state salvate più di 10 volte!",
                color = MaterialTheme.colorScheme.background,
                textAlign = TextAlign.Center,
                modifier = Modifier.align(Alignment.Center)
            )
        }
    }
}

@Composable
@Preview
fun GnamSpecificAchievement() {
    Row (
        modifier = Modifier
            .aspectRatio(3f/1f)
            .padding(5.dp)
            .fillMaxWidth()
            .clip(RoundedCornerShape(20.dp))
            .background(color = MaterialTheme.colorScheme.primary)
    ){
        ImageWithPlaceholder(
            uri = null,
            size = Size.Sm,
            description = "propic",
            modifier =
            Modifier
                .fillMaxWidth(1f/3f)
                .fillMaxHeight()
                .aspectRatio(1f)
                .clip(RoundedCornerShape(20.dp))
        )

        Box(
            modifier = Modifier.fillMaxSize()
        ) {
            Text(
                text = "Panino con la mortazza è stato salvato 400 volte!",
                color = MaterialTheme.colorScheme.background,
                textAlign = TextAlign.Center,
                modifier = Modifier.align(Alignment.Center).padding(5.dp)
            )
        }
    }
}
