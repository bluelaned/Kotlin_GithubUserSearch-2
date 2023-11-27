package com.bluelaned.submission2.viewmodel

import android.app.Application
import androidx.lifecycle.ViewModel
import com.bluelaned.submission2.database.UserRepository

class FavoriteUserViewModel(application: Application) : ViewModel() {
    private val userRepository: UserRepository = UserRepository(application)

    fun getAllFavoriteUsers() = userRepository.getAllFavoriteUsers()

    init {
        getAllFavoriteUsers()
    }
}