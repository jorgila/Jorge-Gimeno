package com.jorgila.jorgegimeno.ui.screen.auth

import android.app.Activity
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.ActivityResult
import androidx.activity.result.ActivityResultRegistryOwner
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
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
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
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
import com.facebook.CallbackManager
import com.facebook.FacebookCallback
import com.facebook.FacebookException
import com.facebook.login.LoginManager
import com.facebook.login.LoginResult
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.common.api.ApiException
import com.jorgila.jorgegimeno.R
import com.jorgila.jorgegimeno.ui.navigation.Routes
import com.jorgila.jorgegimeno.ui.screens.components.NavLink

@Composable
fun SignInScreen(
    navController: NavHostController,
    signInViewModel: SignInViewModel = hiltViewModel()
){

    // VARIABLES

    val context = LocalContext.current
    val activity = LocalContext.current as Activity
    val callbackManager = CallbackManager.Factory.create()

    // GOOGLE

    val googleLauncher =
        rememberLauncherForActivityResult(ActivityResultContracts.StartActivityForResult()) { result: ActivityResult ->
            if(result.resultCode== Activity.RESULT_OK){
                val task = GoogleSignIn.getSignedInAccountFromIntent(result.data)
                try {
                    val account = task.getResult(ApiException::class.java)!!
                    signInViewModel.signInWithGoogle(
                        idToken = account.idToken!!,
                        navigateToHome = { navController.navigate(Routes.MainScreen.route) },
                        communicateError = {message -> Toast.makeText(context,message,Toast.LENGTH_LONG).show()})
                } catch (e: ApiException){
                    Toast.makeText(context,"Ha ocurrido un error: ${e.message}",Toast.LENGTH_SHORT).show()
                }
            }
        }

    // FACEBOOK

    LoginManager.getInstance()
        .registerCallback(callbackManager, object : FacebookCallback<LoginResult> {
            override fun onCancel() {
                Toast.makeText(
                    context,
                    context.getString(R.string.error_another_social_network),
                    Toast.LENGTH_LONG
                ).show()
            }

            override fun onError(error: FacebookException) {
                Toast.makeText(
                    context,
                    context.getString(R.string.error_an_error_has_happened, error.message),
                    Toast.LENGTH_LONG
                ).show()
            }

            override fun onSuccess(result: LoginResult) {
                signInViewModel.signInWithFacebook(
                    result.accessToken,
                    navigateToHome = { navController.navigate(Routes.MainScreen.route) },
                    communicateError = { Toast.makeText(context,signInViewModel.message,Toast.LENGTH_LONG).show() }
                )
            }

        })

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
                string = stringResource(R.string.sign_up).uppercase(),
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
                SignInByMail(
                    onSignInEmail = { user, password ->
                        signInViewModel.signInEmail(
                            email = user,
                            password = password,
                            navigateToHome = { navController.navigate(Routes.MainScreen.route)},
                            communicateError = { Toast.makeText(context,signInViewModel.message,Toast.LENGTH_LONG).show()}
                        )
                    },
                    onForgotPassword = {
                        navController.navigate(Routes.RecoverScreen.route)
                    },
                    signInViewModel = signInViewModel
                )
            }
            OtherMethods(
                onAnonymously = {
                    signInViewModel.signinAnonymously(
                        navigateToHome = { navController.navigate(Routes.MainScreen.route) },
                        communicateError = { Toast.makeText(context,signInViewModel.message,Toast.LENGTH_LONG).show() }
                    )
                },
                onGoogleSignIn = {
                    signInViewModel.onGoogleSignInSelected{
                        googleLauncher.launch(it.signInIntent)
                    }
                },
                onFacebookSignIn = {
                    LoginManager
                        .getInstance()
                        .logInWithReadPermissions(
                            context as ActivityResultRegistryOwner,
                            callbackManager,
                            listOf("email", "public_profile")
                        )
                },
                onGitHubSignIn = {

                    signInViewModel.onOathLoginSelected(
                        oath = OathLogin.GitHub,
                        activity = activity,
                        navigateToHome = { navController.navigate(Routes.MainScreen.route) },
                        communicateError = { Toast.makeText(context,signInViewModel.message,Toast.LENGTH_LONG).show() }
                    )
                },
                onMicrosoftSignIn = {

                    signInViewModel.onOathLoginSelected(
                        oath = OathLogin.Microsoft,
                        activity = activity,
                        navigateToHome = { navController.navigate(Routes.MainScreen.route) },
                        communicateError = { Toast.makeText(context,signInViewModel.message,Toast.LENGTH_LONG).show() }
                    )
                },
                onTwitterSignIn = {
                    signInViewModel.onOathLoginSelected(
                        oath = OathLogin.Twitter,
                        activity = activity,
                        navigateToHome = { navController.navigate(Routes.MainScreen.route) },
                        communicateError = { Toast.makeText(context,signInViewModel.message,Toast.LENGTH_LONG).show() }
                    )
                },
                onYahooSignIn = {
                    signInViewModel.onOathLoginSelected(
                        oath = OathLogin.Yahoo,
                        activity = activity,
                        navigateToHome = { navController.navigate(Routes.MainScreen.route)},
                        communicateError = { Toast.makeText(context,signInViewModel.message,Toast.LENGTH_LONG).show() }
                    )
                }
            )
        }

    }
}


