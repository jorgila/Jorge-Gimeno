package com.estholon.jorgegimeno.ui.screens.auth

import android.util.Patterns
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class SignInViewModel @Inject constructor(
): ViewModel() {

    // Progress Indicator Variable
    var isLoading: Boolean by mutableStateOf(false)

    // Error message
    var message : String by mutableStateOf("")

    // Check to see if text is an email

    fun isEmail(user: String) : Boolean {
        return Patterns.EMAIL_ADDRESS.matcher(user).matches()
    }

}