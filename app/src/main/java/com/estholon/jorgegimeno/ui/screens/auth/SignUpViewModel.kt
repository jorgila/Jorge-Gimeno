package com.estholon.jorgegimeno.ui.screens.auth

import android.util.Patterns
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.estholon.jorgegimeno.data.manager.AnalyticsManager
import com.estholon.jorgegimeno.data.manager.AuthManager
import com.estholon.jorgegimeno.data.manager.AuthRes
import com.estholon.jorgegimeno.data.model.AnalyticModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class SignUpViewModel @Inject constructor(
    private val auth: AuthManager,
    private val analytics: AnalyticsManager
) : ViewModel(

) {
    // Progress Indicator Variable
    var isLoading: Boolean by mutableStateOf(false)

    // Error message
    var message : String by mutableStateOf("")

    // Check to see if text is an email

    fun isEmail(user: String) : Boolean {
        return Patterns.EMAIL_ADDRESS.matcher(user).matches()
    }

    // Anonymously Sign In

    fun signInAnonymously(navigateToHome: () -> Unit, communicateError: () -> Unit) {
        viewModelScope.launch {
            isLoading = true

            when(val result = withContext(Dispatchers.IO){
                auth.signInAnonymously()
            }) {
                is AuthRes.Success -> {
                    navigateToHome()

                    val analyticModel = AnalyticModel(
                        title = "Sign In", analyticsString = listOf(Pair("Anonymously", "Successful login"))
                    )
                    analytics.sendEvent(analyticModel)
                }
                is AuthRes.Error -> {
                    communicateError()
                    val analyticModel = AnalyticModel(
                        title = "Sign In", analyticsString = listOf(Pair("Anonymously", "Failed login: ${result.errorMessage}"))
                    )
                    analytics.sendEvent(analyticModel)
                }
            }
            isLoading = false
        }
    }

}