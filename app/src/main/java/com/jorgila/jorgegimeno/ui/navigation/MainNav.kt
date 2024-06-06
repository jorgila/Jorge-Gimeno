package com.jorgila.jorgegimeno.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.jorgila.jorgegimeno.ui.navigation.Routes.*
import com.jorgila.jorgegimeno.ui.screen.auth.RecoverScreen
import com.jorgila.jorgegimeno.ui.screen.auth.SignInScreen
import com.jorgila.jorgegimeno.ui.screen.auth.SignUpScreen
import com.jorgila.jorgegimeno.ui.screen.main.MainScreen
import com.jorgila.jorgegimeno.ui.screen.splash.SplashScreen

@Composable
fun MainNav(){
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = SplashScreen.route
    ){

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