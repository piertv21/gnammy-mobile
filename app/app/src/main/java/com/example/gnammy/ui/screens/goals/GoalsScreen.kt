package com.example.gnammy.ui.screens.goals

import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.gnammy.ui.composables.Achievement
import com.example.gnammy.ui.composables.GnamSpecificAchievement

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun GoalsScreen() {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(start = 16.dp, end = 16.dp, top = 16.dp)
    ) {
        item {
            Text(
                text = "Utente",
                style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 16.dp)
            )
        }

        item {
            FlowRow(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
            ) {
                for (index in 0 until 10) {
                    Achievement(modifier = Modifier
                        .fillMaxWidth(0.5f)
                        .padding(end = 6.dp, bottom = 6.dp)
                    )
                }
            }
        }

        item {
            Text(
                text = "Ricette",
                style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 16.dp, bottom = 8.dp)
            )
        }

        items(10) {
            GnamSpecificAchievement(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 18.dp, end = 18.dp, bottom = 6.dp)
            )
        }
    }
}