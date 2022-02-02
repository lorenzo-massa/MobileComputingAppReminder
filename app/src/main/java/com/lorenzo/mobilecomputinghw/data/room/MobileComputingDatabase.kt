package com.lorenzo.mobilecomputinghw.data.room

import androidx.room.Database
import androidx.room.RoomDatabase
import com.lorenzo.mobilecomputinghw.data.entity.Category
import com.lorenzo.mobilecomputinghw.data.entity.Reminder
import com.lorenzo.mobilecomputinghw.data.entity.User

/**
 * The [RoomDatabase] for this app
 */
@Database(
    entities = [Category::class, Reminder::class, User::class],
    version = 2,
    exportSchema = false
)
abstract class MobileComputingDatabase : RoomDatabase() {
    //abstract fun categoryDao(): CategoryDao
    abstract fun reminderDao(): ReminderDao
    abstract fun userDao(): UserDao
}