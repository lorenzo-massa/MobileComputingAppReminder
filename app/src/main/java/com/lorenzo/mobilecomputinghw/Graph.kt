package com.lorenzo.mobilecomputinghw

import android.content.Context
import androidx.room.Room
import com.lorenzo.mobilecomputinghw.data.repository.CategoryRepository
import com.lorenzo.mobilecomputinghw.data.repository.PaymentRepository
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

    val paymentRepository by lazy {
        PaymentRepository(
            paymentDao = database.paymentDao()
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