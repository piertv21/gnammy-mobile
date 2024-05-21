package com.example.gnammy.ui.screens.profile

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.navigation.NavHostController
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
        Row(verticalAlignment = Alignment.CenterVertically) {
            ImageWithPlaceholder(
                uri = propicUri,
                size = Size.Sm,
                description = "propic",
                modifier = Modifier
                    .border(2.dp, MaterialTheme.colorScheme.inversePrimary, CircleShape)
                    .size(120.dp)
                    .clip(CircleShape)
            )

            Spacer(modifier = Modifier.width(16.dp))

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 20.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(text = "Zeb89", fontWeight = FontWeight.Bold, fontSize = 20.sp)
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Filled.LocationOn,
                        contentDescription = "Location",
                        modifier = Modifier.size(16.dp)
                    )
                    Text(text = "Cesena, Italia", fontSize = 16.sp)
                }

                Spacer(modifier = Modifier.height(8.dp))

                Row(
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(text = "Follower", fontWeight = FontWeight.Bold)
                        Text(text = "1000")
                    }
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(text = "Seguiti", fontWeight = FontWeight.Bold)
                        Text(text = "500")
                    }
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(text = "Gnam", fontWeight = FontWeight.Bold)
                        Text(text = "150")
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier.fillMaxWidth()
        ) {
            Button(
                modifier = Modifier
                    .weight(0.4f)
                    .padding(end = 8.dp),
                onClick = { /* Follow action */ }
            ) {
                Text(text = "Segui")
            }
            Button(
                modifier = Modifier
                    .weight(0.4f)
                    .padding(start = 8.dp, end = 8.dp),
                onClick = { /* Share action */ }
            ) {
                Text(text = "Condividi")
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

        Spacer(modifier = Modifier.height(16.dp))

        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            modifier = Modifier
                .fillMaxWidth()
                .height(600.dp)
        ) {
            items(10) {
                RecipeCardSmall(modifier = Modifier.padding(5.dp))
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
                        fontSize = 20.sp,
                    )
                    Column(
                        modifier = Modifier.padding(top = 16.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(text = if (isDarkTheme) "Night Mode" else "Day Mode")
                        Switch(
                            checked = isDarkTheme,
                            onCheckedChange = { isDarkTheme = it }
                        )
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    OutlinedTextField(
                        value = username,
                        onValueChange = { username = it },
                        label = { Text("Username") },
                        modifier = Modifier.fillMaxWidth()
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    Button(
                        onClick = { launcher.launch("image/*") },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(text = "Select Profile Picture")
                    }

                    profilePictureUri?.let {
                        Text(
                            text = "Selected Image: $it",
                            modifier = Modifier.padding(top = 8.dp)
                        )
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceEvenly
                    ) {
                        Button(
                            onClick = { onDismissRequest() },
                            modifier = Modifier
                                .weight(1f)
                                .padding(end = 8.dp)
                        ) {
                            Text(text = "Annulla")
                        }

                        Button(
                            onClick = { /* Save action */ },
                            modifier = Modifier
                                .weight(1f)
                                .padding(start = 8.dp)
                        ) {
                            Text(text = "Salva")
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    Button(
                        onClick = { /* Logout action */ },
                        colors = ButtonDefaults.buttonColors(containerColor = Color.Red),
                        modifier = Modifier
                            .fillMaxWidth(0.5f)
                            .padding(vertical = 8.dp)
                    ) {
                        Text(text = "Logout", color = Color.White)
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