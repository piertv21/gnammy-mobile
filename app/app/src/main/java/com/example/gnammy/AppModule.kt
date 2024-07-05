package com.example.gnammy

import android.content.Context
import androidx.datastore.preferences.preferencesDataStore
import androidx.room.Room
import com.example.gnammy.data.local.GnammyDatabase
import com.example.gnammy.data.repository.GnamRepository
import com.example.gnammy.data.repository.LikedGnamRepository
import com.example.gnammy.data.repository.UserRepository
import com.example.gnammy.ui.viewmodels.GnamViewModel
import com.example.gnammy.ui.viewmodels.UserViewModel
import com.example.gnammy.utils.LocationService
import io.ktor.client.HttpClient
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val Context.dataStore by preferencesDataStore("settings")

val backendSocket = "http://172.23.176.1:3000"
//val backendSocket = "http://192.168.1.175:3000"

val appModule = module {
    single { get<Context>().dataStore }

    single {
        Room.databaseBuilder(
            get(),
            GnammyDatabase::class.java,
            "gnammy-db"
        )
            // TODO Sconsigliato per progetti seri! Lo usiamo solo qui per semplicità
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

    single { LocationService(get()) }

    single {
        UserRepository(
            get<GnammyDatabase>().userDao(),
            get<Context>().applicationContext.contentResolver, // TODO questo serve per salvare le immagini in storage, capiremo se serve o meno.
            get()
        )
    }

    single {
        GnamRepository(
            get<GnammyDatabase>().gnamDao(),
            get<GnammyDatabase>().likedGnamDao(),
            get<Context>().applicationContext.contentResolver, // TODO questo serve per salvare le immagini in storage, capiremo se serve o meno.
            get()
        )
    }

    single {
        LikedGnamRepository(
            get<GnammyDatabase>().gnamDao(),
            get<GnammyDatabase>().likedGnamDao(),
            get<Context>().applicationContext.contentResolver // TODO questo serve per salvare le immagini in storage, capiremo se serve o meno.
        )
    }

    viewModel { UserViewModel(get()) }

    viewModel { GnamViewModel(get(), get()) }
}
