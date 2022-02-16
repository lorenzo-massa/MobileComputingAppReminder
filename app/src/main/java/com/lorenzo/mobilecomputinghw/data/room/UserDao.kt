package com.lorenzo.mobilecomputinghw.data.room

import androidx.room.*
import com.lorenzo.mobilecomputinghw.data.entity.User
import kotlinx.coroutines.flow.Flow


@Dao
abstract class UserDao {

    @Query(value = "SELECT * FROM user_table")
    abstract fun getUsers(): Flow<List<User>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract suspend fun insert(entity: User): Long

    @Update(onConflict = OnConflictStrategy.REPLACE)
    abstract suspend fun update(entity: User)

    @Query(value = "SELECT * FROM user_table WHERE id=:id")
    abstract suspend fun getUser(id: Long): User

}