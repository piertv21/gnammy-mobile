package com.example.gnammy.ui.screens.post

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest

@Composable
fun PostScreen(navController: NavHostController, modifier: Modifier) {
    var imageUri by remember { mutableStateOf<Uri?>(null) }
    val context = LocalContext.current
    val imagePickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        imageUri = uri
    }

    var title by remember { mutableStateOf("") }
    var shortDescription by remember { mutableStateOf("") }
    var ingredientsAndRecipe by remember { mutableStateOf("") }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(top = 20.dp, start = 20.dp, end = 20.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        item {
            Button(onClick = { imagePickerLauncher.launch("image/*") }) {
                Text("Seleziona un'immagine")
            }
        }

        item {
            imageUri?.let {
                Image(
                    painter = rememberAsyncImagePainter(
                        ImageRequest.Builder(context)
                            .data(it)
                            .build()
                    ),
                    contentDescription = null,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(200.dp)
                        .padding(vertical = 16.dp)
                )
            }
        }

        item {
            OutlinedTextField(
                value = title,
                onValueChange = { title = it },
                label = { Text("Titolo") },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp)
            )
        }

        item {
            OutlinedTextField(
                value = shortDescription,
                onValueChange = { shortDescription = it },
                label = { Text("Descrizione breve") },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(150.dp)
                    .padding(vertical = 8.dp)
            )
        }

        item {
            OutlinedTextField(
                value = ingredientsAndRecipe,
                onValueChange = { ingredientsAndRecipe = it },
                label = { Text("Ingredienti e Ricetta") },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp)
                    .padding(vertical = 8.dp)
            )
        }

        item {
            Button(
                modifier = Modifier.padding(top = 10.dp, bottom = 20.dp),
                onClick = { /* TO DO */ }
            ) {
                Text("Pubblica")
            }
        }
    }
}
