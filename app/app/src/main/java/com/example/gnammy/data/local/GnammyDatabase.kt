package com.example.gnammy.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.gnammy.data.local.dao.GnamDao
import com.example.gnammy.data.local.dao.LikedGnamDao
import com.example.gnammy.data.local.dao.NotificationDao
import com.example.gnammy.data.local.dao.UserDao
import com.example.gnammy.data.local.entities.Gnam
import com.example.gnammy.data.local.entities.LikedGnam
import com.example.gnammy.data.local.entities.Notification
import com.example.gnammy.data.local.entities.User

@Database(entities = [User::class, Gnam::class, LikedGnam::class, Notification::class], version = 2)
abstract class GnammyDatabase : RoomDatabase() {
    abstract fun userDao(): UserDao
    abstract fun gnamDao(): GnamDao
    abstract fun likedGnamDao(): LikedGnamDao
    abstract fun notificationDao(): NotificationDao
}
