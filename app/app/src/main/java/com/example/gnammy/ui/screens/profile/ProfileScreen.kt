package com.example.gnammy.ui.screens.profile

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.navigation.NavHostController
import com.example.gnammy.R
import com.example.gnammy.ui.composables.ImageWithPlaceholder
import com.example.gnammy.ui.composables.RecipeCardSmall
import com.example.gnammy.ui.composables.Size

@Composable
fun ProfileScreen(navController: NavHostController, modifier: Modifier = Modifier) {
    val propicUri = Uri.parse("https://yt3.googleusercontent.com/ytc/AIdro_mwQSsxeQr7i0F9h6HzA7BuZGsYapkvfIIwAi2grQ_XUwc=s160-c-k-c0x00ffffff-no-rj")
    var showDialog by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .padding(top = 16.dp, start = 16.dp, end = 16.dp)
            .fillMaxSize()
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(bottom = 16.dp)
        ) {
            ImageWithPlaceholder(
                uri = propicUri,
                size = Size.Sm,
                description = "propic",
                modifier = Modifier
                    .border(2.dp, MaterialTheme.colorScheme.inversePrimary, CircleShape)
                    .size(120.dp)
                    .clip(CircleShape)
                    .padding(bottom = 16.dp)
            )

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "Zeb89",
                    style = MaterialTheme.typography.headlineMedium .copy()
                )

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.padding(bottom = 8.dp)
                ) {
                    Icon(
                        imageVector = Icons.Filled.LocationOn,
                        contentDescription = "Location",
                        modifier = Modifier.size(16.dp)
                    )
                    Text(
                        text = "Cesena, Italia",
                        style = MaterialTheme.typography.titleSmall.copy()
                    )
                }

                Row(
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(
                            text = stringResource(R.string.profile_followers),
                            style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold)
                        )
                        Text(
                            text = "1000",
                            style = MaterialTheme.typography.titleSmall.copy()
                        )
                    }
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(
                            text = stringResource(R.string.profile_followed),
                            style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold)
                        )
                        Text(
                            text = "1000",
                            style = MaterialTheme.typography.titleSmall.copy()
                        )
                    }
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(
                            text = stringResource(R.string.profile_gnams),
                            style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold)
                        )
                        Text(
                            text = "1000",
                            style = MaterialTheme.typography.titleSmall.copy()
                        )
                    }
                }
            }
        }

        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp)
        ) {
            Button(
                modifier = Modifier
                    .weight(0.4f)
                    .padding(end = 8.dp),
                onClick = { /* Follow action */ }
            ) {
                Text(text = stringResource(R.string.profile_follow))
            }
            Button(
                modifier = Modifier
                    .weight(0.4f)
                    .padding(start = 8.dp, end = 8.dp),
                onClick = { /* Share action */ }
            ) {
                Text(text = stringResource(R.string.profile_share))
            }
            Button(
                modifier = Modifier
                    .weight(0.2f)
                    .padding(start = 8.dp),
                onClick = { showDialog = true }
            ) {
                Icon(imageVector = Icons.Filled.Settings, contentDescription = "Settings")
            }

            if (showDialog) {
                SettingsModal(onDismissRequest = { showDialog = false })
            }
        }

        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            modifier = Modifier
                .fillMaxWidth()
                .height(600.dp)
        ) {
            items(10) {
                RecipeCardSmall(navController, Modifier.padding(5.dp))
            }
        }
    }
}

@Composable
fun SettingsModal(onDismissRequest: () -> Unit) {
    var username by remember { mutableStateOf(TextFieldValue("Zeb89")) }
    var isDarkTheme by remember { mutableStateOf(false) }
    var profilePictureUri by remember { mutableStateOf<Uri?>(null) }

    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        profilePictureUri = uri
    }

    Dialog(onDismissRequest = { onDismissRequest() }) {
        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            shape = MaterialTheme.shapes.medium
        ) {
            Box {
                Column(
                    modifier = Modifier
                        .padding(16.dp)
                        .verticalScroll(rememberScrollState()),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "Impostazioni",
                        style = MaterialTheme.typography.headlineSmall.copy()
                    )
                    Column(
                        modifier = Modifier.padding(top = 16.dp, bottom = 8.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(text = if (isDarkTheme) stringResource(R.string.profile_night_mode) else stringResource(R.string.profile_light_mode))
                        Switch(
                            checked = isDarkTheme,
                            onCheckedChange = { isDarkTheme = it }
                        )
                    }

                    Button(
                        onClick = { /* TODO */ },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 8.dp)
                    ) {
                        Text(text = stringResource(R.string.profile_update_gps))
                    }

                    OutlinedTextField(
                        value = username,
                        onValueChange = { username = it },
                        label = { Text("Username") },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 16.dp),
                        shape = RoundedCornerShape(30.dp)
                    )

                    Button(
                        onClick = { launcher.launch("image/*") },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 8.dp)
                    ) {
                        Text(text = stringResource(R.string.profile_select_propic))
                    }

                    profilePictureUri?.let {
                        Text(
                            text = "Selected Image: $it",
                            modifier = Modifier.padding(top = 8.dp, bottom = 16.dp)
                        )
                    }

                    Spacer(modifier = Modifier.height(4.dp))

                    Button(
                        onClick = { /* Save action */ },
                        // TODO: scegliere un verde specifico da mettere nel tema
                        colors = ButtonDefaults.buttonColors(containerColor = Color.Green.copy(alpha = 0.8f)),
                        modifier = Modifier
                            .fillMaxWidth(0.5f)
                            ) {
                        Text(text = stringResource(R.string.profile_save))
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    Button(
                        onClick = { /* Logout action */ },
                        colors = ButtonDefaults.buttonColors(containerColor = Color.Red),
                        modifier = Modifier
                            .fillMaxWidth(0.5f)
                    ) {
                        Text(text = stringResource(R.string.profile_logout), color = Color.White)
                    }
                }

                IconButton(
                    onClick = { onDismissRequest() },
                    modifier = Modifier.align(Alignment.TopEnd)
                ) {
                    Icon(
                        imageVector = Icons.Filled.Close,
                        contentDescription = "Close"
                    )
                }
            }
        }
    }
}