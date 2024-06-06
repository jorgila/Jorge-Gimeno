package com.jorgila.jorgegimeno.ui.screen.auth

import android.widget.Toast
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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.jorgila.jorgegimeno.R
import com.jorgila.jorgegimeno.ui.navigation.Routes
import com.jorgila.jorgegimeno.ui.screens.components.NavLink
import kotlin.math.sign

@Composable
fun SignUpScreen(
    navController: NavHostController,
    signUpViewModel: SignUpViewModel = hiltViewModel()
){

    // VARIABLES

    val context = LocalContext.current

    // UI

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

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Column(
            modifier = Modifier
                .size(300.dp, 800.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            NavLink(
                string = stringResource(R.string.sign_in).uppercase(),
                alignment = Alignment.TopEnd,
                navigation = { navController.navigate(Routes.SignUpScreen.route) },
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
                SignUpByMail(
                    onSignUpEmail = { user, password ->
                        signUpViewModel.signUpEmail(
                            email = user,
                            password = password,
                            navigateToHome = { navController.navigate(Routes.MainScreen.route)},
                            communicateError = { Toast.makeText(context,signUpViewModel.message,Toast.LENGTH_LONG).show() }
                        )
                    },
                    signUpViewModel = signUpViewModel
                )
            }
            OtherMethods(
                onAnonymously = { /*TODO*/ },
                onGoogleSignIn = { /*TODO*/ },
                onFacebookSignIn = { /*TODO*/ },
                onGitHubSignIn = { /*TODO*/ },
                onMicrosoftSignIn = { /*TODO*/ },
                onTwitterSignIn = { /*TODO*/ }) {
            }
        }
    }
}

@Composable
fun SignUpByMail(
    onSignUpEmail: (user: String, password: String) -> Unit,
    signUpViewModel: SignUpViewModel = hiltViewModel()
){

    val context = LocalContext.current

    var user by rememberSaveable {
        mutableStateOf("")
    }

    var isError by rememberSaveable {
        mutableStateOf(false)
    }

    var password by rememberSaveable {
        mutableStateOf("")
    }

    var passwordVisibility by rememberSaveable {
        mutableStateOf(false)
    }

    TextField(
        label = { Text(text= stringResource(id = R.string.email)) },
        value = user,
        isError = isError,
        keyboardOptions = KeyboardOptions(
            keyboardType = KeyboardType.Email
        ),
        onValueChange = {
            if(signUpViewModel.isEmail(it)){
                isError = false
            } else {
                isError = true
            }
            user = it
        },
        singleLine = true,
        maxLines = 1
    )

    Spacer(modifier = Modifier.height(10.dp))

    TextField(
        label = { Text(text = stringResource(id = R.string.password)) },
        value = password,
        keyboardOptions = KeyboardOptions(
            keyboardType = KeyboardType.Password
        ),
        onValueChange = {password = it},
        singleLine = true,
        maxLines = 1,
        trailingIcon = {
            val image = if(passwordVisibility){
                Icons.Filled.VisibilityOff
            } else {
                Icons.Filled.Visibility
            }
            IconButton(onClick = { passwordVisibility = !passwordVisibility }) {
                Icon(imageVector = image, contentDescription = stringResource(R.string.show_password))
            }
        },
        visualTransformation = if(passwordVisibility){
            VisualTransformation.None
        } else {
            PasswordVisualTransformation()
        }
    )

    Spacer(modifier = Modifier.height(10.dp))

    Box(modifier = Modifier.padding(40.dp, 0.dp, 40.dp, 0.dp)) {
        Button(
            onClick = {
                if(isError){
                    Toast.makeText(context, context.getString(R.string.error_user_has_to_be_an_email), Toast.LENGTH_LONG).show()
                } else {
                    onSignUpEmail(user, password)
                }
            },
            enabled = (user != null && password.length >= 6),
            shape = RoundedCornerShape(50.dp),
            modifier = Modifier
                .width(250.dp)
                .height(50.dp)
        ) {
            Text(text = stringResource(id = R.string.sign_up).uppercase())
        }
    }
    Spacer(modifier = Modifier.height(60.dp))
}