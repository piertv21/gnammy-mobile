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
import com.example.gnammy.ui.viewmodels.GnamViewModel
import com.example.gnammy.ui.viewmodels.NotificationViewModel
import com.example.gnammy.ui.viewmodels.ThemeViewModel
import com.example.gnammy.ui.viewmodels.UserViewModel
import kotlinx.coroutines.runBlocking

sealed class GnammyRoute(
    val route: String,
    val title: String,
    val arguments: List<NamedNavArgument> = emptyList()
) {
    data object Home : GnammyRoute("home", "Home")
    data object Search : GnammyRoute("search", "Search")
    data object Post : GnammyRoute("post", "Post")
    data object Saved : GnammyRoute("saved", "Saved")
    data object Notification : GnammyRoute("notification", "Notification")
    data object Login : GnammyRoute("login", "Login")
    data object Register : GnammyRoute("register", "Register")
    data object GnamDetails : GnammyRoute(
        "gnamDetails/{gnamId}",
        "Gnam Details",
        listOf(navArgument("gnamId") { type = NavType.StringType })
    ) {
        fun buildRoute(gnamId: String) = "gnamDetails/$gnamId"
    }

    data object Profile : GnammyRoute(
        "profile/{userId}",
        "Profile",
        listOf(navArgument("userId") { type = NavType.StringType })
    ) {
        fun buildRoute(userId: String) = "profile/$userId"
    }

    data object Goals : GnammyRoute("goals", "Goals")

    companion object {
        val routes = setOf(
            Home,
            Search,
            Post,
            Saved,
            Profile,
            Notification,
            Login,
            Register,
            GnamDetails,
            Goals
        )
    }
}

@Composable
fun GnammyNavGraph(
    navController: NavHostController,
    themeViewModel: ThemeViewModel,
    startDestination: String,
    userViewModel: UserViewModel,
    gnamViewModel: GnamViewModel,
    notificationViewModel: NotificationViewModel,
    modifier: Modifier = Modifier
) {
    val usersState by userViewModel.state.collectAsStateWithLifecycle()
    val gnamState by gnamViewModel.state.collectAsStateWithLifecycle()

    val loggedUserId = runBlocking { userViewModel.getLoggedUserId() }

    NavHost(
        navController = navController,
        startDestination = startDestination,
        modifier = modifier
    ) {
        with(GnammyRoute.Home) {
            composable(route) {
                HomeScreen(
                    navController,
                    gnamViewModel,
                    notificationViewModel,
                    loggedUserId,
                    modifier
                )
            }
        }
        with(GnammyRoute.Search) {
            composable(route) {
                SearchScreen(navController)
            }
        }
        with(GnammyRoute.Post) {
            composable(route) {
                PostScreen(gnamViewModel, loggedUserId)
            }
        }
        with(GnammyRoute.Saved) {
            composable(route) {
                SavedScreen(navController, gnamViewModel, loggedUserId)
            }
        }
        with(GnammyRoute.Profile) {
            composable(route, arguments) { backStackEntry ->
                val user = requireNotNull(usersState.users.find {
                    it.id == (backStackEntry.arguments?.getString("userId") ?: loggedUserId)
                })
                ProfileScreen(
                    user,
                    navController,
                    modifier,
                    userViewModel,
                    loggedUserId,
                    themeViewModel
                )
            }
        }
        with(GnammyRoute.Notification) {
            composable(route) {
                NotificationScreen(navController, notificationViewModel, userViewModel, modifier)
            }
        }
        with(GnammyRoute.Login) {
            composable(route) {
                LoginScreen(navController, userViewModel)
            }
        }
        with(GnammyRoute.Register) {
            composable(route) {
                RegisterScreen(navController, userViewModel)
            }
        }
        with(GnammyRoute.GnamDetails) {
            composable(route, arguments) { backStackEntry ->
                val gnam = requireNotNull(gnamState.gnams.find {
                    it.id == backStackEntry.arguments?.getString("gnamId")
                })
                GnamDetailsScreen(navController, gnam)
            }
        }
        with(GnammyRoute.Goals) {
            composable(route) {
                GoalsScreen()
            }
        }
    }
}

