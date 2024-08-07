package com.example.gnammy.ui.screens.profile

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.provider.Settings
import android.widget.Toast
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
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.outlined.PhotoCamera
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonDefaults
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import com.example.gnammy.R
import com.example.gnammy.data.local.entities.User
import com.example.gnammy.ui.composables.ImageWithPlaceholder
import com.example.gnammy.ui.composables.RecipeCardSmall
import com.example.gnammy.ui.theme.Themes
import com.example.gnammy.ui.viewmodels.GnamViewModel
import com.example.gnammy.ui.viewmodels.SettingsActions
import com.example.gnammy.ui.viewmodels.SettingsState
import com.example.gnammy.ui.viewmodels.ThemeViewModel
import com.example.gnammy.ui.viewmodels.UserViewModel
import com.example.gnammy.utils.LocationService
import com.example.gnammy.utils.PermissionStatus
import com.example.gnammy.utils.isOnline
import com.example.gnammy.utils.rememberCameraLauncher
import com.example.gnammy.utils.rememberPermission
import kotlinx.coroutines.runBlocking
import org.koin.compose.koinInject

@Composable
fun ProfileScreen(
    userId: String,
    navController: NavHostController,
    userViewModel: UserViewModel,
    loggedUserId: String,
    themeViewModel: ThemeViewModel,
    gnamViewModel: GnamViewModel,
    actions: SettingsActions,
    state: SettingsState
) {
    val ctx = LocalContext.current
    val offline = remember { mutableStateOf(true) }
    val loading = remember { mutableStateOf(false) }
    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(Unit) {
        if (isOnline(ctx)) {
            offline.value = false
            loading.value = true
            userViewModel.fetchUser(userId)
            gnamViewModel.addCurrentUserGnams(userId)
            loading.value = false
        } else {
            offline.value = true
        }
    }

    // Location
    val locationService = koinInject<LocationService>()

    val locationPermission = rememberPermission(
        if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.S) {
            Manifest.permission.ACCESS_FINE_LOCATION
        } else {
            Manifest.permission.ACCESS_COARSE_LOCATION
        }
    ) { status ->
        when (status) {
            PermissionStatus.Granted ->
                locationService.requestCurrentLocation()

            PermissionStatus.Denied ->
                actions.setShowLocationPermissionDeniedAlert(true)

            PermissionStatus.PermanentlyDenied ->
                actions.setShowLocationPermissionPermanentlyDeniedSnackbar(true)

            PermissionStatus.Unknown -> {}
        }
    }

    fun requestLocation() {
        if (locationPermission.status.isGranted) {
            locationService.requestCurrentLocation()
        } else {
            locationPermission.launchPermissionRequest()
        }
    }

    LaunchedEffect(locationService.isLocationEnabled) {
        actions.setShowLocationDisabledAlert(locationService.isLocationEnabled == false)
    }

    fun openWirelessSettings() {
        val intent = Intent(Settings.ACTION_WIRELESS_SETTINGS).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK
        }
        if (intent.resolveActivity(ctx.applicationContext.packageManager) != null) {
            ctx.applicationContext.startActivity(intent)
        }
    }

    if (state.showLocationDisabledAlert) {
        AlertDialog(
            title = { Text("Posizione disabilitata") },
            text = { Text("La posizione deve essere abilitata per ottenere il luogo corrente nell'app.") },
            confirmButton = {
                TextButton(onClick = {
                    locationService.openLocationSettings()
                    actions.setShowLocationDisabledAlert(false)
                }) {
                    Text("Attiva")
                }
            },
            dismissButton = {
                TextButton(onClick = { actions.setShowLocationDisabledAlert(false) }) {
                    Text("Chiudi")
                }
            },
            onDismissRequest = { actions.setShowLocationDisabledAlert(false) }
        )
    }

    if (state.showLocationPermissionDeniedAlert) {
        AlertDialog(
            title = { Text("Autorizzazione alla Posizione negata") },
            text = { Text("L'autorizzazione alla posizione è necessaria per ottenere il luogo corrente nell'app.") },
            confirmButton = {
                TextButton(onClick = {
                    locationPermission.launchPermissionRequest()
                    actions.setShowLocationPermissionDeniedAlert(false)
                }) {
                    Text("Attiva")
                }
            },
            dismissButton = {
                TextButton(onClick = { actions.setShowLocationPermissionDeniedAlert(false) }) {
                    Text("Chiudi")
                }
            },
            onDismissRequest = { actions.setShowLocationPermissionDeniedAlert(false) }
        )
    }

    if (state.showLocationPermissionPermanentlyDeniedSnackbar) {
        LaunchedEffect(snackbarHostState) {
            val res = snackbarHostState.showSnackbar(
                "È necessaria l'autorizzazione alla Posizione.",
                "Vai alle Impostazioni",
                duration = SnackbarDuration.Long
            )
            if (res == SnackbarResult.ActionPerformed) {
                ctx.startActivity(
                    Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS).apply {
                        data = Uri.fromParts("package", ctx.packageName, null)
                        flags = Intent.FLAG_ACTIVITY_NEW_TASK
                    }
                )
            }
            actions.setShowLocationPermissionPermanentlyDeniedSnackbar(false)
        }
    }

    if (state.showNoInternetConnectivitySnackbar) {
        LaunchedEffect(snackbarHostState) {
            val res = snackbarHostState.showSnackbar(
                message = "Nessuna connessione Internet",
                actionLabel = "Vai alle Impostazioni",
                duration = SnackbarDuration.Long
            )
            if (res == SnackbarResult.ActionPerformed) {
                openWirelessSettings()
            }
            actions.setShowNoInternetConnectivitySnackbar(false)
        }
    }

    if (loading.value) { // Loading state
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            CircularProgressIndicator()
        }
    } else if (offline.value) { // Offline state
        if (userId == loggedUserId) {   // If it's the logged user's profile, show the cached data
            val usersState by userViewModel.state.collectAsStateWithLifecycle()
            usersState.users.find { it.id == userId }?.let { user ->
                profileView(
                    ctx = ctx,
                    user = user,
                    loggedUserId = loggedUserId,
                    themeViewModel = themeViewModel,
                    gnamViewModel = gnamViewModel,
                    navHostController = navController,
                    userViewModel = userViewModel,
                    ::requestLocation,
                    locationService,
                    offline.value
                )
            }
        } else {
            Box(modifier = Modifier.fillMaxSize()) {    // If it's not the logged user's profile, show a message
                Text(
                    "Connettiti ad internet per visualizzare il profilo.",
                    modifier = Modifier.align(Alignment.Center)
                )
            }
        }
    } else {    // Online state
        val userState by userViewModel.state.collectAsStateWithLifecycle()

        userState.users.find { it.id == userId }?.let { user ->
            profileView(
                ctx = ctx,
                user = user,
                loggedUserId = loggedUserId,
                themeViewModel = themeViewModel,
                gnamViewModel = gnamViewModel,
                navHostController = navController,
                userViewModel = userViewModel,
                ::requestLocation,
                locationService,
                offline.value
            )
        }
    }
}

