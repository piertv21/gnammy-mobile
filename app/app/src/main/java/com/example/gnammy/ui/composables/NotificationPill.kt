package com.example.gnammy.ui.composables

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
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
fun NotificationPill() {
    Row (
        modifier = Modifier
            .aspectRatio(5f / 1f)
            .fillMaxWidth()
            .clip(shape = androidx.compose.foundation.shape.CircleShape)
            .background(color = MaterialTheme.colorScheme.inversePrimary)
    ){
        ImageWithPlaceholder(
            uri = null,
            size = Size.Sm,
            description = "propic",
            modifier =
            Modifier
                .padding(5.dp)
                .aspectRatio(1f)
                .clip(shape = androidx.compose.foundation.shape.CircleShape)
                .border(2.dp, MaterialTheme.colorScheme.background, CircleShape)
        )

        Spacer(modifier = Modifier.width(10.dp))

        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier.fillMaxSize()
        ) {
            Text(
                text = "Paolo ha salvato il tuo gnam!",
                color = MaterialTheme.colorScheme.background,
                textAlign = TextAlign.Center
            )
        }

        Spacer(modifier = Modifier.width(10.dp))


    }
}