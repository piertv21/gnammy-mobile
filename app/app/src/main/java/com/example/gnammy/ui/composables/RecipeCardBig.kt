package com.example.gnammy.ui.composables

import android.net.Uri
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp

/*
 It shows a small recipe card with a placeholder image and a description.
 */
@Composable
fun RecipeCardBig(modifier: Modifier) {
    val propicUri = Uri.parse("https://budgetbytes.com/wp-content/uploads/2022/07/Beth-2022-3-60x60.jpg")
    val imageUri = Uri.parse("https://budgetbytes.com/wp-content/uploads/2012/08/7-Green-onion-and-Cilantro-768x576.jpg")

    Column(modifier = modifier
        .clip(RoundedCornerShape(20.dp))
        .background(color = MaterialTheme.colorScheme.primaryContainer)
        .padding(10.dp)
        .fillMaxSize()
        .clip(RoundedCornerShape(20.dp))
    )
    {
        // titleBar
        Column (
            modifier = Modifier
                .fillMaxWidth()
                .clip(shape = RoundedCornerShape(20.dp))
                .background(color = MaterialTheme.colorScheme.primary)
                .padding(10.dp)
        )
        {
            Text(
                text = "Spicy Sriracha Noodles",
                color = MaterialTheme.colorScheme.background,
                textAlign = TextAlign.Start,
                style =  MaterialTheme.typography.displaySmall.copy(fontWeight = FontWeight.Bold),
                modifier = Modifier
                    .fillMaxWidth(),
            )
            Spacer(modifier = Modifier.height(10.dp))
            Row (
                modifier = Modifier
                    .fillMaxWidth()
                    .align(Alignment.CenterHorizontally)
            ) {
                ImageWithPlaceholder(
                    uri = propicUri,
                    size = Size.Sm,
                    description = "propic",
                    modifier = Modifier
                        .border(2.dp, MaterialTheme.colorScheme.background, CircleShape)
                        .size(30.dp)
                        .clip(shape = CircleShape)
                        .align(Alignment.CenterVertically)
                        .background(color = MaterialTheme.colorScheme.primaryContainer)
                )
                Spacer(modifier = Modifier.width(10.dp))
                Text(
                    text = " Beth, Budget Bytes",
                    color = MaterialTheme.colorScheme.background,
                    textAlign = TextAlign.Start,
                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                    modifier = Modifier
                        .align(Alignment.CenterVertically)
                        .wrapContentHeight(align = Alignment.CenterVertically)
                )
                Text(
                    text = " - 24/05/2024",
                    color = MaterialTheme.colorScheme.background,
                    textAlign = TextAlign.Start,
                    style = MaterialTheme.typography.titleMedium.copy(),
                    modifier = Modifier
                        .align(Alignment.CenterVertically)
                        .wrapContentHeight(align = Alignment.CenterVertically)
                )
            }

        }

        Spacer(modifier = Modifier.height(10.dp))

        Box {
            // image

            ImageWithPlaceholder(
                uri = imageUri,
                size = Size.Lg,
                description = "Recipe Image",
                Modifier
                    .background(color = MaterialTheme.colorScheme.primaryContainer)
                    .clip(shape = RoundedCornerShape(20.dp))
                    .fillMaxSize()
                    .padding(0.dp , 0.dp, 0.dp, 100.dp)
            )

            // descriptionBar
            Box(
                modifier = Modifier
                    .fillMaxHeight(1f / 3f)
                    .fillMaxWidth()
                    .align(Alignment.BottomCenter)
                    .clip(shape = RoundedCornerShape(20.dp))
                    .background(color = MaterialTheme.colorScheme.primary)
                    .padding(10.dp)
            )
            {
                Text(
                    text = "Oh WOW. It should be illegal for noodles to be this easy AND this delicious. These spicy sriracha noodles are my new favorite quick fix! They only take about 15 minutes to make, they’re totally rich, flavorful, and SUPER SPICY. Like, “burn a hole through your stomach” spicy. Call me crazy, but sometimes I want that. No, I crave that. So, this one goes out to all of you heat seekers!",
                    color = MaterialTheme.colorScheme.background,
                    textAlign = TextAlign.Start,
                    style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Bold),
                    modifier = Modifier
                        .fillMaxSize()
                        .wrapContentHeight(align = Alignment.CenterVertically),
                    overflow = TextOverflow.Ellipsis
                )
            }
        }
    }
}
