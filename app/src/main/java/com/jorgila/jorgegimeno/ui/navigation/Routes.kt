package com.jorgila.jorgegimeno.ui.navigation

sealed class Routes (val route: String) {

    object SplashScreen: Routes("splash")
    object SignInScreen: Routes("signIn")
    object SignUpScreen: Routes("signUp")
    object RecoverScreen: Routes("recover")
    object MainScreen: Routes("main")

}