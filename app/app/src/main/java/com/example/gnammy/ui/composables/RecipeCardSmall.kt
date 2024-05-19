package com.example.gnammy.ui.composables

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

/*
 It shows a small recipe card with a placeholder image and a description.
 */
@Composable
fun RecipeCardSmall(modifier: Modifier) {

    Box(modifier = modifier
        .aspectRatio(1f)
        .fillMaxSize()
        .clip(RoundedCornerShape(20.dp))
    )
    {
        ImageWithPlaceholder(uri = null,
            size = Size.Lg,
            description = "Recipe Image",
            Modifier
                .background(color = MaterialTheme.colorScheme.onSecondary)
                .clip(shape = RoundedCornerShape(20.dp))
                .padding(50.dp, 50.dp, 50.dp, 90.dp)
                .fillMaxSize())
        Box(
            modifier = Modifier
                .fillMaxHeight(1f / 3f)
                .fillMaxWidth()
                .align(Alignment.BottomCenter)
                .clip(shape = RoundedCornerShape(20.dp))
                .background(color = MaterialTheme.colorScheme.primary)
        )
        {
            Text(
                text = "Spicy Sriracha Noodles",
                color = Color.Black,
                textAlign = TextAlign.Center,
                style = TextStyle(
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold
                ),
                modifier = Modifier
                    .fillMaxSize()
                    .wrapContentHeight(align = Alignment.CenterVertically)
            )
        }
    }
}
