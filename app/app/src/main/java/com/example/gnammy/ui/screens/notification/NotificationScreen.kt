package com.example.gnammy.ui.screens.notification

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import com.example.gnammy.R
import com.example.gnammy.ui.GnammyRoute
import com.example.gnammy.ui.composables.NotificationPill
import com.example.gnammy.ui.composables.PillState
import com.example.gnammy.ui.composables.UserGoal
import com.example.gnammy.ui.composables.rememberPillState
import com.example.gnammy.ui.viewmodels.GoalViewModel
import com.example.gnammy.ui.viewmodels.NotificationState
import com.example.gnammy.ui.viewmodels.NotificationViewModel
import kotlinx.coroutines.runBlocking

@Composable
fun NotificationScreen(
    navHostController: NavHostController,
    notificationViewModel: NotificationViewModel,
    goalsViewModel: GoalViewModel
) {
    val notificationState: NotificationState by notificationViewModel.state.collectAsStateWithLifecycle()
    val loadingGoals = remember { mutableStateOf(true) }
    val loadingNotifications = remember { mutableStateOf(true) }

    if (goalsViewModel.goalsPreview.isNotEmpty()) {
        loadingGoals.value = false
    }

    if (notificationState.notifications.isNotEmpty()) {
        loadingNotifications.value = false
    }

    Column(
        modifier = Modifier
            .fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = stringResource(R.string.notification_goals_title),
            style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
            modifier = Modifier.padding(top = 16.dp, bottom = 8.dp)
        )

        if (goalsViewModel.goalsPreview.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(0.2f),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        } else {
            LazyRow(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(0.2f),
                contentPadding = PaddingValues(10.dp),
                horizontalArrangement = Arrangement.spacedBy(10.dp),
            ) {

                items(goalsViewModel.goalsPreview, key = { it.id }) { goal ->
                    UserGoal(
                        goal,
                        Modifier
                            .fillMaxWidth(1 / 2f)
                            .aspectRatio(2f)
                    )
                }
                item {
                    Box(modifier = Modifier.fillMaxHeight()) {
                        TextButton(
                            onClick = { navHostController.navigate("goals") },
                            modifier = Modifier.align(Alignment.Center)
                        ) {
                            Text(
                                stringResource(id = R.string.notification_see_all_goals),
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.primary
                            )
                        }
                    }
                }
            }
        }

        Text(
            text = stringResource(R.string.notification_notification_subtitle),
            style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
            modifier = Modifier.padding(top = 16.dp, bottom = 8.dp)
        )

        if (notificationState.notifications.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(0.8f),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        } else {
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
                        when (pillState.currentState) {
                            PillState.State.Cancelled -> notificationViewModel.setAsSeen(
                                notification.id
                            )

                            PillState.State.Read -> {
                                runBlocking { notificationViewModel.setAsSeen(notification.id) }
                                if (notification.gnamId == null) {
                                    navHostController.navigate(
                                        GnammyRoute.Profile.buildRoute(
                                            notification.sourceId
                                        )
                                    )
                                } else {
                                    navHostController.navigate(
                                        GnammyRoute.GnamDetails.buildRoute(
                                            notification.gnamId
                                        )
                                    )
                                }
                            }

                            else -> {}
                        }
                    }
                }
            }
        }
    }
}
