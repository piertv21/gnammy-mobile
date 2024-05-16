package com.example.gnammy.ui.composables

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController

/*
    * BasePage composable that displays the current page layout with the bottom navigation bar.
 */
@Composable
fun BasePage(
    navController: NavHostController,
    content: @Composable (PaddingValues) -> Unit
) {
    Scaffold(
        bottomBar = {
            MyNavigationBar(navController = navController)
        }
    ) { paddingValues ->
        content(paddingValues)
    }
}