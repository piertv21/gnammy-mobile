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
    val currentRoute by remember {
        derivedStateOf {
            GnammyRoute.routes.find {
                it.route == backStackEntry?.destination?.route
            } ?: GnammyRoute.Home
        }
    }

    val itemsWithIcons = listOf(
        Pair("Home", Icons.Filled.Home),
        Pair("Cerca", Icons.Filled.Search),
        Pair("Posta", Icons.Filled.Add),
        Pair("Salvati", Icons.Filled.Favorite),
        Pair("Profilo", Icons.Filled.Person)
    )

    var selectedItem by remember { mutableStateOf(0) }

    selectedItem = itemsWithIcons.indexOfFirst { it.first.equals(currentRoute) }

    NavigationBar {
        itemsWithIcons.forEachIndexed { index, itemWithIcon ->
            NavigationBarItem(
                icon = { Icon(itemWithIcon.second, contentDescription = itemWithIcon.first) },
                label = { Text(itemWithIcon.first) },
                selected = selectedItem == index,
                onClick = {
                    if (selectedItem != index) {
                        navController.navigate(GnammyRoute.routes[index].route)
                        selectedItem = index
                    }
                }
            )
        }
    }
}
