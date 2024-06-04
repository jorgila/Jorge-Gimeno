package com.estholon.jorgegimeno.data.manager

import android.content.Context
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class AuthManager @Inject constructor (
    private val firebaseAuth: FirebaseAuth,
    @ApplicationContext private val context: Context
) {

    // User status

    private fun getCurrentUser() = firebaseAuth.currentUser

    fun isUserLogged(): Boolean {
        return getCurrentUser() != null
    }

}