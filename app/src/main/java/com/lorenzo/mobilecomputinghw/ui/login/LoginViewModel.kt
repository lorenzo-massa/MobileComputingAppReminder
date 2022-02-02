package com.lorenzo.mobilecomputinghw.ui.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lorenzo.mobilecomputinghw.Graph
import com.lorenzo.mobilecomputinghw.data.entity.Category
import com.lorenzo.mobilecomputinghw.data.entity.Payment
import com.lorenzo.mobilecomputinghw.data.entity.User
import com.lorenzo.mobilecomputinghw.data.repository.CategoryRepository
import com.lorenzo.mobilecomputinghw.data.repository.PaymentRepository
import com.lorenzo.mobilecomputinghw.data.repository.UserRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class LoginViewModel(
    private val userRepository: UserRepository = Graph.userRepository,
): ViewModel() {
    private val _state = MutableStateFlow(LoginViewState())

    val state: StateFlow<LoginViewState>
        get() = _state


    suspend fun saveUser(user: User): Long {
        return userRepository.addUser(user)
    }

    init {
        viewModelScope.launch {
            userRepository.getUsers().collect { users ->
                _state.value = LoginViewState(users)
            }
        }
    }
}

data class LoginViewState(
    val users: List<User> = emptyList()
)