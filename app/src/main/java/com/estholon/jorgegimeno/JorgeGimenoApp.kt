package com.estholon.jorgegimeno


import android.app.Application
import com.google.firebase.FirebaseApp
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class JorgeGimenoApp : Application() {
    override fun onCreate() {
        super.onCreate()

        // Initialize Firebase when activity is created
        FirebaseApp.initializeApp(this)

    }
}