package com.example.gnammy.ui.composables

import android.net.Uri
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.gnammy.data.local.entities.Gnam
import com.example.gnammy.ui.GnammyRoute

@Composable
fun RecipeCardSmall(
    navHostController: NavHostController,
    modifier: Modifier,
    gnam: Gnam
) {
    Box(modifier = modifier
        .aspectRatio(1f)
        .fillMaxSize()
        .clip(RoundedCornerShape(20.dp))
        .clickable {
            navHostController.navigate(GnammyRoute.GnamDetails.buildRoute(gnam.id))
        }
    )
    {
        ImageWithPlaceholder(uri = Uri.parse(gnam.imageUri),
            size = Size.Lg,
            description = "Recipe Image",
            Modifier
                .background(color = MaterialTheme.colorScheme.primaryContainer)
                .clip(shape = RoundedCornerShape(20.dp))
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
                text = gnam.title,
                color = MaterialTheme.colorScheme.background,
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                modifier = Modifier
                    .fillMaxSize()
                    .wrapContentHeight(align = Alignment.CenterVertically)
            )
        }
    }
}
