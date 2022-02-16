package com.lorenzo.mobilecomputinghw.ui.profile

import androidx.compose.runtime.MutableState
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lorenzo.mobilecomputinghw.Graph
import com.lorenzo.mobilecomputinghw.data.entity.User
import com.lorenzo.mobilecomputinghw.data.repository.UserRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch


class ProfileViewModel(
    private val profileId: Long,
    private val userRepository: UserRepository = Graph.userRepository,
): ViewModel() {
    private val _state = MutableStateFlow(ProfileViewState(null))

    val state: StateFlow<ProfileViewState>
        get() = _state


    suspend fun saveUser(user: User) {
        userRepository.updateUser(user)

        //TODO We have tu update the user on the Home Screen


    }


    init {
        viewModelScope.launch {
            userRepository.getUsers().collect(){ users ->
                userRepository.getUser(profileId).apply {
                    _state.value = ProfileViewState(this, users)
                }
            }
        }
    }
}

data class ProfileViewState(
    val user: User?,
    var users: List<User> = emptyList()
){
    fun checkUsername(userName: MutableState<String>): Boolean {
        if (user != null) {
            users.forEach() {
                if (it.id != user.id && userName.value==it.userName)
                    return false
            }
        }
        return true
    }
}