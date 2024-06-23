package com.example.gnammy.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.gnammy.data.local.dao.UserDao
import com.example.gnammy.data.local.entities.User

@Database(entities = [User::class], version = 1)
abstract class GnammyDatabase : RoomDatabase() {
    abstract fun userDao(): UserDao


    // abstract fun followingDao(): FollowingDao
    // abstract fun gnamDao(): GnamDao
    // abstract fun likeDao(): LikeDao
    // abstract fun notificationDao(): NotificationDao
    // abstract fun notificationTypeDao(): NotificationTypeDao
    // abstract fun goalDao(): GoalDao
    // abstract fun goalTypeDao(): GoalTypeDao
}
