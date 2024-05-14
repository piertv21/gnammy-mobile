package com.example.gnammy.data.database

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [Place::class], version = 2)
abstract class GnammyDatabase : RoomDatabase() {
    abstract fun placesDAO(): PlacesDAO
}