@Composable
fun profileView(
    ctx: Context,
    user: User,
    loggedUserId: String,
    themeViewModel: ThemeViewModel,
    gnamViewModel: GnamViewModel,
    navHostController: NavHostController,
    userViewModel: UserViewModel,
    requestLocation: () -> Unit,
    locationService: LocationService,
    offline: Boolean
) {
    var showDialog by remember { mutableStateOf(false) }
    val gnamsToShow = gnamViewModel.state.collectAsState()
        .value.gnams.filter {
            it.authorId == user.id
        }
    val isUserFollowing by userViewModel.currentFollowStatus.collectAsState()

    if (!offline) {
        userViewModel.checkFollowStatus(user.id)
    }

    fun shareProfile() {
        val sendIntent = Intent().apply {
            action = Intent.ACTION_SEND
            type = "text/plain"
            putExtra(
                Intent.EXTRA_TEXT,
                "Dai un'occhiata a questo profilo su Gnam.my: ${user.username}"
            )
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
                    style = user.username.length.let {
                        if (it > 10) MaterialTheme.typography.headlineSmall.copy()
                        else MaterialTheme.typography.headlineMedium.copy()
                    }
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
                            text = gnamsToShow.size.toString(),
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
                    colors = if (isUserFollowing.toString() == "true") {
                        ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.tertiary)
                    } else {
                        ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)
                    },
                    onClick = {
                        runBlocking {
                            userViewModel.toggleFollowUser(user.id)
                            userViewModel.fetchUser(user.id)
                        }
                    }
                ) {
                    Text(
                        text = if (isUserFollowing.toString() == "true") {
                            stringResource(R.string.profile_being_followed)
                        } else {
                            stringResource(R.string.profile_follow)
                        }
                    )
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
                SettingsModal(
                    onDismissRequest = { showDialog = false },
                    user = user,
                    themeViewModel,
                    userViewModel,
                    requestLocation,
                    locationService,
                    navHostController
                )
            }
        }

        if (gnamsToShow.isNotEmpty()) {
            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(600.dp)
            ) {
                items(gnamsToShow) { gnam ->
                    RecipeCardSmall(gnam = gnam, navHostController, Modifier.padding(5.dp))
                }
            }
        } else {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(600.dp),
                contentAlignment = Alignment.Center
            ) {
                Text("Non hai ancora postato degli Gnam.")
            }
        }
    }
}

