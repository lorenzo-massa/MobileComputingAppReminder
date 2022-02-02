package com.lorenzo.mobilecomputinghw.ui.home.categoryPayment

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lorenzo.mobilecomputinghw.Graph
import com.lorenzo.mobilecomputinghw.data.entity.Reminder
import com.lorenzo.mobilecomputinghw.data.repository.ReminderRepository
import com.lorenzo.mobilecomputinghw.data.room.PaymentToCategory
import com.lorenzo.mobilecomputinghw.ui.home.HomeViewState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch


class CategoryReminderViewModel(
    private val categoryId: Long,
    private val reminderRepository: ReminderRepository = Graph.reminderRepository
) : ViewModel() {
    private val _state = MutableStateFlow(CategoryReminderViewState())

    val state: StateFlow<CategoryReminderViewState>
        get() = _state

    init {
        viewModelScope.launch {
            Graph.reminderRepository.reminders().collect { reminders ->
                _state.value = CategoryReminderViewState(reminders)
            }
        }
    }
}

data class CategoryReminderViewState(
    val reminders: List<Reminder> = emptyList()
)