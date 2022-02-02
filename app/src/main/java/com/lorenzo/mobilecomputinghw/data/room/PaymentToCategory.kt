package com.lorenzo.mobilecomputinghw.data.room

import androidx.room.Embedded
import androidx.room.Ignore
import androidx.room.Relation
import com.lorenzo.mobilecomputinghw.data.entity.Category
import com.lorenzo.mobilecomputinghw.data.entity.Reminder
import java.util.*

class PaymentToCategory {
    @Embedded
    lateinit var reminder: Reminder

    @Relation(parentColumn = "payment_category_id", entityColumn = "id")
    lateinit var _categories: List<Category>

    @get:Ignore
    val category: Category
        get() = _categories[0]

    /**
     * Allow this class to be destructured by consumers
     */
    operator fun component1() = reminder
    operator fun component2() = category

    override fun equals(other: Any?): Boolean = when {
        other === this -> true
        other is PaymentToCategory -> reminder == other.reminder && _categories == other._categories
        else -> false
    }

    override fun hashCode(): Int = Objects.hash(reminder, _categories)
}