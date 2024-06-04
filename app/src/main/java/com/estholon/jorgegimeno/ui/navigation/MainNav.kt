package com.estholon.jorgegimeno.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.estholon.jorgegimeno.ui.navigation.Routes.*
import com.estholon.jorgegimeno.ui.screens.auth.RecoverScreen
import com.estholon.jorgegimeno.ui.screens.auth.SignInScreen
import com.estholon.jorgegimeno.ui.screens.auth.SignUpScreen
import com.estholon.jorgegimeno.ui.screens.main.MainScreen
import com.estholon.jorgegimeno.ui.screens.splash.SplashScreen

@Composable
fun MainNav(){

    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = SplashScreen.route
    ) {
        composable(SplashScreen.route){
            SplashScreen(navController = navController)
        }
        composable(SignInScreen.route){
            SignInScreen(navController = navController)
        }
        composable(SignUpScreen.route){
            SignUpScreen(navController = navController)
        }
        composable(RecoverScreen.route){
            RecoverScreen(navController = navController)
        }
        composable(MainScreen.route){
            MainScreen(navController = navController)
        }

    }
}