package com.example.gnammy.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.gnammy.data.local.dao.GnamDao
import com.example.gnammy.data.local.dao.GnamGoalDao
import com.example.gnammy.data.local.dao.LikedGnamDao
import com.example.gnammy.data.local.dao.NotificationDao
import com.example.gnammy.data.local.dao.UserDao
import com.example.gnammy.data.local.dao.UserGoalDao
import com.example.gnammy.data.local.entities.Gnam
import com.example.gnammy.data.local.entities.GnamGoal
import com.example.gnammy.data.local.entities.LikedGnam
import com.example.gnammy.data.local.entities.Notification
import com.example.gnammy.data.local.entities.User
import com.example.gnammy.data.local.entities.UserGoal

@Database(
    entities = [
        User::class,
        Gnam::class,
        LikedGnam::class,
        Notification::class,
        GnamGoal::class,
        UserGoal::class
    ],
    version = 2
)
abstract class GnammyDatabase : RoomDatabase() {
    abstract fun userDao(): UserDao
    abstract fun gnamDao(): GnamDao
    abstract fun likedGnamDao(): LikedGnamDao
    abstract fun notificationDao(): NotificationDao
    abstract fun userGoalDao(): UserGoalDao
    abstract fun gnamGoalDao(): GnamGoalDao
}
