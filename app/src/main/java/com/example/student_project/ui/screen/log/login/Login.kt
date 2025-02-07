package com.example.student_project.ui.screen.log.login

import android.app.Application
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.student_project.R
import com.example.student_project.StudentApp
import com.example.student_project.data.model.User
import com.example.student_project.data.network.request.StudentLogin
import com.example.student_project.data.repo.StudentRepo
import com.example.student_project.di.AppModule
import com.example.student_project.ui.navigation.Screens
import com.example.student_project.ui.theme.borderButton
import com.example.student_project.ui.theme.buttonColor
import com.example.student_project.ui.theme.headLineColor
import com.example.student_project.ui.theme.textFieldColor
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

// this will change
//val studentRepo = StudentRepo()


@Composable
fun LoginScreen(navController: NavController, studentRepo: StudentRepo) {
    // val loginViewModel: LoginViewModel = viewModel()
//    val application = LocalContext.current.applicationContext as StudentApp
//    val studentRepo = application.studentRepo
    val context = LocalContext.current

    //    var idState by remember {
    //        mutableStateOf("")
    //    }
    var resultState by remember { mutableStateOf<Result<User?>?>(null) }
    val errorState by remember { mutableStateOf<String?>(null) }

    val configuration = LocalConfiguration.current
    val screenHeight = configuration.screenHeightDp.dp
    val screenWidth = configuration.screenWidthDp.dp
    val scope = rememberCoroutineScope()
    var emailState by remember { mutableStateOf("") }
    var emailError by remember { mutableStateOf(false) }
    var passwordState by remember { mutableStateOf("") }
    var passwordError by remember { mutableStateOf(false) }
    var showPassword by remember { mutableStateOf(false) }

    // we will make api call

    Scaffold(modifier = Modifier
        .fillMaxSize()
        .background(Color.White)) { innerPadding ->
        Box(
            modifier =
            Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .verticalScroll(rememberScrollState())
        ) {
            Text(
                text = "Login to your Account",
                style = MaterialTheme.typography.headlineLarge,
                fontSize = 30.sp,
                color = headLineColor,
                modifier =
                Modifier
                    .padding(top = 100.dp, start = 10.dp)
                    .align(alignment = Alignment.TopCenter),
            )
            Column(modifier = Modifier
                .align(Alignment.Center)
                .fillMaxWidth()) {

                // we need to make shadow
                TextField(
                    value = emailState,
                    onValueChange = {
                        emailState = it
                        emailError = it.isEmpty()
                    },
                    modifier =
                    Modifier
                        .padding(10.dp)
                        .width(screenWidth * 90 / 100)
                        .align(alignment = Alignment.CenterHorizontally)
                        .shadow(
                            elevation = 6.dp,
                            shape = MaterialTheme.shapes.small,
                            ambientColor = Color.Gray,
                            spotColor = Color.LightGray,
                        ),
                    //  .shadow(elevation = 2.dp, ambientColor = Color.Gray),
                    label = {
                        Text(
                            text = "Email",
                            style = MaterialTheme.typography.headlineSmall,
                            color = colorResource(id = R.color.icon_gray),
                        )
                    },
                    leadingIcon = {
                        Image(
                            painter = painterResource(id = R.drawable.baseline_email_24),
                            contentDescription = null,
                        )
                    },
                    isError = emailError,
                    singleLine = true,
                    //                    supportingText = {
                    //                        if (emailError) {
                    //                            Text(
                    //                                text = "Email cannot be empty",
                    //                                color = MaterialTheme.colorScheme.error
                    //                            )
                    //                        }
                    //                    },
                    colors =
                    TextFieldDefaults.colors(
                        unfocusedContainerColor = textFieldColor,
                        focusedContainerColor = textFieldColor,
                        unfocusedIndicatorColor = textFieldColor,
                        focusedIndicatorColor = textFieldColor,
                    ),
                )
                TextField(
                    modifier =
                    Modifier
                        .padding(10.dp)
                        .width(screenWidth * 90 / 100)
                        .align(alignment = Alignment.CenterHorizontally)
                        .shadow(
                            elevation = 6.dp,
                            shape = MaterialTheme.shapes.small,
                            ambientColor = Color.Gray,
                            spotColor = Color.LightGray,
                        ),
                    // .shadow(elevation = 2.dp, ambientColor = Color.Gray)
                    //      .border(width = 1.dp, color = Color.Transparent),
                    value = passwordState,
                    onValueChange = {
                        passwordState = it
                        passwordError = it.isEmpty()
                    },
                    label = {
                        Text(
                            text = "Password",
                            style = MaterialTheme.typography.headlineSmall,
                            color = colorResource(id = R.color.icon_gray),
                        )
                    },
                    leadingIcon = {
                        Image(
                            painter = painterResource(id = R.drawable.baseline_lock_24),
                            contentDescription = null,
                        )
                    },
                    trailingIcon = {
                        Button(
                            onClick = { showPassword = !showPassword },
                            colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent),
                        ) {
                            Image(
                                painter = painterResource(id = R.drawable.show_pass),
                                modifier = Modifier.size(17.dp),
                                contentDescription = null,
                            )
                        }
                    },
                    singleLine = true,
                    isError = passwordError,
                    //                    supportingText = {
                    //                        if (passwordError) {
                    //                            Text(
                    //                                text = "Password cannot be empty",
                    //                                color = MaterialTheme.colorScheme.error
                    //                            )
                    //                        }
                    //                    },
                    colors =
                    TextFieldDefaults.colors(
                        unfocusedContainerColor = textFieldColor,
                        focusedContainerColor = textFieldColor,
                        unfocusedIndicatorColor = textFieldColor,
                        focusedIndicatorColor = textFieldColor,
                    ),
                    visualTransformation =
                    if (showPassword) VisualTransformation.None
                    else PasswordVisualTransformation(),
                )
                // we will remove this after we make local storage
                // it remember me feature
                Spacer(modifier = Modifier
                    .fillMaxWidth()
                    .height(40.dp))
                Button(
                    onClick = {
                        if (
                            emailState.isNotEmpty() &&
                            passwordState.isNotEmpty() &&
                            emailState.endsWith("@gmail.com")
                        ) {
                            //                            loginResponseState.studentLogin =
                            // StudentLogin(emailState, passwordState)
                            //                            // we check on user data
                            val user = StudentLogin(emailState, passwordState)
                            // i can use scope.launch
                            CoroutineScope(Dispatchers.IO).launch {
                                val result = studentRepo.checkUser(user)
                                resultState = result
                            }
                            resultState
                                ?.onSuccess {
                                    CoroutineScope(Dispatchers.IO).launch {
                                        it?.let {
                                            studentRepo.addUser(it)
                                            //                                        idState =
                                            // it.id
                                        }
                                    }
                                    navController.navigate(Screens.HomeScreen.route)
                                }
                                ?.onFailure {
                                    Toast.makeText(
                                        context,
                                        "your email or password is mismatched",
                                        Toast.LENGTH_SHORT,
                                    )
                                        .show()
                                }
                            // we will send data to back to check if true the move to next false
                            // make error

                            // will navigate to Home screen
                        } else {
                            Toast.makeText(context, "you entered wrong info", Toast.LENGTH_SHORT)
                                .show()
                        }
                    },
                    shape = RoundedCornerShape(100.dp),
                    modifier =
                    Modifier
                        .height(screenHeight * 6 / 100)
                        .width(screenWidth * 90 / 100)
                        .align(alignment = Alignment.CenterHorizontally),
                    colors =
                    ButtonDefaults.buttonColors(
                        containerColor = colorResource(id = R.color.button_color)
                    ),
                ) {
                    Text(
                        text = "Sign in",
                        style = MaterialTheme.typography.headlineLarge,
                        color = Color.White,
                    )
                }
                TextButton(
                    onClick = {
                        // first we will check if email exist
                        // if it exist
                        // we will send email to this screen
                        // if its not we will show error
                        navController.navigate(Screens.EmailAndPhoneScreen.route)
                    },
                    modifier =
                    Modifier
                        .padding(10.dp)
                        .align(alignment = Alignment.CenterHorizontally),
                ) {
                    Text(
                        text = "Forgot the password?",
                        fontSize = 16.sp,
                        color = buttonColor,
                        style = MaterialTheme.typography.headlineLarge,
                    )
                }
            }
            Column(modifier = Modifier.align(Alignment.BottomCenter)) {
                Text(
                    modifier =
                    Modifier
                        .padding(bottom = 10.dp)
                        .align(alignment = Alignment.CenterHorizontally),
                    text = "Or continue with",
                )
                Row {
                    Button(
                        modifier =
                        Modifier
                            .padding(10.dp)
                            .border(1.dp, borderButton, RoundedCornerShape(16.dp)),
                        onClick = { /*TODO*/ },
                        colors = ButtonDefaults.buttonColors(Color.Transparent),
                    ) {
                        Image(
                            modifier = Modifier.size(17.dp),
                            painter = painterResource(id = R.drawable.facebook),
                            contentDescription = null,
                        )
                    }
                    Button(
                        modifier =
                        Modifier
                            .padding(10.dp)
                            .border(1.dp, borderButton, RoundedCornerShape(16.dp)),
                        onClick = { /*TODO*/ },
                        colors = ButtonDefaults.buttonColors(Color.Transparent),
                    ) {
                        Image(
                            painter = painterResource(id = R.drawable.google_img),
                            contentDescription = null,
                        )
                    }
                    Button(
                        modifier =
                        Modifier
                            .padding(10.dp)
                            .border(1.dp, borderButton, RoundedCornerShape(16.dp)),
                        onClick = { /*TODO*/ },
                        colors = ButtonDefaults.buttonColors(Color.Transparent),
                    ) {
                        Image(
                            modifier = Modifier.size(17.dp),
                            painter = painterResource(id = R.drawable.apple),
                            contentDescription = null,
                        )
                    }
                }

                Row(modifier = Modifier.align(Alignment.End)) {
                    Text(
                        text = "Don't have an account?",
                        modifier = Modifier.padding(top = 17.dp),
                        fontSize = 14.sp,
                        style = MaterialTheme.typography.headlineSmall,
                    )
                    TextButton(onClick = { navController.navigate(Screens.SignupScreen.route) }) {
                        Text(
                            text = "sign up",
                            fontSize = 14.sp,
                            color = buttonColor,
                            style = MaterialTheme.typography.headlineLarge,
                        )
                    }
                }
            }
        }
    }
}
