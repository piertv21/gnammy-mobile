package com.example.gnammy

import android.content.Context
import androidx.datastore.preferences.preferencesDataStore
import org.koin.dsl.module

val Context.dataStore by preferencesDataStore("settings")

val appModule = module {
    // ROBA QUI
}
