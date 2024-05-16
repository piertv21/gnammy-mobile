package com.example.gnammy

import android.content.Context
import androidx.datastore.preferences.preferencesDataStore
import androidx.room.Room
import com.example.gnammy.data.database.GnammyDatabase
import com.example.gnammy.data.remote.OSMDataSource
import com.example.gnammy.data.repositories.PlacesRepository
import com.example.gnammy.data.repositories.SettingsRepository
import com.example.gnammy.ui.PlacesViewModel
import com.example.gnammy.utils.LocationService
import io.ktor.client.HttpClient
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val Context.dataStore by preferencesDataStore("settings")

val appModule = module {
    single { get<Context>().dataStore }

    single {
        Room.databaseBuilder(
            get(),
            GnammyDatabase::class.java,
            "travel-diary"
        )
            // Sconsigliato per progetti seri! Lo usiamo solo qui per semplicità
            .fallbackToDestructiveMigration()
            .build()
    }

    single {
        HttpClient {
            install(ContentNegotiation) {
                json(Json {
                    ignoreUnknownKeys = true
                })
            }
        }
    }

    single { OSMDataSource(get()) }

    single { LocationService(get()) }

    single { SettingsRepository(get()) }

    single {
        PlacesRepository(
            get<GnammyDatabase>().placesDAO(),
            get<Context>().applicationContext.contentResolver
        )
    }

    viewModel { PlacesViewModel(get()) }
}
