package com.example.gnammy.data.repository

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.example.gnammy.ui.theme.Themes
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "theme_preferences")

class ThemeRepository(private val context: Context) {

    private val dataStore = context.dataStore

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