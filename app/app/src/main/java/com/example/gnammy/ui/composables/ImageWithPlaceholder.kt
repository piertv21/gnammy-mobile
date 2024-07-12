package com.example.gnammy.ui.composables

import android.net.Uri
import androidx.compose.foundation.Image
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Image
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import coil.compose.AsyncImage
import coil.request.ImageRequest

@Composable
fun ImageWithPlaceholder(uri: Uri?, description: String, modifier: Modifier) {
    if (uri?.path?.isNotEmpty() == true) {
        AsyncImage(
            ImageRequest.Builder(LocalContext.current)
                .data(uri)
                .crossfade(true)
                .build(),
            description,
            contentScale = ContentScale.Crop,
            modifier = modifier
        )
    } else {
        Image(
            Icons.Outlined.Image,
            description,
            contentScale = ContentScale.Fit,
            colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.background),
            modifier = modifier
        )
    }
}
