package com.example.gnammy.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NamedNavArgument
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.gnammy.ui.screens.gnamdetails.GnamDetailsScreen
import com.example.gnammy.ui.screens.goals.GoalsScreen
import com.example.gnammy.ui.screens.home.HomeScreen
import com.example.gnammy.ui.screens.login.LoginScreen
import com.example.gnammy.ui.screens.notification.NotificationScreen
import com.example.gnammy.ui.screens.post.PostScreen
import com.example.gnammy.ui.screens.profile.ProfileScreen
import com.example.gnammy.ui.screens.register.RegisterScreen
import com.example.gnammy.ui.screens.saved.SavedScreen
import com.example.gnammy.ui.screens.search.SearchScreen
import org.koin.androidx.compose.koinViewModel

sealed class GnammyRoute(
    val route: String,
    val title: String,
    val arguments: List<NamedNavArgument> = emptyList()
) {
    data object Home : GnammyRoute("home", "Home")
    data object Search : GnammyRoute("search", "Search")
    data object Post : GnammyRoute("post", "Post")
    data object Saved : GnammyRoute("saved", "Saved")
    data object Profile : GnammyRoute("profile", "Profile")
    data object Notification : GnammyRoute("notification", "Notification")
    data object Login : GnammyRoute("login", "Login")
    data object Register : GnammyRoute("register", "Register")
    data object GnamDetails : GnammyRoute("gnamDetails", "Gnam Details")
    data object Goals : GnammyRoute("goals", "Goals")

    companion object {
        val routes = setOf(Home, Search, Post, Saved, Profile, Notification, Login, Register, GnamDetails, Goals)
    }
}

@Composable
fun GnammyNavGraph(
    navController: NavHostController,
    modifier: Modifier = Modifier
) {
    NavHost(
        navController = navController,
        startDestination = GnammyRoute.Home.route, // TODO Check if user is logged in
        modifier = modifier
    ) {
        with(GnammyRoute.Home) {
            composable(route) {
                HomeScreen(navController, modifier)
            }
        }
        with(GnammyRoute.Search) {
            composable(route) {
                SearchScreen(navController)
            }
        }
        with(GnammyRoute.Post) {
            composable(route) {
                PostScreen(navController, modifier)
            }
        }
        with(GnammyRoute.Saved) {
            composable(route) {
                SavedScreen(navController)
            }
        }
        with(GnammyRoute.Profile) {
            composable(route) {
                ProfileScreen(navController, modifier)
            }
        }
        with(GnammyRoute.Notification) {
            composable(route) {
                NotificationScreen(navController, modifier)
            }
        }
        with(GnammyRoute.Login) {
            composable(route) {
                LoginScreen(navController)
            }
        }
        with(GnammyRoute.Register) {
            composable(route) {
                RegisterScreen(navController)
            }
        }
        with(GnammyRoute.GnamDetails) {
            composable(route) {
                GnamDetailsScreen(navController)
            }
        }
        with(GnammyRoute.Goals) {
            composable(route) {
                GoalsScreen()
            }
        }
    }
}
