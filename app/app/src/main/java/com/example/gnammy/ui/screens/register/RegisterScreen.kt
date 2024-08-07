package com.example.gnammy.ui.screens.register

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.gnammy.R
import com.example.gnammy.ui.viewmodels.UserViewModel
import com.example.gnammy.utils.Result
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

@Composable
fun RegisterScreen(navHostController: NavHostController, userViewModel: UserViewModel) {
    var username by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }
    var profilePictureUri by remember { mutableStateOf<Uri?>(null) }

    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }
    val keyboardController = LocalSoftwareKeyboardController.current

    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        profilePictureUri = uri
    }

    Scaffold(
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) }
    ) { contentPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(60.dp)
                .padding(contentPadding),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text("Registrati", fontWeight = FontWeight.Bold, fontSize = 30.sp)

            Spacer(modifier = Modifier.height(32.dp))

            OutlinedTextField(
                value = username,
                onValueChange = { username = it },
                label = { Text("Username") },
                modifier = Modifier.fillMaxWidth(),
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Text,
                    imeAction = ImeAction.Next
                ),
                keyboardActions = KeyboardActions(onNext = { /* Focus next input field */ })
            )

            Spacer(modifier = Modifier.height(16.dp))

            OutlinedTextField(
                value = password,
                onValueChange = { password = it },
                label = { Text("Password") },
                modifier = Modifier.fillMaxWidth(),
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Password,
                    imeAction = ImeAction.Next
                ),
                keyboardActions = KeyboardActions(onNext = { /* Focus next input field */ })
            )

            Spacer(modifier = Modifier.height(16.dp))

            OutlinedTextField(
                value = confirmPassword,
                onValueChange = { confirmPassword = it },
                label = { Text("Conferma Password") },
                modifier = Modifier.fillMaxWidth(),
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Password,
                    imeAction = ImeAction.Done
                ),
                keyboardActions = KeyboardActions(onDone = { /* Perform registration action */ })
            )

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = { launcher.launch("image/*") },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 8.dp)
            ) {
                Text(
                    text = if (profilePictureUri == null) {
                        stringResource(R.string.register_propic)
                    } else {
                        "Selezionato: " + profilePictureUri?.lastPathSegment
                    }
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = {
                    keyboardController?.hide()
                    val error =
                        validateInput(username, password, confirmPassword, profilePictureUri)
                    if (error.isEmpty()) {
                        val propic = profilePictureUri!!
                        val state = runBlocking {
                            userViewModel.register(
                                context,
                                username.trim(),
                                password.trim(),
                                propic
                            )
                        }

                        when (state) {
                            is Result.Success -> {
                                scope.launch {
                                    snackbarHostState.showSnackbar(state.data)
                                    navHostController.navigate("Home") {
                                        popUpTo("Register") { inclusive = true }
                                    }
                                }
                            }

                            is Result.Error -> {
                                scope.launch {
                                    snackbarHostState.showSnackbar(state.message)
                                }
                            }

                            null -> { /* No action */
                            }
                        }
                    } else {
                        scope.launch {
                            snackbarHostState.showSnackbar(error)
                        }
                    }
                }
            ) {
                Text("Registrati")
            }

            Spacer(modifier = Modifier.height(16.dp))

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(1.dp)
                    .background(Color.Gray)
            )

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                "Hai già un account?",
                color = Color.Gray
            )

            Button(
                modifier = Modifier.padding(top = 8.dp),
                onClick = {
                    navHostController.navigate("login")
                }
            ) {
                Text("Accedi")
            }
        }
    }
}

private fun validateInput(
    username: String,
    password: String,
    confirmPassword: String,
    profilePictureUri: Uri?
): String {
    return when {
        username.isBlank() || password.isBlank() -> "Compila tutti i campi"
        profilePictureUri == null -> "Seleziona una foto profilo"
        password != confirmPassword -> "Le password non corrispondono"
        username.length > 15 -> "Username troppo lungo"
        else -> ""
    }
}
