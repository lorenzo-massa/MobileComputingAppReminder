package com.lorenzo.mobilecomputinghw.data.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "user_table")


data class User(
    @PrimaryKey @ColumnInfo(name = "userName") val userName: String,
    @ColumnInfo(name = "password") val password: String
)
