package com.example.gnammy.ui.composables

import android.net.Uri
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
import androidx.navigation.NavHostController
import com.example.gnammy.data.local.entities.Gnam
import com.example.gnammy.utils.DateFormats
import com.example.gnammy.utils.millisToDateString

@Composable
fun RecipeCardBig(
    key: String,
    gnam: Gnam,
    modifier: Modifier,
    navController: NavHostController
) {
    Column(
        modifier = modifier
            .clip(RoundedCornerShape(20.dp))
            .background(color = MaterialTheme.colorScheme.primaryContainer)
            .padding(10.dp)
            .fillMaxSize()
            .clip(RoundedCornerShape(20.dp))
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .clip(shape = RoundedCornerShape(20.dp))
                .background(color = MaterialTheme.colorScheme.primary)
                .padding(10.dp)
        ) {
            Text(
                text = gnam.title,
                color = MaterialTheme.colorScheme.background,
                textAlign = TextAlign.Start,
                style = MaterialTheme.typography.displaySmall.copy(fontWeight = FontWeight.Bold),
                modifier = Modifier
                    .fillMaxWidth(),
            )
            Spacer(modifier = Modifier.height(10.dp))
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .align(Alignment.CenterHorizontally)
                    .clickable {
                        navController.navigate("profile/${gnam.authorId}")
                    }
            ) {
                ImageWithPlaceholder(
                    uri = Uri.parse(gnam.authorImageUri),
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
                    text = gnam.authorName,
                    color = MaterialTheme.colorScheme.background,
                    textAlign = TextAlign.Start,
                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                    modifier = Modifier
                        .align(Alignment.CenterVertically)
                        .wrapContentHeight(align = Alignment.CenterVertically)
                )
                Text(
                    text = " - " + millisToDateString(gnam.date, DateFormats.SHOW_FORMAT),
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
            ImageWithPlaceholder(
                uri = Uri.parse(gnam.imageUri),
                description = "Recipe Image",
                Modifier
                    .background(color = MaterialTheme.colorScheme.primaryContainer)
                    .clip(shape = RoundedCornerShape(20.dp))
                    .fillMaxSize()
                    .padding(0.dp, 0.dp, 0.dp, 100.dp)
            )
            Box(
                modifier = Modifier
                    .fillMaxHeight(1f / 3f)
                    .fillMaxWidth()
                    .align(Alignment.BottomCenter)
                    .clip(shape = RoundedCornerShape(20.dp))
                    .background(color = MaterialTheme.colorScheme.primary)
                    .padding(10.dp)
            ) {
                Text(
                    text = gnam.description,
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
