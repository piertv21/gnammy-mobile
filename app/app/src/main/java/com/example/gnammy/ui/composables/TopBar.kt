package com.example.gnammy.ui.composables

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.ArrowBack
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.gnammy.R
import com.example.gnammy.ui.GnammyRoute
import com.example.gnammy.ui.viewmodels.NotificationViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopBar(
    navController: NavHostController,
    currentRoute: GnammyRoute,
    notificationViewModel: NotificationViewModel
) {
    CenterAlignedTopAppBar(
        title = {
            Text(
                when (currentRoute.route) {
                    GnammyRoute.Home.route -> stringResource(R.string.route_home)
                    GnammyRoute.Notification.route -> stringResource(R.string.route_notification)
                    GnammyRoute.GnamDetails.route -> stringResource(R.string.route_gnam_details)
                    GnammyRoute.Goals.route -> stringResource(R.string.route_goals)
                    GnammyRoute.Profile.route -> stringResource(R.string.route_profile)
                    GnammyRoute.Search.route -> stringResource(R.string.route_search)
                    GnammyRoute.Post.route -> stringResource(R.string.route_post)
                    GnammyRoute.Saved.route -> stringResource(R.string.route_saved)
                    else -> stringResource(R.string.app_name)
                },
                fontWeight = FontWeight.Medium,
            )
        },
        navigationIcon = {
            if (currentRoute in setOf(
                    GnammyRoute.Notification,
                    GnammyRoute.GnamDetails,
                    GnammyRoute.Goals
                )
            ) {
                IconButton(onClick = { navController.navigateUp() }) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Outlined.ArrowBack,
                        contentDescription = "Back button"
                    )
                }
            }
        },
        actions = {
            if (currentRoute.route == GnammyRoute.Home.route) {
                val notificationState = notificationViewModel.state.collectAsState()
                BadgedBox(
                    modifier = Modifier
                        .padding(5.dp)
                        .padding(end = 10.dp)
                        .clickable { navController.navigate(GnammyRoute.Notification.route) },
                    badge = {
                        val notificationCount = notificationState.value.notifications.size
                        if (notificationCount > 0) {
                            Badge(
                                containerColor = MaterialTheme.colorScheme.error,
                                contentColor = MaterialTheme.colorScheme.onPrimary,
                                modifier = Modifier
                                    .padding(end = 5.dp)
                            ) {
                                if (notificationCount > 99)
                                    Text(
                                        "99+", modifier = Modifier
                                            .padding(1.dp)
                                    )
                                else
                                    Text(
                                        "$notificationCount", modifier = Modifier
                                            .padding(1.dp)
                                    )
                            }
                        }
                    }
                ) {
                    Icon(Icons.Filled.Notifications, contentDescription = "Notifications")
                }
            }

        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer
        )
    )
}

