import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.example.gnammy.ui.GnammyRoute

@Composable
fun MyNavigationBar(navController: NavHostController) {
    val backStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = backStackEntry?.destination?.route ?: ""

    val itemsWithIcons = listOf(
        Pair(GnammyRoute.Home, Pair("Home", Icons.Filled.Home)),
        Pair(GnammyRoute.Search, Pair("Cerca", Icons.Filled.Search)),
        Pair(GnammyRoute.Post, Pair("Posta", Icons.Filled.Add)),
        Pair(GnammyRoute.Saved, Pair("Salvati", Icons.Filled.Favorite)),
        Pair(GnammyRoute.Profile, Pair("Profilo", Icons.Filled.Person))
    )

    NavigationBar {
        itemsWithIcons.forEach { (route, itemWithIcon) ->
            val selected = currentRoute == route.route
            NavigationBarItem(
                icon = { Icon(itemWithIcon.second, contentDescription = itemWithIcon.first) },
                label = { Text(itemWithIcon.first) },
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
