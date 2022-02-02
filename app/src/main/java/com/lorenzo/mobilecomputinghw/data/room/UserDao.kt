package com.lorenzo.mobilecomputinghw.data.room

import androidx.room.*
import com.lorenzo.mobilecomputinghw.data.entity.Category
import com.lorenzo.mobilecomputinghw.data.entity.Payment
import com.lorenzo.mobilecomputinghw.data.entity.User
import kotlinx.coroutines.flow.Flow


@Dao
abstract class UserDao {

    @Query(value = "SELECT * FROM user_table")
    abstract fun getUsers(): Flow<List<User>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract suspend fun insert(entity: User): Long


}