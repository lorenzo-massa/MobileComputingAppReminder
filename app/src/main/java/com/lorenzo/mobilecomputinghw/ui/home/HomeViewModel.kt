package com.lorenzo.mobilecomputinghw.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lorenzo.mobilecomputinghw.Graph
import com.lorenzo.mobilecomputinghw.Graph.reminderRepository
import com.lorenzo.mobilecomputinghw.Graph.userRepository
import com.lorenzo.mobilecomputinghw.data.entity.Reminder
import com.lorenzo.mobilecomputinghw.data.entity.User
import com.lorenzo.mobilecomputinghw.data.repository.ReminderRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class HomeViewModel(
    private val idLogged: Long,
    private val reminderRepository: ReminderRepository = Graph.reminderRepository,
) : ViewModel() {
    private val _state = MutableStateFlow(HomeViewState(null))

    val state: StateFlow<HomeViewState>
        get() = _state

    suspend fun deleteReminder(id :Long){
        reminderRepository.removeReminderById(id)
    }

    fun updateUser(){
        viewModelScope.launch {
            _state.value.updateUser()
        }
    }

    init {
        viewModelScope.launch {
                reminderRepository.reminders().collect { reminders ->
                    userRepository.getUser(idLogged).apply {
                        _state.value = HomeViewState(this, reminders)
                    }
                }

        }
    }
}

data class HomeViewState(
    //val categories: List<Category> = emptyList(),
    //val selectedCategory: Category? = null
    var user: User?,
    var reminders: List<Reminder> = emptyList()
){
    suspend fun updateUser(){
        userRepository.getUser(user?.id ?: 0).apply {
            user = this
        }
    }
}