@file:OptIn(ExperimentalMaterialApi::class)
@file:Suppress("DEPRECATION")

package com.example.gnammy.ui.composables

//noinspection UsingMaterialAndMaterial3Libraries
//noinspection UsingMaterialAndMaterial3Libraries
//noinspection UsingMaterialAndMaterial3Libraries
//noinspection UsingMaterialAndMaterial3Libraries
//noinspection UsingMaterialAndMaterial3Libraries
//noinspection UsingMaterialAndMaterial3Libraries
//noinspection UsingMaterialAndMaterial3Libraries
//noinspection UsingMaterialAndMaterial3Libraries
import android.net.Uri
import android.util.Log
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.spring
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.DismissDirection
import androidx.compose.material.DismissState
import androidx.compose.material.DismissValue
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.FractionalThreshold
import androidx.compose.material.Icon
import androidx.compose.material.SwipeToDismiss
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.rememberDismissState
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.gnammy.data.local.entities.Notification

@Composable
fun rememberPillState() = remember { PillState() }

class PillState {
    enum class State {
        Read,
        Cancelled,
        Unread
    }

    var currentState by mutableStateOf(State.Unread)
}

@Composable
fun NotificationPill(notification: Notification, pillState: PillState) {
    var show by remember { mutableStateOf(true) }

    val dismissState = rememberDismissState(
        confirmStateChange = {
            if (it == DismissValue.DismissedToStart || it == DismissValue.DismissedToEnd) {
                pillState.currentState = PillState.State.Cancelled
                true
            } else {
                false
            }
        }
    )
    AnimatedVisibility(
        visible = show,
        exit = fadeOut(spring())
    ) {
        SwipeToDismiss(
            state = dismissState,
            background = {
                DismissBackground(dismissState)
            },
            dismissContent = {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .aspectRatio(5f / 1f)
                        .fillMaxWidth()
                        .clip(shape = CircleShape)
                        .background(color = MaterialTheme.colorScheme.primary)
                        .clickable {
                            Log.i("NotificationPill", "Notification clicked")
                            pillState.currentState = PillState.State.Read
                        }
                        .padding(2.dp)
                ) {
                    ImageWithPlaceholder(
                        uri = Uri.parse(notification.imageUri),
                        size = Size.Sm,
                        description = "propic",
                        modifier = Modifier
                            .padding(5.dp)
                            .aspectRatio(1f)
                            .clip(shape = CircleShape)
                            .border(2.dp, MaterialTheme.colorScheme.background, CircleShape)
                    )

                    Text(
                        text = notification.content,
                        color = MaterialTheme.colorScheme.background,
                        textAlign = TextAlign.Center,
                        modifier = Modifier
                            .weight(1f)
                            .padding(horizontal = 8.dp)
                    )

                    Spacer(modifier = Modifier.width(16.dp))
                }
            },
            dismissThresholds = {
                FractionalThreshold(0.5f)
            }
        )
    }
}


@OptIn(ExperimentalMaterialApi::class)
@Composable
fun DismissBackground(dismissState: DismissState) {
    val direction = dismissState.dismissDirection

    Row(
        modifier = Modifier
            .fillMaxSize()
            .padding(6.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        if (direction == DismissDirection.StartToEnd) {
            Icon(
                Icons.Default.Delete,
                contentDescription = "delete",
                modifier = Modifier
                    .aspectRatio(1f)
                    .clip(CircleShape)
                    .background(MaterialTheme.colorScheme.error)
                    .padding(15.dp)
            )
        }
        Spacer(modifier = Modifier)
        if (direction == DismissDirection.EndToStart) {
            Icon(
                Icons.Default.Delete,
                contentDescription = "delete",
                modifier = Modifier
                    .aspectRatio(1f)
                    .clip(CircleShape)
                    .background(MaterialTheme.colorScheme.error)
                    .padding(15.dp)
            )
        }
    }
}