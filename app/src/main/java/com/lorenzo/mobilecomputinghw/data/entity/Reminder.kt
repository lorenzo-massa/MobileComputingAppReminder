package com.lorenzo.mobilecomputinghw.data.entity

import androidx.annotation.NonNull
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import com.lorenzo.mobilecomputinghw.data.entity.Category

//Message, location_x, location_y, reminder_time, creation_time, creator_id, reminder_seen

@Entity(
    tableName = "reminders",
    /*indices = [
        Index("id", unique = true),
        Index("payment_category_id")
    ],
    foreignKeys = [
        ForeignKey(
            entity = Category::class,
            parentColumns = ["id"],
            childColumns = ["payment_category_id"],
            onUpdate = ForeignKey.CASCADE,
            onDelete = ForeignKey.CASCADE
        )
    ]*/
)

data class Reminder(
    @PrimaryKey(autoGenerate = true) @ColumnInfo(name = "id") val reminderId: Long = 0,
    @ColumnInfo(name = "message") val message: String,
    @ColumnInfo(name = "location_x") val location_x: String,
    @ColumnInfo(name = "location_y") val location_y: String,
    @ColumnInfo(name = "reminder_time") val reminder_time: String,
    @ColumnInfo(name = "creation_time") val creation_time: String,
    @ColumnInfo(name = "creator_id") val creator_id: Long,
    @ColumnInfo(name = "reminder_seen") val reminder_seen: Long,
    @ColumnInfo(name = "img_uri") val img_uri: String
)
