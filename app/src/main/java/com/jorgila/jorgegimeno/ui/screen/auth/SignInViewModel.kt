package com.jorgila.jorgegimeno.ui.screen.auth

import android.app.Activity
import android.util.Patterns
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.facebook.AccessToken
import com.google.android.gms.auth.api.signin.GoogleSignInClient
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
class SignInViewModel @Inject constructor(
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

    //// Email Sign In

    fun signInEmail(
        email: String,
        password: String,
        navigateToHome: () -> Unit,
        communicateError: (String) -> Unit
    ) {
        viewModelScope.launch {
            isLoading = true

            val signIn = auth.signInWithEmail(email,password)

            when(val result = withContext(Dispatchers.IO){
                signIn
            }) {
                is AuthRes.Success -> {
                    navigateToHome()

                    val analyticModel = AnalyticModel(
                        title = "Sign In", analyticsString = listOf(Pair("Email", "Successful Sign In"))
                    )
                    analytics.sendEvent(analyticModel)
                }
                is AuthRes.Error -> {

                    signIn.let {
                        val string = it.toString().substringAfter("errorMessage=")
                        message = string.substring( 0 , string.length - 1 )
                    }

                    communicateError(message)
                    val analyticModel = AnalyticModel(
                        title = "Sign In", analyticsString = listOf(Pair("Email", "Failed Sign In: ${result.errorMessage}"))
                    )
                    analytics.sendEvent(analyticModel)
                }
            }
            isLoading = false
        }
    }

    // Anonymously Sign In

    fun signinAnonymously(navigateToHome: () -> Unit, communicateError: () -> Unit) {
        viewModelScope.launch {
            isLoading = true
            val signIn = auth.signInAnonymously()
            when(val result = withContext(Dispatchers.IO){
                signIn
            }) {
                is AuthRes.Success -> {
                    navigateToHome()

                    val analyticModel = AnalyticModel(
                        title = "Sign In", analyticsString = listOf(Pair("Anonymously", "Successful login"))
                    )
                    analytics.sendEvent(analyticModel)


                }
                is AuthRes.Error -> {
                    signIn.let {
                        val string = it.toString().substringAfter("errorMessage=")
                        message = string.substring( 0 , string.length - 1 )
                    }
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

    fun onGoogleSignInSelected(googleLauncherSignIn:(GoogleSignInClient)->Unit) {
        val gsc = auth.getGoogleClient()
        googleLauncherSignIn(gsc)
    }

    fun signInWithGoogle(idToken: String?, navigateToHome: () -> Unit, communicateError: (String) -> Unit) {
        viewModelScope.launch {

            isLoading = true

            val signIn = auth.signInWithGoogle(idToken)

            when(val result = withContext(Dispatchers.IO){
                signIn
            }) {
                is AuthRes.Success -> {
                    navigateToHome()

                    val analyticModel = AnalyticModel(
                        title = "Sign In", analyticsString = listOf(Pair("Email", "Successful Sign In"))
                    )
                    analytics.sendEvent(analyticModel)
                }
                is AuthRes.Error -> {

                    signIn.let {
                        val string = it.toString().substringAfter("errorMessage=")
                        message = string.substring( 0 , string.length - 1 )
                    }

                    communicateError(message)
                    val analyticModel = AnalyticModel(
                        title = "Sign In", analyticsString = listOf(Pair("Email", "Failed Sign In: ${result.errorMessage}"))
                    )
                    analytics.sendEvent(analyticModel)
                }
            }
            isLoading = false

        }
    }

    fun signInWithFacebook(accessToken: AccessToken, navigateToHome: () -> Unit, communicateError: () -> Unit) {
        viewModelScope.launch {

            isLoading = true

            val signIn = auth.signInWithFacebook(accessToken)
            when(val result = withContext(Dispatchers.IO){
                signIn
            }) {
                is AuthRes.Success -> {
                    navigateToHome()
                    val analyticModel = AnalyticModel(
                        title = "Sign In", analyticsString = listOf(Pair("Facebook", "Successful Sign In"))
                    )
                    analytics.sendEvent(analyticModel)
                }
                is AuthRes.Error -> {
                    signIn.let {
                        val string = it.toString().substringAfter("errorMessage=")
                        message = string.substring( 0 , string.length - 1 )
                    }
                    communicateError()
                    val analyticModel = AnalyticModel(
                        title = "Sign In", analyticsString = listOf(Pair("Facebook", "Failed Sign In: ${result.errorMessage}"))
                    )
                    analytics.sendEvent(analyticModel)
                }
            }

            isLoading = false

        }
    }

    fun onOathLoginSelected(
        oath: OathLogin,
        activity: Activity,
        navigateToHome: () -> Unit,
        communicateError: () -> Unit
    )
    {

        viewModelScope.launch {

            isLoading = true

            val signIn = when (oath) {
                OathLogin.GitHub -> auth.signInWithGitHub(activity)
                OathLogin.Microsoft -> auth.signInWithMicrosoft(activity)
                OathLogin.Twitter -> auth.signInWithTwitter(activity)
                OathLogin.Yahoo -> auth.signInWithYahoo(activity)
            }


            when(val result = withContext(Dispatchers.IO){
                signIn
            }) {
                is AuthRes.Success -> {
                    navigateToHome()

                    val analyticModel = AnalyticModel(
                        title = "Sign In", analyticsString = listOf(Pair("$oath", "Successful Sign In"))
                    )
                    analytics.sendEvent(analyticModel)
                }
                is AuthRes.Error -> {
                    signIn.let {
                        val string = it.toString().substringAfter("errorMessage=")
                        message = string.substring( 0 , string.length - 1 )
                    }
                    communicateError()
                    val analyticModel = AnalyticModel(
                        title = "Sign In", analyticsString = listOf(Pair("$oath", "Failed Sign In: ${result.errorMessage}"))
                    )
                    analytics.sendEvent(analyticModel)
                }
            }

            isLoading = false

        }

    }
}

sealed class OathLogin() {
    object GitHub:OathLogin()
    object Microsoft:OathLogin()
    object Twitter:OathLogin()
    object Yahoo: OathLogin()
}