package com.example.gnammy.ui.composables

import android.net.Uri
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Error
import androidx.compose.material.icons.outlined.Image
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.request.ImageRequest

enum class Size { Sm, Lg }

@Composable
fun ImageWithPlaceholder(uri: Uri?, size: Size, description: String, shape: Shape) {
    if (uri?.path?.isNotEmpty() == true) {
        AsyncImage(
            ImageRequest.Builder(LocalContext.current)
                .data(uri)
                .crossfade(true)
                .build(),
            description,
            contentScale = ContentScale.Fit,
            modifier = Modifier
                .fillMaxSize()
                .clip(shape)
        )
    } else {
        Image(
            Icons.Outlined.Image,
            description,
            contentScale = ContentScale.Fit,
            colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.onSecondary),
            modifier = Modifier
                .clip(shape)
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.secondary)
                .padding(150.dp)
        )
    }
}
