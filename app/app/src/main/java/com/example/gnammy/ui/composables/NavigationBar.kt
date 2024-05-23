import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavHostController
import androidx.wear.compose.material.ContentAlpha
import com.example.gnammy.R
import com.example.gnammy.ui.GnammyRoute


@Composable
fun NavigationBar(
    navController: NavHostController,
    currentRoute: GnammyRoute
) {
    val routesWithIcons = listOf(
        Pair(GnammyRoute.Home, Pair(stringResource(R.string.home_nav_button),  Icons.Filled.Home)),
        Pair(GnammyRoute.Search, Pair(stringResource(R.string.search_nav_button), Icons.Filled.Search)),
        Pair(GnammyRoute.Post, Pair(stringResource(R.string.publish_nav_button), Icons.Filled.Add)),
        Pair(GnammyRoute.Saved, Pair(stringResource(R.string.saved_nav_button), Icons.Filled.Favorite)),
        Pair(GnammyRoute.Profile, Pair(stringResource(R.string.profile_nav_button), Icons.Filled.Person))
    )

    NavigationBar {
        routesWithIcons.forEach { (route, itemWithIcon) ->
            val selected = currentRoute == route
            NavigationBarItem(
                icon = { Icon(itemWithIcon.second, contentDescription = itemWithIcon.first, tint = if (selected) LocalContentColor.current else LocalContentColor.current.copy(alpha = ContentAlpha.disabled)) },
                label = { Text(itemWithIcon.first, color = if (selected) LocalContentColor.current else LocalContentColor.current.copy(alpha = ContentAlpha.disabled)) },
                selected = selected,
                onClick = {
                    if (!selected) {
                        navController.navigate(route.route)
                    }
                }
            )
        }
    }
}
