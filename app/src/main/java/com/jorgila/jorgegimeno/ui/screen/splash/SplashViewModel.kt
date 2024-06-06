package com.jorgila.jorgegimeno.ui.screen.splash

import androidx.lifecycle.ViewModel
import com.jorgila.jorgegimeno.data.manager.AuthManager
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class SplashViewModel @Inject constructor(
    private val auth: AuthManager
) : ViewModel() {

    fun isLogged() : Boolean {
        return auth.isLogged()
    }

}