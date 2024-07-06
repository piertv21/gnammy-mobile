package com.example.gnammy

import NavigationBar
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.gnammy.ui.GnammyNavGraph
import com.example.gnammy.ui.GnammyRoute
import com.example.gnammy.ui.composables.TopBar
import com.example.gnammy.ui.theme.AutomaticTheme
import com.example.gnammy.ui.theme.DarkTheme
import com.example.gnammy.ui.theme.DynamicTheme
import com.example.gnammy.ui.theme.LightTheme
import com.example.gnammy.ui.theme.Themes
import com.example.gnammy.ui.viewmodels.GnamViewModel
import com.example.gnammy.ui.viewmodels.NotificationViewModel
import com.example.gnammy.ui.viewmodels.ThemeViewModel
import com.example.gnammy.ui.viewmodels.UserViewModel
import kotlinx.coroutines.runBlocking
import org.koin.androidx.compose.koinViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            val themeViewModel: ThemeViewModel = koinViewModel<ThemeViewModel>()
            val selectedTheme by themeViewModel.theme.collectAsState()

            when (selectedTheme) {
                Themes.Light -> LightTheme { MainScreen(themeViewModel) }
                Themes.Dark -> DarkTheme { MainScreen(themeViewModel) }
                Themes.Auto -> AutomaticTheme { MainScreen(themeViewModel) }
                Themes.Dynamic -> DynamicTheme { MainScreen(themeViewModel) }
            }
        }
    }

    override fun onPause() {
        super.onPause()
        //locationService.pauseLocationRequest()
    }

    override fun onResume() {
        super.onResume()
        //locationService.resumeLocationRequest()
    }
}

@Composable
fun MainScreen(themeViewModel: ThemeViewModel) {
    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        val userViewModel = koinViewModel<UserViewModel>()
        val loggedUserId = runBlocking { userViewModel.getLoggedUserId() }
        var startDestination by remember { mutableStateOf(GnammyRoute.routes.first()) }

        startDestination = if (loggedUserId == "NOT SET") {
            GnammyRoute.Login
        } else {
            GnammyRoute.Home
        }
        if (loggedUserId.isNotEmpty()) {
            userViewModel.fetchUser(loggedUserId)
        }

        val navController = rememberNavController()
        val backStackEntry by navController.currentBackStackEntryAsState()
        val currentRoute by remember {
            derivedStateOf {
                GnammyRoute.routes.find {
                    it.route == backStackEntry?.destination?.route
                } ?: startDestination
            }
        }

        val gnamViewModel = koinViewModel<GnamViewModel>()
        val notificationViewModel = koinViewModel<NotificationViewModel>()

        Scaffold(
            bottomBar = {
                if (currentRoute !in setOf(GnammyRoute.Login, GnammyRoute.Register)) {
                    NavigationBar(navController, currentRoute, userViewModel)
                }
            },
            topBar = {
                TopBar(navController, currentRoute, notificationViewModel)
            }
        ) { contentPadding ->
            GnammyNavGraph(
                navController,
                themeViewModel,
                startDestination.route,
                userViewModel,
                gnamViewModel,
                notificationViewModel,
                modifier = Modifier.padding(contentPadding)
            )
        }
    }
}