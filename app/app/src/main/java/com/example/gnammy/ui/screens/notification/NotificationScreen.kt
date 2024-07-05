package com.example.gnammy.ui.screens.notification

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import com.example.gnammy.ui.composables.Achievement
import com.example.gnammy.ui.composables.NotificationPill
import com.example.gnammy.ui.composables.PillState
import com.example.gnammy.ui.composables.rememberPillState
import com.example.gnammy.ui.viewmodels.NotificationViewModel
import com.example.gnammy.ui.viewmodels.UserViewModel

@Composable
fun NotificationScreen(
    navHostController: NavHostController,
    notificationViewModel: NotificationViewModel,
    userViewModel: UserViewModel,
    modifier: Modifier = Modifier
) {
    val notificationState by notificationViewModel.state.collectAsStateWithLifecycle()

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(top = 16.dp, start = 16.dp, end = 16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "Achievements",
            style = MaterialTheme.typography.headlineLarge,
            modifier = Modifier.padding(bottom = 8.dp)
        )

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
            item {
                Box(modifier = Modifier.fillMaxHeight()) {
                    TextButton(
                        onClick = { navHostController.navigate("goals") },
                        modifier = Modifier.align(Alignment.Center)
                    ) {
                        Text(
                            "See all",
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.primary
                        )
                    }
                }
            }
        }

        Text(
            text = "Notifications",
            style = MaterialTheme.typography.headlineLarge,
            modifier = Modifier.padding(bottom = 8.dp)
        )

        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 4.dp)
                .weight(0.8f),
            verticalArrangement = Arrangement.spacedBy(10.dp),
            contentPadding = PaddingValues(10.dp)
        ) {
            items(notificationState.notifications, key = { it.id }) { notification ->
                val pillState = rememberPillState()
                NotificationPill(notification, pillState)
                LaunchedEffect(key1 = pillState.currentState) {
                    if (pillState.currentState == PillState.State.Read) {
                        notificationViewModel.setAsSeen(notification.id)
                    }
                }
            }
        }
    }
}
