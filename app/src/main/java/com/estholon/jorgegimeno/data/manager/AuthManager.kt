package com.estholon.jorgegimeno.data.manager

import android.content.Context
import android.util.Log
import com.estholon.jorgegimeno.R
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.auth.AuthCredential
import com.google.firebase.auth.EmailAuthProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.suspendCancellableCoroutine
import javax.inject.Inject
import kotlin.coroutines.resume

sealed class AuthRes<out T> {
    data class Success<T>(val data: T): AuthRes<T>()
    data class Error(val errorMessage: String): AuthRes<Nothing>()
}
class AuthManager @Inject constructor (
    private val firebaseAuth: FirebaseAuth,
    @ApplicationContext private val context: Context
) {

    // User status

    private fun getCurrentUser() = firebaseAuth.currentUser

    fun getCurrentEmail() = firebaseAuth.currentUser?.email

    fun getCurrentUID() = firebaseAuth.currentUser?.uid

    fun isUserLogged(): Boolean {
        return getCurrentUser() != null
    }

    // Logout

    fun logout() {
        firebaseAuth.signOut()
        getGoogleClient().signOut()
    }

    // Anonymously Sign In and Sign Up
    suspend fun signInAnonymously(): AuthRes<FirebaseUser?> {
        return suspendCancellableCoroutine { cancellableContinuation ->
            firebaseAuth.signInAnonymously()
                .addOnSuccessListener {
                    val result = if (it.user != null) {
                        AuthRes.Success(it.user)
                    } else {
                        AuthRes.Error("Error al iniciar sesión")
                    }
                    cancellableContinuation.resume(result)
                }
                .addOnFailureListener {
                    val result = AuthRes.Error("Error al iniciar sesión: $it")
                    cancellableContinuation.resume(result)
                }
        }
    }

    // Email Sign Up

    suspend fun signUpWithEmail(email: String, password: String): AuthRes<FirebaseUser?> {
        return suspendCancellableCoroutine { cancellableContinuation ->
            firebaseAuth.createUserWithEmailAndPassword(email,password)
                .addOnSuccessListener {
                    val result = if (it.user != null) {
                        AuthRes.Success(it.user)
                    } else {
                        AuthRes.Error("Error al iniciar sesión")
                    }
                    cancellableContinuation.resume(result)
                }
                .addOnFailureListener {
                    val result = AuthRes.Error("Error al iniciar sesión: $it")
                    cancellableContinuation.resume(result)
                }
        }
    }

    // Email Recover

    suspend fun resetPassword(email: String): AuthRes<Unit> {
        return suspendCancellableCoroutine {cancellableContinuation ->
            firebaseAuth.sendPasswordResetEmail(email)
                .addOnSuccessListener {
                    val result = AuthRes.Success(Unit)
                    cancellableContinuation.resume(result)
                }
                .addOnFailureListener{
                    val result = AuthRes.Error(it.message ?: "Error al restablecer la contraseña")
                    cancellableContinuation.resume(result)
                }
        }
    }

    // Email Sign In
    suspend fun signInWithEmail(user: String, password: String): AuthRes<FirebaseUser?> {

        return suspendCancellableCoroutine { cancellableContinuation ->
            firebaseAuth.signInWithEmailAndPassword(user,password)
                .addOnSuccessListener {
                    val result = if (it.user != null) {
                        AuthRes.Success(it.user)
                    } else {
                        AuthRes.Error("Error al iniciar sesión")
                    }
                    cancellableContinuation.resume(result)
                }
                .addOnFailureListener {
                    val result = AuthRes.Error("Error al iniciar sesión: $it")
                    cancellableContinuation.resume(result)
                }
        }
    }

    private suspend fun completeRegisterWithCredential(credential: AuthCredential):AuthRes<FirebaseUser?> {
        return suspendCancellableCoroutine { cancellableContinuation ->

            firebaseAuth.signInWithCredential(credential)
                .addOnSuccessListener {
                    val result = if (it.user != null) {
                        AuthRes.Success(it.user)
                    } else {
                        AuthRes.Error("Error al iniciar sesión")
                    }
                    cancellableContinuation.resume(result)
                }
                .addOnFailureListener {
                    val result = AuthRes.Error("Error al iniciar sesión: $it")
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

    // LINK WITH CREDENTIAL

    suspend fun linkWithCredential(user: String, password: String) : AuthRes<FirebaseUser?>{

        val credential = EmailAuthProvider.getCredential(user, password)

        return suspendCancellableCoroutine { cancellableContinuation ->

            firebaseAuth.getCurrentUser()!!.linkWithCredential(credential)
                .addOnSuccessListener {
                    val result = if (it.user != null) {
                        AuthRes.Success(it.user)
                    } else {
                        AuthRes.Error("Error al vincular cuenta")
                    }
                    cancellableContinuation.resume(result)
                }
                .addOnFailureListener {
                    val result = AuthRes.Error("Error al vincular cuenta: $it")
                    cancellableContinuation.resume(result)
                }
        }

    }

    suspend fun deleteAccount(user: FirebaseUser?) : AuthRes<String> {

        return suspendCancellableCoroutine { cancellableContinuation ->
            if (user != null) {
                user.delete()
                    .addOnSuccessListener {
                        val result = AuthRes.Success("Eliminación completa")
                        cancellableContinuation.resume(result)
                    }
                    .addOnFailureListener {
                        val result = AuthRes.Error("Error al eliminar cuenta: $it")
                        cancellableContinuation.resume(result)
                    }

            }
        }
    }

}

