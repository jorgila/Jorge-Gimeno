package com.jorgila.jorgegimeno.ui.screen.main

import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.jorgila.jorgegimeno.ui.navigation.Routes

@Composable
fun MainScreen(
    navController: NavHostController,
    mainViewModel: MainViewModel = hiltViewModel()
){
    Button(onClick = { mainViewModel.logout { navController.navigate(Routes.SignInScreen.route) } }) {
        Text(text = "LOGOUT")
    }
}