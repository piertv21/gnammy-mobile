package com.example.gnammy.ui.screens.goals

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.gnammy.ui.composables.Achievement

@Composable
fun GoalsScreen() {
    Column (
        modifier = Modifier.fillMaxWidth()
            .padding(16.dp)
    ) {
        Text(
            text = "Utente",
            style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
            modifier = Modifier.fillMaxWidth()
                .padding(start = 16.dp)
        )
        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            contentPadding = PaddingValues(16.dp)
        ) {
            items(10) { index ->
                Achievement()
            }
        }
        Text(
            text = "Ricette",
            style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
            modifier = Modifier.fillMaxWidth()
                .padding(start = 16.dp)
        )
        LazyColumn(
            contentPadding = PaddingValues(16.dp)
        ) {
            items(10) { index ->
                Achievement()
            }
        }
    }
}