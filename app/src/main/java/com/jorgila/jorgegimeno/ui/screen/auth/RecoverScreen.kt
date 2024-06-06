package com.jorgila.jorgegimeno.ui.screen.auth

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.jorgila.jorgegimeno.R

@Composable
fun RecoverScreen(
    navController: NavHostController,
    recoverViewModel: RecoverViewModel = hiltViewModel()
){
    Image(
        painter = painterResource(id = R.drawable.img_background),
        contentDescription = stringResource(R.string.background),
        contentScale = ContentScale.FillBounds,
        modifier = Modifier.fillMaxSize()
    )

    Surface(
        color = Color.White,
        modifier = Modifier
            .fillMaxSize()
            .alpha(0.7f)
    ){

    }




    if(recoverViewModel.isLoading){
        Box(modifier = Modifier.fillMaxSize()){
            CircularProgressIndicator(modifier = Modifier
                .size(100.dp)
                .align(Alignment.Center))
        }
    }

}