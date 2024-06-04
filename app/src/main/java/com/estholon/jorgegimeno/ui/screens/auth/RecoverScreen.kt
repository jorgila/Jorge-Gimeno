package com.estholon.jorgegimeno.ui.screens.auth

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
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

    Image(
        painter = painterResource(id = R.drawable.img_background),
        contentDescription = stringResource(R.string.background),
        modifier = Modifier.fillMaxSize()
    )

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
        Column(
            modifier = Modifier
                .size(300.dp,800.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            NavLink(
                string = stringResource(R.string.sign_in).uppercase(),
                alignment = Alignment.TopEnd,
                navigation = { navController.navigate(Routes.SignInScreen.route) },
                modifier = Modifier.width(300.dp)
            )
            Spacer(modifier = Modifier.height(20.dp))
            Column(
                modifier = Modifier.size(300.dp, 500.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Image(
                    painter = painterResource(id = R.drawable.img_logo),
                    contentDescription = "Jorge Gimeno"
                )
                Spacer(modifier = Modifier.height(50.dp))
                RecoverByMail(
                    onRecover = {
                        navController.navigate(Routes.SignInScreen.route)
                    },
                )
            }
            Spacer(modifier = Modifier.height(50.dp))

        }
    }
}

@Composable
fun RecoverByMail(
    onRecover: (user: String) -> Unit,
){

    var user by rememberSaveable {
        mutableStateOf("")
    }

    TextField(
        label = { Text(text= stringResource(R.string.email)) },
        value = user,
        keyboardOptions = KeyboardOptions(
            keyboardType = KeyboardType.Email
        ),
        onValueChange = { user = it},
        singleLine = true,
        maxLines = 1
    )

    Spacer(modifier = Modifier.height(10.dp))

    Box(modifier = Modifier.padding(40.dp, 0.dp, 40.dp, 0.dp)) {
        Button(
            onClick = {
                onRecover(user)
            },
            enabled = (user != ""),
            shape = RoundedCornerShape(50.dp),
            modifier = Modifier
                .width(250.dp)
                .height(50.dp)
        ) {
            Text(text = stringResource(R.string.recover).uppercase())
        }
    }
}