package com.example.gnammy.ui.screens.post

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import com.example.gnammy.R

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
            .padding(top = 16.dp, start = 16.dp, end = 16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        item {
            Button(onClick = { imagePickerLauncher.launch("image/*") }) {
                Text(stringResource(R.string.post_select_image))
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
                label = { Text(stringResource(R.string.post_title)) },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp),
                shape = RoundedCornerShape(30.dp)
            )
        }

        item {
            OutlinedTextField(
                value = shortDescription,
                onValueChange = { shortDescription = it },
                label = { Text(stringResource(R.string.post_brief_desc)) },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(150.dp)
                    .padding(vertical = 4.dp),
                shape = RoundedCornerShape(30.dp)
            )
        }

        item {
            OutlinedTextField(
                value = ingredientsAndRecipe,
                onValueChange = { ingredientsAndRecipe = it },
                label = { Text(stringResource(R.string.post_ingr_and_recipe)) },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp)
                    .padding(vertical = 4.dp),
                shape = RoundedCornerShape(30.dp)
            )
        }

        item {
            Button(
                modifier = Modifier.padding(top = 4.dp, bottom = 20.dp),
                onClick = { /* TO DO */ }
            ) {
                Text(stringResource(R.string.post_publish))
            }
        }
    }
}
