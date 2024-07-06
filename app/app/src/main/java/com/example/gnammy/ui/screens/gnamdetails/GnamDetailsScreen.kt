package com.example.gnammy.ui.screens.gnamdetails

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.gnammy.data.local.entities.Gnam
import com.example.gnammy.ui.composables.ImageWithPlaceholder
import com.example.gnammy.ui.composables.Size
import com.example.gnammy.utils.DateFormats
import com.example.gnammy.utils.millisToDateString

@Composable
fun GnamDetailsScreen(
    navController: NavHostController,
    gnam: Gnam
) {
    val context = LocalContext.current
    val scrollState = rememberScrollState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp, 16.dp, 16.dp)
            .verticalScroll(scrollState)
    ) {
        Column (
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
                    uri = Uri.parse(gnam.authorImageUri),
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

        ImageWithPlaceholder(
            uri = Uri.parse(gnam.imageUri),
            size = Size.Lg,
            description = "Recipe Image",
            modifier = Modifier
                .fillMaxSize()
                .aspectRatio(1f)
                .clip(shape = RoundedCornerShape(20.dp))
        )

        Spacer(modifier = Modifier.height(10.dp))

        Box(
            modifier = Modifier
                .fillMaxHeight(1f / 3f)
                .fillMaxWidth()
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

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "Recipe:",
            style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
            modifier = Modifier.align(Alignment.Start)
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = gnam.recipe,
            style = MaterialTheme.typography.bodyLarge,
            modifier = Modifier.align(Alignment.Start)
        )

        Spacer(modifier = Modifier.height(16.dp))

        Row(
            horizontalArrangement = Arrangement.Center,
            modifier = Modifier.fillMaxWidth()
        ) {
            Button(
                modifier = Modifier.padding(end = 8.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color.Red),
                onClick = {
                    // TODO Handle unlike action
                }
            ) {
                Text("Rimuovi dai preferiti")
            }
            Button(
                onClick = {
                    val intent = Intent().apply {
                        action = Intent.ACTION_SEND
                        putExtra(Intent.EXTRA_TEXT, gnam.recipe)
                        type = "text/plain"
                    }
                    context.startActivity(Intent.createChooser(intent, "Save Recipe to Notes"))
                }
            ) {
                Text("Salva nelle note")
            }
        }
    }
}