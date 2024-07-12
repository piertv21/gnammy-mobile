package com.example.gnammy.ui.screens.gnamdetails

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
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
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import com.example.gnammy.R
import com.example.gnammy.data.local.entities.Gnam
import com.example.gnammy.ui.composables.ImageWithPlaceholder
import com.example.gnammy.ui.viewmodels.GnamViewModel
import com.example.gnammy.utils.DateFormats
import com.example.gnammy.utils.isOnline
import com.example.gnammy.utils.millisToDateString
import kotlinx.coroutines.runBlocking

@SuppressLint("StateFlowValueCalledInComposition")
@Composable
fun GnamDetailsScreen(
    navController: NavHostController,
    gnamId: String,
    gnamViewModel: GnamViewModel,
    loggedUserId: String
) {
    val ctx = LocalContext.current
    val offline = remember { mutableStateOf(true) }
    val loading = remember { mutableStateOf(false) }
    val fetchedGnam by gnamViewModel.gnamToBeFetched.collectAsStateWithLifecycle(null)

    LaunchedEffect(Unit) {
        if (isOnline(ctx)) {
            offline.value = false
            loading.value = true
            gnamViewModel.getGnamData(gnamId)
            loading.value = false
        } else {
            offline.value = true
        }
    }

    if (loading.value && fetchedGnam == null) { // Loading
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            CircularProgressIndicator()
        }
    } else if (offline.value) { // Offline
        val localgnam = gnamViewModel.likedGnamsState.value.gnams.find { it.id == gnamId }
        if (localgnam != null) {
            gnamDetailsView(
                navController,
                localgnam,
                gnamViewModel,
                loggedUserId,
                ctx,
                !offline.value
            )
        } else {
            Box(modifier = Modifier.fillMaxSize()) {
                Text("Connettiti ad internet per visualizzare lo gnam.", modifier = Modifier.align(Alignment.Center))
            }
        }
    } else if(!offline.value) { // Online
        gnamDetailsView(
            navController,
            fetchedGnam!!,
            gnamViewModel,
            loggedUserId,
            ctx,
            !offline.value
        )
    }
}

@SuppressLint("StateFlowValueCalledInComposition")
@Composable
fun gnamDetailsView(
    navController: NavHostController,
    gnam: Gnam,
    gnamViewModel: GnamViewModel,
    loggedUserId: String,
    context: Context,
    online: Boolean
) {
    val scrollState = rememberScrollState()
    val isGnamSaved by gnamViewModel.isCurrentGnamSaved.collectAsState()
    if(gnam.authorId != loggedUserId && online) { // If online and is not the author
        gnamViewModel.isCurrentGnamSaved(gnam.id)
    }

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

        ImageWithPlaceholder(
            uri = Uri.parse(gnam.imageUri),
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
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp)
        ) {
            if(gnam.authorId != loggedUserId && online) { // If online and is not the author
                Button(
                    modifier = Modifier.padding(end = 8.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = if(!isGnamSaved) MaterialTheme.colorScheme.tertiary else MaterialTheme.colorScheme.error,
                    ),
                    onClick = {
                        if (!isGnamSaved) {
                            gnamViewModel.likeGnam(gnam)
                        } else {
                            gnamViewModel.removeGnamFromSaved(gnam, loggedUserId)
                        }
                        navController.navigate("saved")
                    }
                ) {
                    Text(
                        stringResource(
                            if(!isGnamSaved)
                                R.string.saved_add_to_saved
                            else
                                R.string.saved_remove_from_saved
                        )
                    )
                }
            }
            Button(
                onClick = {
                    runBlocking { gnamViewModel.shareGnam(gnam) }
                    val intent = Intent().apply {
                        action = Intent.ACTION_SEND
                        putExtra(Intent.EXTRA_TEXT, "Ehi! Prova questa ricetta che ho trovato su Gnammy:\n" + gnam.recipe)
                        type = "text/plain"
                    }
                    context.startActivity(Intent.createChooser(intent, "Save Recipe to Notes"))
                }
            ) {
                Text("Condividi")
            }
        }
    }
}