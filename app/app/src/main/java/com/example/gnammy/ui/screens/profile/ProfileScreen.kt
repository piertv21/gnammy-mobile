package com.example.gnammy.ui.screens.profile

import android.content.Intent
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.navigation.NavHostController
import com.example.gnammy.R
import com.example.gnammy.data.local.entities.User
import com.example.gnammy.ui.composables.ImageWithPlaceholder
import com.example.gnammy.ui.composables.Size
import com.example.gnammy.ui.theme.Themes
import com.example.gnammy.ui.viewmodels.ThemeViewModel
import com.example.gnammy.ui.viewmodels.UserViewModel

@Composable
fun ProfileScreen(
    user: User,
    navController: NavHostController,
    modifier: Modifier = Modifier,
    userViewModel: UserViewModel,
    loggedUserId: String,
    themeViewModel: ThemeViewModel
) {
    var showDialog by remember { mutableStateOf(false) }

    val ctx = LocalContext.current

    fun shareProfile() {
        val sendIntent = Intent().apply {
            action = Intent.ACTION_SEND
            type = "text/plain"
            putExtra(Intent.EXTRA_TEXT, "Check out this profile on Gnam.my: ${user.username}")
        }
        val shareIntent = Intent.createChooser(sendIntent, "Share profile")
        if (shareIntent.resolveActivity(ctx.packageManager) != null) {
            ctx.startActivity(shareIntent)
        }
    }

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
                uri = Uri.parse(user.imageUri),
                size = Size.Sm,
                description = "propic",
                modifier = Modifier
                    .border(2.dp, MaterialTheme.colorScheme.primaryContainer, CircleShape)
                    .size(120.dp)
                    .clip(CircleShape)
            )

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = user.username,
                    style = MaterialTheme.typography.headlineMedium.copy()
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
                        text = user.location ?: stringResource(R.string.profile_location_unknown),
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
                            text = user.followers.toString(),
                            style = MaterialTheme.typography.titleSmall.copy()
                        )
                    }
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(
                            text = stringResource(R.string.profile_followed),
                            style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold)
                        )
                        Text(
                            text = user.following.toString(),
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
            if (user.id != loggedUserId) { // Hide follow button if it's the user's own profile
                Button(
                    modifier = Modifier
                        .weight(0.4f)
                        .padding(end = 8.dp),
                    onClick = { /* Follow action */ }
                ) {
                    Text(text = stringResource(R.string.profile_follow))
                }
            }
            Button(
                modifier = Modifier
                    .weight(0.4f)
                    .padding(start = 8.dp, end = 8.dp),
                onClick = ::shareProfile
            ) {
                Text(text = stringResource(R.string.profile_share))
            }
            if (user.id == loggedUserId) { // Show settings button only if it's the user's own profile
                Button(
                    modifier = Modifier
                        .weight(0.2f)
                        .padding(start = 8.dp),
                    onClick = { showDialog = true }
                ) {
                    Icon(imageVector = Icons.Filled.Settings, contentDescription = "Settings")
                }
            }

            if (showDialog) {
                SettingsModal(onDismissRequest = { showDialog = false }, user = user, themeViewModel)
            }
        }

        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            modifier = Modifier
                .fillMaxWidth()
                .height(600.dp)
        ) {
            items(10) {
                //RecipeCardSmall(navController, Modifier.padding(5.dp))
            }
        }
    }
}

@Composable
fun SettingsModal(onDismissRequest: () -> Unit, user: User, themeViewModel: ThemeViewModel) {
    var username by remember { mutableStateOf(TextFieldValue(user.username)) }
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
                    /*Column(
                        modifier = Modifier.padding(top = 16.dp, bottom = 8.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = if (isDarkTheme) stringResource(R.string.profile_night_mode) else stringResource(
                                R.string.profile_light_mode
                            )
                        )
                        Switch(
                            checked = isDarkTheme,
                            onCheckedChange = { isDarkTheme = it }
                        )
                    }*/

                    Column(modifier = Modifier.fillMaxSize()) {
                        Button(onClick = { themeViewModel.selectTheme(Themes.Light) }) {
                            Text("Tema Light")
                        }
                        Button(onClick = { themeViewModel.selectTheme(Themes.Dark) }) {
                            Text("Tema Dark")
                        }
                        Button(onClick = { themeViewModel.selectTheme(Themes.Auto) }) {
                            Text("Tema Automatico")
                        }
                        Button(onClick = { themeViewModel.selectTheme(Themes.Dynamic) }) {
                            Text("Tema Dinamico")
                        }
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
                        Text(
                            text = if (profilePictureUri == null) {
                                stringResource(R.string.profile_select_propic)
                            } else {
                                "Selected: " + profilePictureUri?.lastPathSegment
                            }
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