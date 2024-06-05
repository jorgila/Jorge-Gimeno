package com.estholon.jorgegimeno.ui.screens.splash

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.estholon.jorgegimeno.R
import com.estholon.jorgegimeno.ui.navigation.Routes.*
import com.estholon.jorgegimeno.ui.screens.components.NavLink
import kotlinx.coroutines.delay

@Composable
fun SplashScreen(
    navController: NavHostController,
    splashViewModel: SplashViewModel = hiltViewModel()
){
    // Delay to navigate to other screen

    LaunchedEffect(key1 = true) {
        delay(1000)
        navController.popBackStack()

        if(splashViewModel.isUserLogged()){
            navController.navigate(MainScreen.route)
        } else {
            navController.navigate(SignInScreen.route)
        }

    }

    Box(
        modifier = Modifier.fillMaxSize(),
    ){
        Image(
            painter = painterResource(id = R.drawable.img_background),
            contentDescription = "Jorge Gimeno",
            modifier = Modifier.fillMaxSize()
        )
    }

}