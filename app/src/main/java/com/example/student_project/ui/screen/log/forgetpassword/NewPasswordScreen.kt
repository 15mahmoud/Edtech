package com.example.student_project.ui.screen.log.forgetpassword

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
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
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
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
import com.example.student_project.ui.navigation.Screens
import com.example.student_project.screen.uiconstant.PopBackStackEntry
import com.example.student_project.ui.theme.headLineColor
import com.example.student_project.ui.theme.textFieldColor

@Composable
fun NewPasswordScreen(navController: NavController) {
    val context = LocalContext.current
    val configuration = LocalConfiguration.current
    val screenHeight = configuration.screenHeightDp.dp
    val screenWidth = configuration.screenWidthDp.dp

    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }
    var passwordError by remember { mutableStateOf(false) }
    var confirmPasswordError by remember { mutableStateOf(false) }
    var showPassword by remember { mutableStateOf(false) }
    var showConfirmPassword by remember { mutableStateOf(false) }

    Column(modifier = Modifier.verticalScroll(rememberScrollState())) {
        Row(modifier = Modifier.padding(top = 50.dp, start = 10.dp, bottom = 50.dp)) {
            PopBackStackEntry(navController)
            Text(
                text = "Create New Password",
                style = MaterialTheme.typography.headlineLarge,
                fontSize = 24.sp,
                color = headLineColor,
                modifier = Modifier.padding(top = 30.dp, start = 10.dp),
            )
        }
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Center) {
            Image(
                modifier =
                    Modifier.width(screenWidth * 64 / 100)
                        .height(screenHeight * 28 / 100)
                        .align(alignment = Alignment.CenterVertically),
                painter = painterResource(id = R.drawable.change_password),
                contentDescription = null,
            )
        }
        Text(
            modifier =
                Modifier.align(alignment = Alignment.CenterHorizontally)
                    .padding(top = 50.dp, start = 10.dp, end = 10.dp, bottom = 10.dp),
            text = "Create New Password",
            style = MaterialTheme.typography.titleMedium,
            fontSize = 18.sp,
        )
        TextField(
            value = password,
            onValueChange = {
                password = it
                passwordError = it.isEmpty()
            },
            label = {
                Text(
                    "Password",
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
            visualTransformation =
                if (showPassword) VisualTransformation.None else PasswordVisualTransformation(),
            isError = passwordError,
            //                        supportingText = {
            //                            if (passwordError) {
            //                                Text(
            //                                    text = "Password cannot be empty",
            //                                    color = MaterialTheme.colorScheme.error
            //                                )
            //                            }
            //                        },
            modifier =
                Modifier.padding(10.dp)
                    .width(screenWidth * 90 / 100)
                    .align(alignment = Alignment.CenterHorizontally)
                    .shadow(
                        elevation = 6.dp,
                        shape = MaterialTheme.shapes.small,
                        ambientColor = Color.Gray,
                        spotColor = Color.LightGray,
                    ),
            colors =
                TextFieldDefaults.colors(
                    unfocusedContainerColor = textFieldColor,
                    focusedContainerColor = textFieldColor,
                    unfocusedIndicatorColor = textFieldColor,
                    focusedIndicatorColor = textFieldColor,
                ),
        )

        TextField(
            value = confirmPassword,
            onValueChange = {
                confirmPassword = it
                confirmPasswordError = it.isEmpty() || it != password
            },
            label = {
                Text(
                    "Confirm Password",
                    style = MaterialTheme.typography.headlineSmall,
                    color = colorResource(id = R.color.icon_gray),
                )
            },
            isError = confirmPasswordError,
            //                        supportingText = {
            //                            if (confirmPasswordError) {
            //                                Text(
            //                                    text = "Passwords don't match",
            //                                    color = MaterialTheme.colorScheme.error
            //                                )
            //                            }
            //                        },
            leadingIcon = {
                Image(
                    painter = painterResource(id = R.drawable.baseline_lock_24),
                    contentDescription = null,
                )
            },
            trailingIcon = {
                Button(
                    onClick = { showConfirmPassword = !showConfirmPassword },
                    colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent),
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.show_pass),
                        modifier = Modifier.size(17.dp),
                        contentDescription = null,
                    )
                }
            },
            visualTransformation =
                if (showConfirmPassword) VisualTransformation.None
                else PasswordVisualTransformation(),
            modifier =
                Modifier.padding(10.dp)
                    .width(screenWidth * 90 / 100)
                    .align(alignment = Alignment.CenterHorizontally)
                    .shadow(
                        elevation = 6.dp,
                        shape = MaterialTheme.shapes.small,
                        ambientColor = Color.Gray,
                        spotColor = Color.LightGray,
                    ),
            colors =
                TextFieldDefaults.colors(
                    unfocusedContainerColor = textFieldColor,
                    focusedContainerColor = textFieldColor,
                    unfocusedIndicatorColor = textFieldColor,
                    focusedIndicatorColor = textFieldColor,
                ),
        )
        // this will be for remember me feature
        Spacer(modifier = Modifier.height(20.dp))
        Button(
            onClick = {
                // Handle sign-up logic here, including validation
                if (password.isNotEmpty() && confirmPassword == password) {
                    // send this password to the backend with email to modify it
                    // and then back to login
                    navController.navigate(Screens.LoginScreen.route)
                } else {
                    Toast.makeText(context, "You insert invalid info", Toast.LENGTH_SHORT).show()
                }
            },
            shape = RoundedCornerShape(100.dp),
            modifier =
                Modifier.padding(top = 40.dp)
                    .align(alignment = Alignment.CenterHorizontally)
                    //                .width(screenWidth * 90 / 100)
                    .height(screenHeight * 6 / 100),
            colors =
                ButtonDefaults.buttonColors(
                    containerColor = colorResource(id = R.color.button_color)
                ),
        ) {
            Text(
                text = "Continue",
                style = MaterialTheme.typography.headlineLarge,
                color = Color.White,
            )
        }
    }
}
