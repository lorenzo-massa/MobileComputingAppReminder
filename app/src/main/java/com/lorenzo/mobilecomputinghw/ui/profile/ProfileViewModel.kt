package com.lorenzo.mobilecomputinghw.ui.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.lorenzo.mobilecomputinghw.Graph
import com.lorenzo.mobilecomputinghw.data.entity.Reminder
import com.lorenzo.mobilecomputinghw.data.entity.User
import com.lorenzo.mobilecomputinghw.data.repository.ReminderRepository
import com.lorenzo.mobilecomputinghw.data.repository.UserRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch


class ProfileViewModel(
    private val userRepository: UserRepository = Graph.userRepository,
): ViewModel() {
    private val _state = MutableStateFlow(ProfileViewState())
    private var _username: String = ""

    val state: StateFlow<ProfileViewState>
        get() = _state


    suspend fun saveUser(user: User) {
        userRepository.updateUser(user)
    }

    fun setUser(userName: String) {
        _username = userName
    }

    init {
        viewModelScope.launch {
            //_state.value = ProfileViewState(userRepository.getUser(username))
        }
    }
}

data class ProfileViewState(
    val user: User? = null
)