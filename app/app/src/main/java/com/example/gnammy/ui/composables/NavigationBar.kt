import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.wear.compose.material.ContentAlpha
import com.example.gnammy.R
import com.example.gnammy.ui.GnammyRoute
import com.example.gnammy.ui.viewmodels.UserViewModel
import kotlinx.coroutines.runBlocking


@Composable
fun NavigationBar(
    navController: NavHostController,
    currentRoute: GnammyRoute,
    userViewModel: UserViewModel
) {
    val routesWithIcons = listOf(
        Pair(GnammyRoute.Home, Pair(stringResource(R.string.home_nav_button), Icons.Filled.Home)),
        Pair(
            GnammyRoute.Search,
            Pair(stringResource(R.string.search_nav_button), Icons.Filled.Search)
        ),
        Pair(GnammyRoute.Post, Pair(stringResource(R.string.publish_nav_button), Icons.Filled.Add)),
        Pair(
            GnammyRoute.Saved,
            Pair(stringResource(R.string.saved_nav_button), Icons.Filled.Favorite)
        ),
        Pair(
            GnammyRoute.Profile,
            Pair(stringResource(R.string.profile_nav_button), Icons.Filled.Person)
        )
    )

    NavigationBar(
        containerColor = MaterialTheme.colorScheme.primaryContainer
    ) {
        routesWithIcons.forEach { (route, itemWithIcon) ->
            val selected = currentRoute == route
            NavigationBarItem(
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = MaterialTheme.colorScheme.onSurface,
                    selectedTextColor = MaterialTheme.colorScheme.onSurface,
                    indicatorColor = MaterialTheme.colorScheme.onPrimary,
                    unselectedIconColor = MaterialTheme.colorScheme.onSurface,
                    unselectedTextColor = MaterialTheme.colorScheme.onSurface,
                    disabledIconColor = MaterialTheme.colorScheme.primary,
                    disabledTextColor = MaterialTheme.colorScheme.primary,
                ),
                icon = {
                    Icon(
                        itemWithIcon.second,
                        contentDescription = itemWithIcon.first,
                        tint = if (selected) LocalContentColor.current else LocalContentColor.current.copy(
                            alpha = ContentAlpha.disabled
                        )
                    )
                },
                label = {
                    Text(
                        itemWithIcon.first,
                        color = if (selected) LocalContentColor.current else LocalContentColor.current.copy(
                            alpha = ContentAlpha.disabled
                        )
                    )
                },
                selected = selected,
                onClick = {
                    if (!selected) {
                        if (route == GnammyRoute.Profile) {
                            val loggedId = runBlocking { userViewModel.getLoggedUserId() }
                            navController.navigate(GnammyRoute.Profile.buildRoute(loggedId))
                        } else {
                            navController.navigate(route.route)
                        }
                    }
                }
            )
        }
    }
}
