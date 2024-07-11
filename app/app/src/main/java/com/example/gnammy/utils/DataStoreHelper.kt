package com.example.gnammy.utils

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import java.io.IOException

class DataStoreHelper(private val context: Context, private val dataStoreName: String) {

    private val Context.dataStore by preferencesDataStore(name = dataStoreName)

    suspend fun putStringValue(key: String, value: String) {
        val dataStoreKey = stringPreferencesKey(key)
        context.dataStore.edit { preferences ->
            preferences[dataStoreKey] = value
        }
    }

    suspend fun putIntValue(key: String, value: Int) {
        val dataStoreKey = intPreferencesKey(key)
        context.dataStore.edit { preferences ->
            preferences[dataStoreKey] = value
        }
    }

    suspend fun putBooleanValue(key: String, value: Boolean) {
        val dataStoreKey = booleanPreferencesKey(key)
        context.dataStore.edit { preferences ->
            preferences[dataStoreKey] = value
        }
    }

    suspend fun removeValue(key: String) {
        val stringKey = stringPreferencesKey(key)
        val intKey = intPreferencesKey(key)
        val booleanKey = booleanPreferencesKey(key)
        context.dataStore.edit { preferences ->
            preferences.remove(stringKey)
            preferences.remove(intKey)
            preferences.remove(booleanKey)
        }
    }

    fun getStringValue(key: String): Flow<String?> {
        val dataStoreKey = stringPreferencesKey(key)
        return context.dataStore.data
            .catch { exception ->
                if (exception is IOException) {
                    emit(emptyPreferences())
                } else {
                    throw exception
                }
            }
            .map { preferences ->
                preferences[dataStoreKey]
            }
    }

    fun getIntValue(key: String): Flow<Int?> {
        val dataStoreKey = intPreferencesKey(key)
        return context.dataStore.data
            .catch { exception ->
                if (exception is IOException) {
                    emit(emptyPreferences())
                } else {
                    throw exception
                }
            }
            .map { preferences ->
                preferences[dataStoreKey]
            }
    }

    fun getBooleanValue(key: String): Flow<Boolean?> {
        val dataStoreKey = booleanPreferencesKey(key)
        return context.dataStore.data
            .catch { exception ->
                if (exception is IOException) {
                    emit(emptyPreferences())
                } else {
                    throw exception
                }
            }
            .map { preferences ->
                preferences[dataStoreKey]
            }
    }

    suspend fun clearAll() {
        context.dataStore.edit { preferences ->
            preferences.clear()
        }
    }
}