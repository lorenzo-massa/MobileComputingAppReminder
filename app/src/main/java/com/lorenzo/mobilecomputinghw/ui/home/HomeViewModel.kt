package com.lorenzo.mobilecomputinghw.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lorenzo.mobilecomputinghw.Graph.reminderRepository
import com.lorenzo.mobilecomputinghw.Graph.userRepository
import com.lorenzo.mobilecomputinghw.data.entity.Reminder
import com.lorenzo.mobilecomputinghw.data.entity.User
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class HomeViewModel(
    //private val categoryRepository: CategoryRepository = Graph.categoryRepository
) : ViewModel() {
    private val _state = MutableStateFlow(HomeViewState())
    //private val _selectedCategory = MutableStateFlow<Category?>(null)

    val state: StateFlow<HomeViewState>
        get() = _state

    /*fun onCategorySelected(category: Category) {
        _selectedCategory.value = category
    }*/

    suspend fun updateUsers(){
        userRepository.getUsers().collect() { users ->
            _state.value.users = users

        }
    }

    init {
        viewModelScope.launch {
            userRepository.getUsers().collect(){ users ->
                reminderRepository.reminders().collect { reminders ->
                    _state.value = HomeViewState(reminders,users)
                }
            }

        }



        //loadCategoriesFromDb()
    }
    /*
    private fun loadCategoriesFromDb() {
        val list = mutableListOf(
            Category(name = "Food"),
            Category(name = "Health"),
            Category(name = "Savings"),
            Category(name = "Drinks"),
            Category(name = "Clothing"),
            Category(name = "Investment"),
            Category(name = "Travel"),
            Category(name = "Fuel"),
            Category(name = "Repairs"),
            Category(name = "Coffee")
        )
        viewModelScope.launch {
            list.forEach { category -> categoryRepository.addCategory(category) }
        }
    }*/
}

data class HomeViewState(
    //val categories: List<Category> = emptyList(),
    //val selectedCategory: Category? = null
    var reminders: List<Reminder> = emptyList(),
    var users: List<User> = emptyList()
)