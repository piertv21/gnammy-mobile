package com.example.gnammy.ui.composables

import android.net.Uri
import android.os.Build
import android.os.VibrationEffect
import android.os.Vibrator
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.tween
import androidx.compose.animation.core.updateTransition
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.rounded.Close
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.alexstyl.swipeablecard.SwipeableCardState
import com.example.gnammy.data.local.entities.Gnam
import com.example.gnammy.utils.DateFormats
import com.example.gnammy.utils.millisToDateString

@Composable
fun RecipeCardBig(
    key: String,
    gnam: Gnam,
    modifier: Modifier = Modifier,
    navController: NavHostController,
    state: SwipeableCardState
) {
    val transition =
        updateTransition(targetState = state.offset.value.x, label = "cardOffsetTransition")
    val context = LocalContext.current
    val vibrator = remember { context.getSystemService(Vibrator::class.java) }

    var hasVibrated by remember { mutableStateOf(false) }

    val screenWidth = with(LocalDensity.current) {
        LocalConfiguration.current.screenWidthDp.dp.toPx()
    }
    val threshold = screenWidth / 4

    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
        if (kotlin.math.abs(state.offset.value.x) > threshold && !hasVibrated) {
            vibrator.vibrate(VibrationEffect.createPredefined(VibrationEffect.EFFECT_TICK))
            hasVibrated = true
        } else if (kotlin.math.abs(state.offset.value.x) <= threshold) {
            hasVibrated = false
        }
    }

    val alpha by transition.animateFloat(
        transitionSpec = { tween(durationMillis = 500, easing = FastOutSlowInEasing) },
        label = "alphaAnimation"
    ) { offsetX ->
        kotlin.math.min(threshold / 400f, kotlin.math.abs(offsetX) / 400f)
    }

    val iconAlpha by transition.animateFloat(
        transitionSpec = { tween(durationMillis = 500, easing = FastOutSlowInEasing) },
        label = "iconAlphaAnimation"
    ) { offsetX ->
        if (kotlin.math.abs(offsetX) > 25f) {
            kotlin.math.min(threshold / 200f, kotlin.math.abs(offsetX) / 200f)
        } else {
            0f
        }
    }

    Box(
        modifier = modifier
            .padding(10.dp)
            .fillMaxSize()
            .clip(RoundedCornerShape(20.dp))
    ) {
        Column(
            modifier = Modifier
                .clip(RoundedCornerShape(20.dp))
                .background(color = MaterialTheme.colorScheme.primaryContainer)
                .padding(10.dp)
                .fillMaxSize()
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(20.dp))
                    .background(MaterialTheme.colorScheme.primary)
                    .padding(10.dp)
            ) {
                Text(
                    text = gnam.title,
                    color = MaterialTheme.colorScheme.background,
                    textAlign = TextAlign.Start,
                    style = MaterialTheme.typography.displaySmall.copy(fontWeight = FontWeight.Bold),
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(10.dp))
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { navController.navigate("profile/${gnam.authorId}") }
                ) {
                    ImageWithPlaceholder(
                        uri = Uri.parse(gnam.authorImageUri),
                        description = "Author Image",
                        modifier = Modifier
                            .border(2.dp, MaterialTheme.colorScheme.background, CircleShape)
                            .size(30.dp)
                            .clip(CircleShape)
                            .background(MaterialTheme.colorScheme.primaryContainer)
                    )
                    Spacer(modifier = Modifier.width(10.dp))
                    Text(
                        text = gnam.authorName,
                        color = MaterialTheme.colorScheme.background,
                        textAlign = TextAlign.Start,
                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                        modifier = Modifier.align(Alignment.CenterVertically)
                    )
                    Text(
                        text = " - " + millisToDateString(gnam.date, DateFormats.SHOW_FORMAT),
                        color = MaterialTheme.colorScheme.background,
                        textAlign = TextAlign.Start,
                        style = MaterialTheme.typography.titleMedium.copy(),
                        modifier = Modifier.align(Alignment.CenterVertically)
                    )
                }
            }
            Spacer(modifier = Modifier.height(10.dp))
            Box {
                ImageWithPlaceholder(
                    uri = Uri.parse(gnam.imageUri),
                    description = "Recipe Image",
                    modifier = Modifier
                        .background(MaterialTheme.colorScheme.primaryContainer)
                        .clip(RoundedCornerShape(20.dp))
                        .fillMaxSize()
                        .padding(bottom = 100.dp)
                )
                Box(
                    modifier = Modifier
                        .fillMaxHeight(1f / 3f)
                        .fillMaxWidth()
                        .align(Alignment.BottomCenter)
                        .clip(RoundedCornerShape(20.dp))
                        .background(MaterialTheme.colorScheme.primary)
                        .padding(10.dp)
                ) {
                    Text(
                        text = gnam.description,
                        color = MaterialTheme.colorScheme.background,
                        textAlign = TextAlign.Start,
                        style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Bold),
                        modifier = Modifier
                            .fillMaxSize()
                            .wrapContentHeight(Alignment.CenterVertically),
                        overflow = TextOverflow.Ellipsis
                    )
                }
            }
        }
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.primary.copy(alpha = alpha))
        )
        Box(
            modifier = Modifier
                .align(Alignment.Center)
                .size(80.dp)
                .graphicsLayer(alpha = iconAlpha)
                .background(MaterialTheme.colorScheme.primaryContainer, CircleShape)
        ) {
            Icon(
                imageVector = if (state.offset.value.x > 0) Icons.Default.Favorite else Icons.Rounded.Close,
                contentDescription = if (state.offset.value.x > 0) "Like" else "Dislike",
                tint = MaterialTheme.colorScheme.primary,
                modifier = Modifier
                    .align(Alignment.Center)
                    .size(35.dp)
            )
        }
    }
}
