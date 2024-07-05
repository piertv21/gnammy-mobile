package com.example.gnammy.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.Composable

enum class Themes {
    Light,
    Dark,
    Auto,
    Dynamic
}

@Composable
fun LightTheme(content: @Composable () -> Unit) {
    GnammyTheme(
        darkTheme = false,
        dynamicColor = false,
        content = content
    )
}

@Composable
fun DarkTheme(content: @Composable () -> Unit) {
    GnammyTheme(
        darkTheme = true,
        dynamicColor = false,
        content = content
    )
}

@Composable
fun AutomaticTheme(content: @Composable () -> Unit) {
    GnammyTheme(
        darkTheme = isSystemInDarkTheme(),
        dynamicColor = false,
        content = content
    )
}

@Composable
fun DynamicTheme(content: @Composable () -> Unit) {
    GnammyTheme(
        darkTheme = isSystemInDarkTheme(),
        dynamicColor = true,
        content = content
    )
}
