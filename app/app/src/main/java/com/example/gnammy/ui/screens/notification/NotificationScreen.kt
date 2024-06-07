package com.example.gnammy.ui.screens.notification

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.gnammy.ui.composables.Achievement
import com.example.gnammy.ui.composables.NotificationPill
import com.example.gnammy.ui.composables.PillState
import com.example.gnammy.ui.composables.rememberPillState

@Composable
fun NotificationScreen(navController: NavHostController, modifier: Modifier) {
    var notifications = remember { mutableStateOf(listOf("0", "1", "2", "3", "4", "5")) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(top = 16.dp, start = 16.dp, end = 16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        LazyRow(
            modifier = Modifier
                .fillMaxWidth()
                .weight(0.2f),
            contentPadding = PaddingValues(10.dp),
            horizontalArrangement = Arrangement.spacedBy(10.dp),
        ) {
            items(5) {
                Achievement()
            }
        }

        LazyColumn (
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 4.dp)
                .weight(0.8f),
            verticalArrangement = Arrangement.spacedBy(10.dp),
            contentPadding = PaddingValues(10.dp)
        ) {
            itemsIndexed(notifications.value, key = { _, notification -> notification }) { index, notification ->
                val pillState = rememberPillState()
                NotificationPill(notification, null, pillState)
                LaunchedEffect(key1 = pillState.currentState) {
                    if (pillState.currentState == PillState.State.Read) {
                        notifications.value = notifications.value.filterIndexed { i, _ -> i != index }
                    }
                }
            }
        }
    }
}
