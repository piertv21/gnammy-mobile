package com.example.gnammy.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.gnammy.data.local.dao.GnamDao
import com.example.gnammy.data.local.dao.UserDao
import com.example.gnammy.data.local.entities.Gnam
import com.example.gnammy.data.local.entities.User

@Database(entities = [User::class, Gnam::class], version = 1)
abstract class GnammyDatabase : RoomDatabase() {
    abstract fun userDao(): UserDao
    abstract fun gnamDao(): GnamDao
}
