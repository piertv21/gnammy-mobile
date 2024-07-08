package com.example.gnammy.ui.composables

import android.net.Uri
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.gnammy.R
import com.example.gnammy.data.local.entities.GnamGoal
import com.example.gnammy.data.local.entities.UserGoal

@Composable
fun UserGoal(goal: UserGoal, modifier: Modifier = Modifier) {
    Row(
        modifier = modifier
            .clip(RoundedCornerShape(20.dp))
            .background(color = MaterialTheme.colorScheme.primary)
    ) {
        Image(
            painter = painterResource(id = R.drawable.medal),
            contentDescription = "medal",
            modifier = Modifier
                .align(Alignment.CenterVertically)
                .padding(start = 10.dp, end = 0.dp, top = 5.dp, bottom = 5.dp)
                .weight(0.25f)
        )

        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = 10.dp, bottom = 10.dp, start = 0.dp, end = 5.dp)
                .weight(0.75f)
        ) {
            Text(
                text = goal.content,
                color = MaterialTheme.colorScheme.background,
                textAlign = TextAlign.Center,
                modifier = Modifier.align(Alignment.Center)
            )
        }
    }
}

@Composable
fun GnamGoal(goal: GnamGoal) {
    Column(
        modifier = Modifier
            .fillMaxWidth(1 / 2f)
            .aspectRatio(1f)
            .padding(4.dp)
            .clip(RoundedCornerShape(20.dp))
            .background(color = MaterialTheme.colorScheme.primary)
    ) {
        Box(
            modifier = Modifier
                .weight(0.65f)
        ) {
            ImageWithPlaceholder(
                uri = Uri.parse(goal.imageUri),
                size = Size.Sm,
                description = "propic",
                modifier =
                Modifier
                    .fillMaxHeight()
            )
        }

        Row(
            modifier = Modifier
                .weight(0.35f)
                .clip(RoundedCornerShape(20.dp))
        ) {
            Image(
                painter = painterResource(id = R.drawable.medal),
                contentDescription = "medal",
                modifier = Modifier
                    .align(Alignment.CenterVertically)
                    .padding(start = 10.dp, end = 0.dp, top = 0.dp, bottom = 5.dp)
                    .weight(0.2f)
            )

            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(top = 0.dp, bottom = 0.dp, start = 0.dp, end = 5.dp)
                    .weight(0.80f)
            ) {
                Text(
                    text = goal.content,
                    color = MaterialTheme.colorScheme.background,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.align(Alignment.Center)
                )
            }
        }
    }
}
