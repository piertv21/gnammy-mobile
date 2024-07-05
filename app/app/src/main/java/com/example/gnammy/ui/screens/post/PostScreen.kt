package com.example.gnammy.ui.screens.post

import android.annotation.SuppressLint
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
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
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
import com.example.gnammy.ui.viewmodels.GnamViewModel
import com.example.gnammy.ui.viewmodels.UserViewModel
import com.example.gnammy.utils.Result
import kotlinx.coroutines.launch

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun PostScreen(
    navController: NavHostController,
    modifier: Modifier,
    userViewModel: UserViewModel,
    gnamViewModel: GnamViewModel,
    loggedUserId: String
) {
    var title by remember { mutableStateOf("") }
    var shortDescription by remember { mutableStateOf("") }
    var ingredientsAndRecipe by remember { mutableStateOf("") }
    var imageUri by remember { mutableStateOf<Uri?>(null) }
    val postGnamState by gnamViewModel.postGnamState.collectAsState()

    val context = LocalContext.current
    val imagePickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        imageUri = uri
    }

    val scope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(postGnamState) {
        when (val state = postGnamState) {
            is Result.Success -> {
                scope.launch {
                    title = ""
                    shortDescription = ""
                    ingredientsAndRecipe = ""
                    imageUri = null
                    gnamViewModel.resetPostGnamState()
                    snackbarHostState.showSnackbar(state.data)
                }
            }
            is Result.Error -> {
                scope.launch {
                    snackbarHostState.showSnackbar(state.message)
                }
            }
            null -> { /* No action */ }
        }
    }

    Scaffold(
        snackbarHost = {
            SnackbarHost(hostState = snackbarHostState)
        }
    ) {
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
                    onClick = {
                        val error =
                            validateInput(title, shortDescription, ingredientsAndRecipe, imageUri)
                        if (error.isNotEmpty()) {
                            scope.launch {
                                snackbarHostState.showSnackbar(error)
                            }
                        } else {
                            imageUri?.let { uri ->
                                gnamViewModel.publishGnam(
                                    context = context,
                                    currentUserId = loggedUserId,
                                    title = title,
                                    shortDescription = shortDescription,
                                    ingredientsAndRecipe = ingredientsAndRecipe,
                                    imageUri = uri
                                )
                            }
                        }
                    }
                ) {
                    Text(stringResource(R.string.post_publish))
                }
            }
        }
    }
}

private fun validateInput(
    title: String,
    shortDescription: String,
    ingredientsAndRecipe: String,
    imageUri: Uri?
): String {
    return when {
        title.isBlank() || shortDescription.isBlank() || ingredientsAndRecipe.isBlank() -> "Compila tutti i campi"
        imageUri == null -> "Seleziona una foto per lo gnam"
        else -> ""
    }
}
