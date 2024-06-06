package com.jorgila.jorgegimeno.ui.screen.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jorgila.jorgegimeno.data.manager.AuthManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val auth: AuthManager
): ViewModel() {
    // Logout

    fun logout(navigateToLogin:()-> Unit) {
        viewModelScope.launch(Dispatchers.IO) {
            auth.logout()
        }
        navigateToLogin()
    }

}