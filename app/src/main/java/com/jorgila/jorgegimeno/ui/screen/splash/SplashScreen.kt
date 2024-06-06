package com.jorgila.jorgegimeno.ui.screen.splash

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.jorgila.jorgegimeno.R
import com.jorgila.jorgegimeno.ui.navigation.Routes
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

        if(splashViewModel.isLogged()){
            navController.navigate(Routes.MainScreen.route)
        } else {
            navController.navigate(Routes.SignInScreen.route)
        }
    }

    Image(
        painter = painterResource(id = R.drawable.img_background),
        contentDescription = stringResource(R.string.background),
        contentScale = ContentScale.FillBounds,
        modifier = Modifier.fillMaxSize()
    )

}