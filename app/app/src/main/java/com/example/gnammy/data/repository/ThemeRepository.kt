package com.example.gnammy.data.repository

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import com.example.gnammy.ui.theme.Themes
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class ThemeRepository(private val dataStore: DataStore<Preferences>) {

    fun getThemePreference(): Flow<Themes> {
        return dataStore.data.map { preferences ->
            val themePreferenceKey = stringPreferencesKey("theme_preference")
            val themePreferenceValue = preferences[themePreferenceKey] ?: Themes.Auto.name
            Themes.valueOf(themePreferenceValue)
        }
    }

    suspend fun saveThemePreference(themePreference: Themes) {
        dataStore.edit { preferences ->
            val themePreferenceKey = stringPreferencesKey("theme_preference")
            preferences[themePreferenceKey] = themePreference.name
        }
    }
}