package com.lorenzo.mobilecomputinghw

import android.content.Context
import androidx.room.Room
import com.lorenzo.mobilecomputinghw.data.repository.CategoryRepository
import com.lorenzo.mobilecomputinghw.data.repository.ReminderRepository
import com.lorenzo.mobilecomputinghw.data.repository.UserRepository
import com.lorenzo.mobilecomputinghw.data.room.MobileComputingDatabase

/**
 * A simple singleton dependency graph
 *
 * For a real app, please use something like Koin/Dagger/Hilt instead
 */
object Graph {
    lateinit var database: MobileComputingDatabase

    val categoryRepository by lazy {
        CategoryRepository(
            categoryDao = database.categoryDao()
        )
    }

    val reminderRepository by lazy {
        ReminderRepository(
            reminderDao = database.reminderDao()
        )
    }

    val userRepository by lazy {
        UserRepository(
            userDao = database.userDao()
        )
    }

    fun provide(context: Context) {
        database = Room.databaseBuilder(context, MobileComputingDatabase::class.java, "mcData.db")
            .fallbackToDestructiveMigration() // don't use this in production app
            .build()
    }
}