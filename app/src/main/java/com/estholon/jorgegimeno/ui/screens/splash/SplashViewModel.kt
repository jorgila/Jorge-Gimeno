package com.estholon.jorgegimeno.ui.screens.splash

import androidx.lifecycle.ViewModel
import com.estholon.jorgegimeno.data.manager.AuthManager
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class SplashViewModel @Inject constructor(
    val auth: AuthManager
): ViewModel() {
    fun isUserLogged():Boolean{
        return auth.isUserLogged()
    }

}