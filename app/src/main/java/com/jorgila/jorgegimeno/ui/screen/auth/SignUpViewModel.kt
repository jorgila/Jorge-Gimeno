package com.jorgila.jorgegimeno.ui.screen.auth

import android.util.Patterns
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jorgila.jorgegimeno.data.manager.AnalyticsManager
import com.jorgila.jorgegimeno.data.manager.AuthManager
import com.jorgila.jorgegimeno.data.manager.AuthRes
import com.jorgila.jorgegimeno.data.model.AnalyticModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class SignUpViewModel @Inject constructor(
    private val auth: AuthManager,
    private val analytics: AnalyticsManager
) : ViewModel() {

    // VARIABLES
    var isLoading: Boolean by mutableStateOf(false)
    var message: String by mutableStateOf("")

    // FUNCTIONS
    //// Check to see if text is an email

    fun isEmail(user: String): Boolean {
        return Patterns.EMAIL_ADDRESS.matcher(user).matches()
    }

    // Sign Up with email

    fun signUpEmail(
        email: String,
        password: String,
        navigateToHome: () -> Unit,
        communicateError: () -> Unit
    ) {

        viewModelScope.launch {
            isLoading = true

            val signUp = auth.signUpWithEmail(email,password)
            when (withContext(Dispatchers.IO) {
                signUp
            }) {
                is AuthRes.Success -> {
                    navigateToHome()

                    val analyticModel = AnalyticModel(
                        title = "Sign Up",
                        analyticsString = listOf(Pair("Email", "Successful Sign Up"))
                    )
                    analytics.sendEvent(analyticModel)


                }

                is AuthRes.Error -> {
                    signUp.let {
                        val string = it.toString().substringAfter("errorMessage=")
                        message = string.substring( 0 , string.length - 1 )
                    }
                    communicateError()
                    val analyticModel = AnalyticModel(
                        title = "Sign Up", analyticsString = listOf(Pair("Email", "Failed Sign Up"))
                    )
                    analytics.sendEvent(analyticModel)
                }
            }

            isLoading = false
        }

    }


}