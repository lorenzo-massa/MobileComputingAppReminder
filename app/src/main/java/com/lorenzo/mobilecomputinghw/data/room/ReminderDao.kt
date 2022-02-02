package com.lorenzo.mobilecomputinghw.data.room

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.lorenzo.mobilecomputinghw.data.entity.Reminder
import kotlinx.coroutines.flow.Flow

@Dao
abstract class ReminderDao {
    @Query("""
        SELECT payments.* FROM payments
        INNER JOIN categories ON payments.payment_category_id = categories.id
        WHERE payment_category_id = :categoryId
    """)
    abstract fun paymentsFromCategory(categoryId: Long): Flow<List<PaymentToCategory>>

    @Query("""SELECT * FROM payments WHERE id = :paymentId""")
    abstract fun reminder(paymentId: Long): Reminder?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract suspend fun insert(entity: Reminder): Long

    @Update(onConflict = OnConflictStrategy.REPLACE)
    abstract suspend fun update(entity: Reminder)

    @Delete
    abstract suspend fun delete(entity: Reminder): Int
}