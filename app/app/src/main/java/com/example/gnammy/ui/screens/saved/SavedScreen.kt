package com.example.gnammy.ui.screens.saved

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.gnammy.ui.composables.RecipeCardSmall
import com.example.gnammy.ui.viewmodels.GnamViewModel
import com.example.gnammy.utils.isOnline

@Composable
fun SavedScreen(
    navController: NavHostController,
    gnamViewModel: GnamViewModel,
    currentUserId: String
) {
    val context = LocalContext.current
    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(isOnline(context)) {
        if (isOnline(context)) {
            gnamViewModel.syncSavedGnam(currentUserId)
        } else {
            snackbarHostState.showSnackbar("Impossibile sincronizzare i preferiti, connessione assente")
        }
    }

    val likedGnams by gnamViewModel.likedGnamsState.collectAsState()

    Box(modifier = Modifier.fillMaxSize()) {

        if (likedGnams.gnams.isNotEmpty()) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(top = 24.dp, start = 24.dp, end = 24.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                LazyVerticalGrid(
                    columns = GridCells.Fixed(2)
                ) {
                    items(likedGnams.gnams) { likedGnam ->
                        RecipeCardSmall(
                            gnam = likedGnam,
                            navController,
                            Modifier.padding(5.dp)
                        )
                    }
                }
            }
        } else {
            Box(modifier = Modifier.fillMaxSize()) {
                Text("Nessuno gnam salvato.", modifier = Modifier.align(Alignment.Center))
            }
        }

        SnackbarHost(
            hostState = snackbarHostState,
            modifier = Modifier.align(Alignment.BottomCenter)
        )
    }
}