@Composable
fun SignInByMail(
    onSignInEmail: (user: String, password: String) -> Unit,
    onForgotPassword: () -> Unit,
    signInViewModel: SignInViewModel
) {
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
        label = { Text(text= stringResource(R.string.email)) },
        value = user,
        isError = isError,
        keyboardOptions = KeyboardOptions(
            keyboardType = KeyboardType.Email
        ),
        onValueChange = {
            if(signInViewModel.isEmail(it)){
                isError = false
            } else {
                isError = true
            }
            user = it
        },
        maxLines = 1,
        singleLine = true,
    )

    Spacer(modifier = Modifier.height(10.dp))

    TextField(
        label = { Text(text = stringResource(R.string.password)) },
        value = password,
        keyboardOptions = KeyboardOptions(
            keyboardType = KeyboardType.Password
        ),
        onValueChange = {password = it},
        maxLines = 1,
        singleLine = true,
        trailingIcon = {
            val image = if(passwordVisibility){
                Icons.Filled.VisibilityOff
            } else {
                Icons.Filled.Visibility
            }
            IconButton(onClick = { passwordVisibility = !passwordVisibility }) {
                Icon(imageVector = image, contentDescription = "Show password")
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
                    Toast.makeText(context,
                        context.getString(R.string.error_user_has_to_be_an_email), Toast.LENGTH_LONG).show()
                } else {
                    onSignInEmail(user, password)
                }
            },
            enabled = (user != null && password.length >= 6),
            shape = RoundedCornerShape(50.dp),
            modifier = Modifier
                .width(250.dp)
                .height(50.dp),
        ) {
            Text(text = stringResource(R.string.sign_in).uppercase())
        }
    }
    Spacer(modifier = Modifier.height(10.dp))

    TextButton(onClick = {
        onForgotPassword()
    }) {
        Text(text = stringResource(R.string.password_forgotten))
    }

}

@Composable
fun OtherMethods(
    onAnonymously : () -> Unit,
    onGoogleSignIn : () -> Unit,
    onFacebookSignIn : () -> Unit,
    onGitHubSignIn : () -> Unit,
    onMicrosoftSignIn : () -> Unit,
    onTwitterSignIn : () -> Unit,
    onYahooSignIn : () -> Unit
){
    Text(text = "Otros métodos")
    Spacer(modifier = Modifier.height(30.dp))
    Column {
        Row {
            FloatingActionButton(
                onClick = {  }
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_phone),
                    contentDescription = "Phone",
                    modifier = Modifier
                        .size(50.dp)
                        .padding(8.dp)
                )
            }
            Spacer(modifier = Modifier.width(20.dp))
            FloatingActionButton(
                onClick = { onGoogleSignIn() }
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_google),
                    contentDescription = "Google",
                    modifier = Modifier
                        .size(50.dp)
                        .padding(8.dp)
                )
            }
            Spacer(modifier = Modifier.width(20.dp))
            FloatingActionButton(
                onClick = { onFacebookSignIn() }
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_fb),
                    contentDescription = "Facebook",
                    modifier = Modifier
                        .size(50.dp)
                        .padding(8.dp)
                )
            }
            Spacer(modifier = Modifier.width(20.dp))
            FloatingActionButton(
                onClick = { onGitHubSignIn() }
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_github),
                    contentDescription = "GitHub",
                    modifier = Modifier
                        .size(50.dp)
                        .padding(8.dp)
                )
            }

        }
        Spacer(modifier = Modifier.height(30.dp))
        Row {
            FloatingActionButton(
                onClick = { onMicrosoftSignIn() }
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_microsoft),
                    contentDescription = "Microsoft",
                    modifier = Modifier
                        .size(50.dp)
                        .padding(8.dp)
                )
            }
            Spacer(modifier = Modifier.width(20.dp))
            FloatingActionButton(
                onClick = { onTwitterSignIn() }
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_twitter),
                    contentDescription = "Twitter",
                    modifier = Modifier
                        .size(50.dp)
                        .padding(8.dp)
                )
            }
            Spacer(modifier = Modifier.width(20.dp))
            FloatingActionButton(
                onClick = { onYahooSignIn() }
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_yahoo),
                    contentDescription = "Yahoo",
                    modifier = Modifier
                        .size(50.dp)
                        .padding(8.dp)
                )
            }
            Spacer(modifier = Modifier.width(20.dp))
            FloatingActionButton(
                onClick = {
                    onAnonymously()
                }
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_anonymously),
                    contentDescription = "Anonymously",
                    modifier = Modifier
                        .size(50.dp)
                        .padding(8.dp)
                )
            }

        }
    }
}