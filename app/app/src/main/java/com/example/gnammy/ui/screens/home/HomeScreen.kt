package com.example.gnammy.ui.screens.home

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.rounded.Close
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import com.alexstyl.swipeablecard.Direction
import com.alexstyl.swipeablecard.ExperimentalSwipeableCardApi
import com.alexstyl.swipeablecard.rememberSwipeableCardState
import com.alexstyl.swipeablecard.swipableCard
import com.example.gnammy.ui.composables.RecipeCardBig
import com.example.gnammy.ui.viewmodels.GnamViewModel
import com.example.gnammy.ui.viewmodels.NotificationViewModel
import com.example.gnammy.utils.isOnline
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@OptIn(ExperimentalSwipeableCardApi::class)
@Composable
fun HomeScreen(
    navController: NavHostController,
    gnamViewModel: GnamViewModel,
    notificationViewModel: NotificationViewModel,
    loggedUserId: String,
    modifier: Modifier = Modifier
) {
    val gnamsState by gnamViewModel.timelineState.collectAsStateWithLifecycle()
    val scope = rememberCoroutineScope()
    val context = LocalContext.current
    val offline = remember { mutableStateOf(true) }
    val fetchMore = remember { mutableStateOf(false) }
    val loading = remember { mutableStateOf(false) }

    val coroutineScope = rememberCoroutineScope()

    DisposableEffect(Unit) {
        val job = coroutineScope.launch {
            while (true) {
                Log.i("HomeScreen", "fetching notifications")
                notificationViewModel.fetchNotifications(loggedUserId)
                delay(2 * 60 * 1000)
            }
        }

        onDispose {
            job.cancel()
        }
    }

    LaunchedEffect(Unit) {
        if (isOnline(context)) {
            offline.value = false
            fetchMore.value = true
        } else {
            offline.value = true
        }
    }
    LaunchedEffect(isOnline(context)) {
        if (isOnline(context)) {
            offline.value = false
        } else {
            offline.value = true
        }
    }
    LaunchedEffect(gnamsState.gnams.size) {
        if (gnamsState.gnams.size < 5) {
            fetchMore.value = true
            if (gnamsState.gnams.isEmpty()) {
                loading.value = true
            }
        } else {
            loading.value = false
            fetchMore.value = false
        }
    }
    if (offline.value) {
        Box(modifier = Modifier.fillMaxSize()) {
            Text("No internet connection", modifier = Modifier.align(Alignment.Center))
        }
    } else {
        if (fetchMore.value) {
            gnamViewModel.fetchGnamTimeline()
            fetchMore.value = false
        }
        if (loading.value) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
        } else {
            val states = gnamsState.gnams.reversed()
                .map { it to rememberSwipeableCardState() }

            Column(
                modifier = Modifier.fillMaxSize(),
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp, 8.dp, 8.dp)
                        .weight(0.85f)
                ) {
                    states.forEach { (gnam, state) ->
                        if (state.swipedDirection == null) {
                            RecipeCardBig(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .swipableCard(
                                        state = state,
                                        blockedDirections = listOf(Direction.Down, Direction.Up),
                                        onSwiped = {
                                        },
                                        onSwipeCancel = {
                                            Log.d("Swipeable-Card", "Cancelled swipe")
                                        }
                                    ),
                                gnam = gnam
                            )
                        }
                        LaunchedEffect(state.swipedDirection) {
                            Log.i("HomeScreen", "swipato ${state.swipedDirection}")
                            if (state.swipedDirection != null) {
                                gnamViewModel.removeFromTimeline(
                                    gnam,
                                    state.swipedDirection != Direction.Left
                                )
                            }
                        }
                    }
                }

                Row(
                    modifier = Modifier
                        .weight(0.15f)
                        .fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    IconButton(
                        onClick = {
                            scope.launch {
                                val last = states.reversed()
                                    .firstOrNull {
                                        it.second.offset.value == Offset(0f, 0f)
                                    }?.second
                                last?.swipe(Direction.Left)
                            }
                        },
                        modifier = Modifier
                            .size(80.dp)
                            .background(MaterialTheme.colorScheme.primaryContainer, CircleShape)
                    ) {
                        Icon(
                            imageVector = Icons.Rounded.Close,
                            contentDescription = "Dislike",
                            tint = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.size(35.dp)
                        )
                    }
                    IconButton(
                        onClick = {
                            scope.launch {
                                val last = states.reversed()
                                    .firstOrNull {
                                        it.second.offset.value == Offset(0f, 0f)
                                    }?.second

                                last?.swipe(Direction.Right)
                            }
                        },
                        modifier = Modifier
                            .size(80.dp)
                            .background(MaterialTheme.colorScheme.primaryContainer, CircleShape)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Favorite,
                            contentDescription = "Like",
                            tint = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.size(35.dp)
                        )
                    }
                }
            }
        }
    }
}
