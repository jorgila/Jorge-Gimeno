package com.jorgila.jorgegimeno.data.manager

import android.app.Activity
import android.content.Context
import android.util.Log
import com.facebook.AccessToken
import com.facebook.login.LoginManager
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.auth.AuthCredential
import com.google.firebase.auth.FacebookAuthProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.OAuthProvider
import com.jorgila.jorgegimeno.R
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.CancellableContinuation
import kotlinx.coroutines.suspendCancellableCoroutine
import javax.inject.Inject
import kotlin.coroutines.resume

class AuthManager @Inject constructor(
    private val auth: FirebaseAuth,
    @ApplicationContext private val context: Context
){

    // User status

    private fun getCurrentUser() = auth.currentUser

    fun getCurrentEmail() = auth.currentUser?.email

    fun getCurrentUID() = auth.currentUser?.uid

    fun isLogged(): Boolean {
        return getCurrentUser() != null
    }

    // Email Sign Up

    suspend fun signUpWithEmail(email: String, password: String): AuthRes<FirebaseUser?> {
        return suspendCancellableCoroutine { cancellableContinuation ->
            auth.createUserWithEmailAndPassword(email, password)
                .addOnSuccessListener {
                    Log.e("AuthManager","PRUEBA")
                    val result = if (it.user != null) {
                        AuthRes.Success(it.user)
                    } else {
                        AuthRes.Error("Error al iniciar sesión")
                    }
                    cancellableContinuation.resume(result)
                }
                .addOnFailureListener {
                    val result = AuthRes.Error(it.message.toString())
                    cancellableContinuation.resume(result)
                }
        }
    }

    // Email Sign In
    suspend fun signInWithEmail(user: String, password: String): AuthRes<FirebaseUser?> {

        return suspendCancellableCoroutine { cancellableContinuation ->
            auth.signInWithEmailAndPassword(user,password)
                .addOnSuccessListener {
                    val result = if (it.user != null) {
                        AuthRes.Success(it.user)
                    } else {
                        AuthRes.Error("Error al iniciar sesión")
                    }
                    cancellableContinuation.resume(result)
                }
                .addOnFailureListener {
                    val result = AuthRes.Error(it.message.toString())
                    cancellableContinuation.resume(result)
                }
        }


    }

    // Anonymously Sign In and Sign Up
    suspend fun signInAnonymously() : AuthRes<FirebaseUser?> {
        return suspendCancellableCoroutine { cancellableContinuation ->
            auth.signInAnonymously()
                .addOnSuccessListener {
                    val result = if (it.user != null) {
                        AuthRes.Success(it.user)
                    } else {
                        AuthRes.Error("Error al iniciar sesión")
                    }
                    cancellableContinuation.resume(result)
                }
                .addOnFailureListener {
                    val result = AuthRes.Error("Error al iniciar sesión")
                    cancellableContinuation.resume(result)
                }
        }
    }

    private suspend fun completeRegisterWithCredential(credential: AuthCredential): AuthRes<FirebaseUser?> {
        return suspendCancellableCoroutine { cancellableContinuation ->
            auth.signInWithCredential(credential)
                .addOnSuccessListener {
                    val result = if (it.user != null) {
                        AuthRes.Success(it.user)
                    } else {
                        AuthRes.Error("Error al iniciar sesión")
                    }
                    cancellableContinuation.resume(result)
                }
                .addOnFailureListener {
                    val result = AuthRes.Error(it.message.toString())
                    cancellableContinuation.resume(result)
                }
        }
    }

    fun getGoogleClient(): GoogleSignInClient {
        val gso = GoogleSignInOptions
            .Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(context.getString(R.string.default_web_client_id))
            .requestEmail()
            .build()

        return GoogleSignIn.getClient(context,gso)
    }

    suspend fun signInWithGoogle(idToken: String?): AuthRes<FirebaseUser?> {
        val credential = GoogleAuthProvider.getCredential(idToken,null)
        return completeRegisterWithCredential(credential)
    }

    suspend fun signInWithFacebook(accessToken: AccessToken): AuthRes<FirebaseUser?> {
        val credential = FacebookAuthProvider.getCredential(accessToken.token)
        return completeRegisterWithCredential(credential)
    }

    private suspend fun initRegisterWithProvider(activity: Activity, provider: OAuthProvider) : AuthRes<FirebaseUser?> {
        return suspendCancellableCoroutine { cancellableContinuation ->
            auth.pendingAuthResult
                ?.addOnSuccessListener {
                    val result = if (it.user != null) {
                        AuthRes.Success(it.user)
                    } else {
                        AuthRes.Error("Error al iniciar sesión")
                    }
                    cancellableContinuation.resume(result)
                }
                ?.addOnFailureListener {
                    val result = AuthRes.Error(it.message.toString())
                    cancellableContinuation.resume(result)
                }?: completeRegisterWithProvider(activity, provider,cancellableContinuation)
        }
    }


    private fun completeRegisterWithProvider(
        activity: Activity,
        provider: OAuthProvider,
        cancellableContinuation: CancellableContinuation<AuthRes<FirebaseUser?>>
    ) {
        auth.startActivityForSignInWithProvider(activity, provider)
            .addOnSuccessListener {
                val result = if (it.user != null) {
                    AuthRes.Success(it.user)
                } else {
                    AuthRes.Error("Error al iniciar sesión")
                }
                cancellableContinuation.resume(result)
            }
            .addOnFailureListener {
                val result = AuthRes.Error(it.message.toString())
                cancellableContinuation.resume(result)
            }
    }

    suspend fun signInWithGitHub(activity: Activity): AuthRes<FirebaseUser?> {
        val provider = OAuthProvider.newBuilder("github.com").apply {
            scopes = listOf("user:email")
        }.build()
        return initRegisterWithProvider(activity,provider)
    }

    suspend fun signInWithMicrosoft(activity: Activity) : AuthRes<FirebaseUser?> {
        val provider = OAuthProvider.newBuilder("microsoft.com").apply{
            scopes = listOf("mail.read","calendars.read")
        }.build()
        return initRegisterWithProvider(activity,provider)
    }

    suspend fun signInWithTwitter(activity: Activity): AuthRes<FirebaseUser?> {
        val provider = OAuthProvider.newBuilder("twitter.com").build()
        return initRegisterWithProvider(activity,provider)
    }

    suspend fun signInWithYahoo(activity: Activity): AuthRes<FirebaseUser?> {
        val provider = OAuthProvider.newBuilder("yahoo.com").build()
        return initRegisterWithProvider(activity,provider)
    }

    // Logout

    fun logout() {
        auth.signOut()
    }

}



sealed class AuthRes<out T> {
    data class Success<T>(val data: T): AuthRes<T>()
    data class Error(val errorMessage: String): AuthRes<Nothing>()
}