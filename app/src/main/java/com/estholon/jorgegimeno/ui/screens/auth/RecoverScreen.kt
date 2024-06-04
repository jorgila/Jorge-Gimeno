package com.estholon.jorgegimeno.ui.screens.auth

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.estholon.jorgegimeno.R
import com.estholon.jorgegimeno.ui.navigation.Routes
import com.estholon.jorgegimeno.ui.screens.components.NavLink

@Composable
fun RecoverScreen(
    navController: NavHostController,
    recoverViewModel: RecoverViewModel = hiltViewModel()
){
    Box(
        modifier = Modifier.fillMaxSize(),
    ){
        Image(
            painter = painterResource(id = R.drawable.img_fondo1),
            contentDescription = stringResource(R.string.background),
            modifier = Modifier.fillMaxSize()
        )
    }
    Surface(
        color = Color.White,
        modifier = Modifier
            .fillMaxSize()
            .alpha(0.7f)
    ){

    }
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        NavLink(
            string = stringResource(R.string.sign_in).uppercase(),
            alignment = Alignment.TopEnd,
            navigation = { navController.navigate(Routes.SignInScreen.route) },
            modifier = Modifier.width(300.dp)
        )




        Image(
            painter = painterResource(id = R.drawable.img_logo),
            contentDescription = "Jorge Gimeno"
        )
        Spacer(modifier = Modifier.height(30.dp))
    }
}