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
import com.example.gnammy.ui.screens.addtravel.AddTravelScreen
import com.example.gnammy.ui.screens.addtravel.AddTravelViewModel
import com.example.gnammy.ui.screens.home.HomeScreen
import com.example.gnammy.ui.screens.settings.SettingsScreen
import com.example.gnammy.ui.screens.settings.SettingsViewModel
import com.example.gnammy.ui.screens.traveldetails.TravelDetailsScreen
import org.koin.androidx.compose.koinViewModel

sealed class TravelDiaryRoute(
    val route: String,
    val title: String,
    val arguments: List<NamedNavArgument> = emptyList()
) {
    data object Home : TravelDiaryRoute("travels", "TravelDiary")
    data object TravelDetails : TravelDiaryRoute(
        "travels/{travelId}",
        "Travel Details",
        listOf(navArgument("travelId") { type = NavType.StringType })
    ) {
        fun buildRoute(travelId: String) = "travels/$travelId"
    }
    data object AddTravel : TravelDiaryRoute("travels/add", "Add Travel")
    data object Settings : TravelDiaryRoute("settings", "Settings")

    companion object {
        val routes = setOf(Home, TravelDetails, AddTravel, Settings)
    }
}

@Composable
fun TravelDiaryNavGraph(
    navController: NavHostController,
    modifier: Modifier = Modifier
) {
    val placesVm = koinViewModel<PlacesViewModel>()
    val placesState by placesVm.state.collectAsStateWithLifecycle()

    NavHost(
        navController = navController,
        startDestination = TravelDiaryRoute.Home.route,
        modifier = modifier
    ) {
        with(TravelDiaryRoute.Home) {
            composable(route) {
                HomeScreen(placesState, navController)
            }
        }
        with(TravelDiaryRoute.TravelDetails) {
            composable(route, arguments) { backStackEntry ->
                val place = requireNotNull(placesState.places.find {
                    it.id == backStackEntry.arguments?.getString("travelId")?.toInt()
                })
                TravelDetailsScreen(place)
            }
        }
        with(TravelDiaryRoute.AddTravel) {
            composable(route) {
                val addTravelVm = koinViewModel<AddTravelViewModel>()
                val state by addTravelVm.state.collectAsStateWithLifecycle()
                AddTravelScreen(
                    state = state,
                    actions = addTravelVm.actions,
                    onSubmit = { placesVm.addPlace(state.toPlace()) },
                    navController = navController
                )
            }
        }
        with(TravelDiaryRoute.Settings) {
            composable(route) {
                val settingsVm = koinViewModel<SettingsViewModel>()
                SettingsScreen(settingsVm.state, settingsVm::setUsername)
            }
        }
    }
}
