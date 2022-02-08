package com.lorenzo.mobilecomputinghw.ui.editReminder

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lorenzo.mobilecomputinghw.Graph
import com.lorenzo.mobilecomputinghw.data.entity.Reminder
import com.lorenzo.mobilecomputinghw.data.repository.ReminderRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class EditReminderViewModel(
    private val reminderId: Long,
    private val reminderRepository: ReminderRepository = Graph.reminderRepository,
): ViewModel() {
    private val _state = MutableStateFlow(EditReminderViewState(null))

    val state: StateFlow<EditReminderViewState>
        get() = _state

    suspend fun saveReminder(reminder: Reminder) {
        return reminderRepository.updateReminder(reminder)
    }

    init {
        viewModelScope.launch {
            reminderRepository.getReminder(reminderId).apply {
                _state.value = EditReminderViewState(this)
            }
        }
    }
}

data class EditReminderViewState(
    val reminder: Reminder?
)