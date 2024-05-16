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
import com.example.gnammy.ui.screens.home.HomeScreen
import com.example.gnammy.ui.screens.post.PostScreen
import com.example.gnammy.ui.screens.profile.ProfileScreen
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

    companion object { // TODO Si potrebbe togliere perchè era usato per ottenere il nome della pagina corrente
        val routes = setOf(Home, Search, Post, Saved, Profile)
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
                HomeScreen(navController)
            }
        }
        with(GnammyRoute.Search) {
            composable(route) {
                SearchScreen(navController)
            }
        }
        with(GnammyRoute.Post) {
            composable(route) {
                PostScreen(navController)
            }
        }
        with(GnammyRoute.Saved) {
            composable(route) {
                SavedScreen(navController)
            }
        }
        with(GnammyRoute.Profile) {
            composable(route) {
                ProfileScreen(navController)
            }
        }
    }
}
