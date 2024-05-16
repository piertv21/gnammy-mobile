package com.example.gnammy.ui.composables

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Save
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.tooling.preview.Preview

/*
 It shows a bottom navigation bar with 5 items.
 Usage: use it on scaffold with bottomBar = { MyNavigationBar() }
 */
@Composable
@Preview
fun MyNavigationBar(onItemSelected: (Int) -> Unit) {
    var selectedItem by remember { mutableStateOf(0) }
    val itemsWithIcons = listOf(
        Pair("Home", Icons.Filled.Home),
        Pair("Cerca", Icons.Filled.Search),
        Pair("Posta", Icons.Filled.Add),
        Pair("Salvati", Icons.Filled.Favorite),
        Pair("Profilo", Icons.Filled.Person)
    )

    NavigationBar {
        itemsWithIcons.forEachIndexed { index, itemWithIcon ->
            NavigationBarItem(
                icon = { Icon(itemWithIcon.second, contentDescription = itemWithIcon.first) },
                label = { Text(itemWithIcon.first) },
                selected = selectedItem == index,
                onClick = {
                    selectedItem = index
                    onItemSelected(index) // TODO Callback on click
                }
            )
        }
    }
}