@SuppressLint("StateFlowValueCalledInComposition")
@Composable
fun SettingsModal(
    onDismissRequest: () -> Unit,
    user: User,
    themeViewModel: ThemeViewModel,
    userViewModel: UserViewModel,
    requestLocation: () -> Unit,
    locationService: LocationService,
    navHostController: NavHostController
) {
    var username by remember { mutableStateOf(TextFieldValue(user.username)) }
    var profilePictureUri by remember { mutableStateOf<Uri?>(null) }
    var isRequestingLocation by remember { mutableStateOf(false) }
    var showSnackbar by remember { mutableStateOf(false) }
    var homeBtnEnabled by remember { mutableStateOf(userViewModel.homeBtnEnabled.value) }
    val ctx = LocalContext.current

    val snackbarHostState = remember { SnackbarHostState() }

    val cameraLauncher = rememberCameraLauncher { imageUri ->
        profilePictureUri = imageUri
    }

    val cameraPermission = rememberPermission(Manifest.permission.CAMERA) { status ->
        if (status.isGranted) {
            cameraLauncher.captureImage()
        } else {
            Toast.makeText(ctx, "Permission denied", Toast.LENGTH_SHORT).show()
        }
    }

    fun takePicture() {
        if (cameraPermission.status.isGranted) {
            cameraLauncher.captureImage()
        } else {
            cameraPermission.launchPermissionRequest()
        }
    }

    Dialog(onDismissRequest = { onDismissRequest() }) {
        Box(
            modifier = Modifier.fillMaxSize()
        ) {
            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 8.dp, start = 8.dp)
                    .align(Alignment.Center),
                shape = MaterialTheme.shapes.medium
            ) {
                Column(
                    modifier = Modifier
                        .padding(16.dp)
                        .verticalScroll(rememberScrollState())
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(
                            text = "Impostazioni",
                            style = MaterialTheme.typography.headlineSmall.copy(),
                            textAlign = TextAlign.Center,
                            modifier = Modifier.weight(0.9f)
                        )

                        IconButton(
                            onClick = { onDismissRequest() },
                            modifier = Modifier.weight(0.1f)
                        ) {
                            Icon(
                                imageVector = Icons.Filled.Close,
                                contentDescription = "Close"
                            )
                        }
                    }

                    RadioButtonGroup(
                        selectedOption = themeViewModel.theme.value,
                        options = listOf(Themes.Light, Themes.Dark, Themes.Auto, Themes.Dynamic),
                        onOptionSelected = { themeViewModel.selectTheme(it) },
                        modifier = Modifier.padding(vertical = 4.dp)
                    )

                    Text(
                        text = "Bottoni circolari in home:",
                        style = MaterialTheme.typography.bodyLarge,
                        modifier = Modifier.padding(bottom = 8.dp, start = 8.dp)
                    )
                    Switch(
                        checked = homeBtnEnabled,
                        onCheckedChange = { isChecked ->
                            userViewModel.toggleHomeBtn()
                            homeBtnEnabled = isChecked
                        },
                        modifier = Modifier.fillMaxWidth()
                    )

                    Text(
                        text = "Aggiorna la posizione mostrata:",
                        style = MaterialTheme.typography.bodyLarge,
                        modifier = Modifier.padding(bottom = 8.dp, start = 8.dp)
                    )
                    Button(
                        onClick = {
                            if (isOnline(ctx)) {
                                requestLocation()
                                isRequestingLocation = true
                            } else {
                                showSnackbar = true
                            }
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 8.dp)
                    ) {
                        Text(text = stringResource(R.string.profile_update_gps))
                    }

                    if (isRequestingLocation) {
                        LaunchedEffect(locationService.coordinates) {
                            if (locationService.coordinates != null) {
                                userViewModel.updateUserLocation(locationService.coordinates!!)
                                onDismissRequest()
                                isRequestingLocation = false
                            }
                        }
                    }

                    Text(
                        text = "Modifica il tuo username:",
                        style = MaterialTheme.typography.bodyLarge,
                        modifier = Modifier.padding(bottom = 8.dp, start = 8.dp)
                    )
                    OutlinedTextField(
                        value = username,
                        onValueChange = { username = it },
                        label = { Text("Username") },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 16.dp),
                        shape = RoundedCornerShape(30.dp)
                    )

                    Text(
                        text = "Modifica l'immagine del profilo:",
                        style = MaterialTheme.typography.bodyLarge,
                        modifier = Modifier.padding(bottom = 8.dp, start = 8.dp)
                    )
                    Button(
                        onClick = { takePicture() },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 8.dp)
                    ) {
                        Icon(
                            Icons.Outlined.PhotoCamera,
                            contentDescription = "Camera icon",
                            modifier = Modifier.size(ButtonDefaults.IconSize)
                        )
                        Spacer(Modifier.size(ButtonDefaults.IconSpacing))
                        Text(
                            text = if (profilePictureUri == null) {
                                stringResource(R.string.profile_update_propic)
                            } else {
                                "Selezionato: " + profilePictureUri?.lastPathSegment
                            }
                        )
                    }

                    Button(
                        onClick = {
                            runBlocking {
                                userViewModel.updateUserData(
                                    ctx,
                                    username.text,
                                    profilePictureUri
                                )
                            }
                            onDismissRequest()
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.tertiary),
                        modifier = Modifier
                            .fillMaxWidth()
                    ) {
                        Text(text = stringResource(R.string.profile_save))
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    Button(
                        onClick = {
                            runBlocking { userViewModel.logout() }
                            onDismissRequest()
                            navHostController.navigate("login") {
                                popUpTo("login") { inclusive = true }
                            }
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.error),
                        modifier = Modifier
                            .fillMaxWidth(0.5f)
                            .align(Alignment.CenterHorizontally)
                    ) {
                        Text(text = stringResource(R.string.profile_logout), color = Color.White)
                    }
                }
            }

            // Snackbar host
            SnackbarHost(
                hostState = snackbarHostState,
                modifier = Modifier.align(Alignment.BottomCenter)
            )
        }
    }

    // Show snackbar if required
    if (showSnackbar) {
        LaunchedEffect(snackbarHostState) {
            snackbarHostState.showSnackbar("Connettiti a internet")
            showSnackbar = false
        }
    }
}


@Composable
fun RadioButtonGroup(
    selectedOption: Themes,
    options: List<Themes>,
    onOptionSelected: (Themes) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier) {
        Text(
            text = "Seleziona un tema:",
            style = MaterialTheme.typography.bodyLarge,
            modifier = Modifier.padding(bottom = 8.dp, start = 8.dp)
        )
        options.forEach { theme ->
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
            ) {
                RadioButton(
                    selected = theme == selectedOption,
                    onClick = { onOptionSelected(theme) },
                    colors = RadioButtonDefaults.colors(selectedColor = MaterialTheme.colorScheme.primary)
                )
                Text(
                    text = theme.name,
                    style = MaterialTheme.typography.bodyLarge,
                    modifier = Modifier.padding(start = 4.dp)
                )
            }
        }
    }
}
