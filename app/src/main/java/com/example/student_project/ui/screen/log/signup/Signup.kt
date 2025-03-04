package com.example.student_project.ui.screen.log.signup

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHostState
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.student_project.R
import com.example.student_project.data.model.Student
import com.example.student_project.data.repo.StudentRepo
import com.example.student_project.ui.navigation.Screens
import com.example.student_project.ui.screen.widgets.PopBackStackEntry
import com.example.student_project.ui.theme.borderButton
import com.example.student_project.ui.theme.buttonColor
import com.example.student_project.ui.theme.headLineColor
import com.example.student_project.ui.theme.textFieldColor
import com.example.student_project.ui.theme.unselectedButton
import com.example.student_project.util.Constant
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


@Composable
fun SignupScreen(navController: NavController, studentRepo: StudentRepo) {
    val configuration = LocalConfiguration.current
    val screenHeight = configuration.screenHeightDp.dp
    val screenWidth = configuration.screenWidthDp.dp
    val scope = rememberCoroutineScope()
    val context = LocalContext.current

    var firstName by remember { mutableStateOf("") }
    var firstNameError by remember { mutableStateOf(false) }
    var lastNameError by remember { mutableStateOf(false) }
    var verifyState by remember { mutableStateOf(false) }
    var lastName by remember { mutableStateOf("") }

    var emailState by remember { mutableStateOf("") }
    var emailError by remember { mutableStateOf(false) }
    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }
    var passwordError by remember { mutableStateOf(false) }
    var confirmPasswordError by remember { mutableStateOf(false) }
    var showPassword by remember { mutableStateOf(false) }
    var showConfirmPassword by remember { mutableStateOf(false) }

    Scaffold(
        modifier = Modifier
            .fillMaxSize()
    ) { innerPadding ->
//        Column(
//            modifier =
//            Modifier
//                .fillMaxSize()
//                .verticalScroll(rememberScrollState())
//        ) {
//
//        }
        Box(
            modifier =
            Modifier
                .padding(innerPadding)
                .fillMaxSize()
        ) {
            PopBackStackEntry(navController)

            Text(
                text = "Create your Account",
                style = MaterialTheme.typography.headlineLarge,
                fontWeight = FontWeight(700),
                fontSize = 48.sp,
                color = headLineColor,
                modifier = Modifier
                    .padding(
                        top = 70.dp,
                        start = Constant.paddingComponentFromScreen
                    )
                    .align(alignment = Alignment.TopCenter),
            )
            Column(
                modifier = Modifier
                    .align(Alignment.Center)
                    .fillMaxWidth()
                    .padding(top = Constant.paddingTextFromText)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(
                            top = Constant.normalPadding,
                            bottom = Constant.normalPadding,
                            start = Constant.paddingComponentFromScreen,
                            end = Constant.paddingComponentFromScreen
                        )
                ) {
                    TextField(
                        value = firstName,
                        onValueChange = {
                            firstName = it
                            firstNameError = it.isEmpty()
                        },
                        modifier =
                        Modifier
                            .weight(0.5f)
                            .padding(end = 5.dp)
                            .shadow(
                                elevation = 6.dp,
                                shape = MaterialTheme.shapes.small,
                                ambientColor = Color.Gray,
                                spotColor = Color.LightGray,
                            ),
                        label = {
                            Text(
                                text = "First Name",
                                style = MaterialTheme.typography.titleMedium,
                                color = colorResource(id = R.color.icon_gray),
                            )
                        },
                        leadingIcon = {
                            Icon(
                                imageVector = Icons.Filled.Person,
                                tint = unselectedButton,
                                contentDescription = "name icon"
                            )
                        },
                        isError = firstNameError,
                        singleLine = true,
                        // supporting text
                        colors =
                        TextFieldDefaults.colors(
                            unfocusedContainerColor = textFieldColor,
                            focusedContainerColor = textFieldColor,
                            unfocusedIndicatorColor = textFieldColor,
                            focusedIndicatorColor = textFieldColor,
                        ),
                    )

                    TextField(
                        value = lastName,
                        onValueChange = {
                            lastName = it
                            lastNameError = it.isEmpty()
                        },
                        modifier =
                        Modifier
                            .weight(0.5f)
                            .padding(start = 5.dp)
                            .shadow(
                                elevation = 6.dp,
                                shape = MaterialTheme.shapes.small,
                                ambientColor = Color.Gray,
                                spotColor = Color.LightGray,
                            ),
                        label = {
                            Text(
                                text = "Last Name",
                                style = MaterialTheme.typography.titleMedium,
                                color = colorResource(id = R.color.icon_gray),
                            )
                        },
                        isError = lastNameError,
                        singleLine = true,
                        colors =
                        TextFieldDefaults.colors(
                            unfocusedContainerColor = textFieldColor,
                            focusedContainerColor = textFieldColor,
                            unfocusedIndicatorColor = textFieldColor,
                            focusedIndicatorColor = textFieldColor,
                        ),
                    )
                }

                TextField(
                    value = emailState,
                    onValueChange = {
                        emailState = it
                        emailError = it.isEmpty()
                    },
                    modifier =
                    Modifier
                        .fillMaxWidth()
                        .padding(
                            top = Constant.normalPadding,
                            start = Constant.paddingComponentFromScreen,
                            end = Constant.paddingComponentFromScreen,
                            bottom = Constant.normalPadding
                        )

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
                            style = MaterialTheme.typography.titleMedium,
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
                    //                        supportingText = {
                    //                            if (emailError) {
                    //                                Text(
                    //                                    text = "Email cannot be empty",
                    //                                    color = MaterialTheme.colorScheme.error
                    //                                )
                    //                            }
                    //                        },
                    colors =
                    TextFieldDefaults.colors(
                        unfocusedContainerColor = textFieldColor,
                        focusedContainerColor = textFieldColor,
                        unfocusedIndicatorColor = textFieldColor,
                        focusedIndicatorColor = textFieldColor,
                    ),
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
                            style = MaterialTheme.typography.titleMedium,
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
                    if (showPassword) VisualTransformation.None
                    else PasswordVisualTransformation(),
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
                    Modifier
                        .fillMaxWidth()
                        .padding(
                            top = Constant.normalPadding,
                            start = Constant.paddingComponentFromScreen,
                            end = Constant.paddingComponentFromScreen,
                            bottom = Constant.normalPadding
                        )

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
                            style = MaterialTheme.typography.titleMedium,
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
                    Modifier
                        .fillMaxWidth()
                        .padding(
                            top = Constant.normalPadding,
                            start = Constant.paddingComponentFromScreen,
                            end = Constant.paddingComponentFromScreen,
                            bottom = Constant.normalPadding
                        )

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

                Button(
                    onClick = {
                        if (
                            firstName.isNotEmpty() &&
                            lastName.isNotEmpty() &&
                            password.isNotEmpty() &&
                            confirmPassword == password &&
                            emailState.isNotEmpty() &&
                            emailState.endsWith("@gmail.com")
                        ) {
                            val student =
                                Student(
                                    firstName,
                                    lastName,
                                    emailState,
                                    password,
                                    confirmPassword,
                                    "Student",
                                )
                            CoroutineScope(Dispatchers.IO).launch {
                                studentRepo.addUser(student)
                            }
                            verifyState = true
                        } else {
                            Toast.makeText(
                                context,
                                "Please enter valid info",
                                Toast.LENGTH_SHORT
                            )
                                .show()
                            scope.launch {
                                SnackbarHostState()
                                    .showSnackbar(
                                        message = "You invalid info",
                                        duration = SnackbarDuration.Short,
                                    )
                            }
                            lastNameError = true
                            firstNameError = true
                            emailError = true
                            passwordError = true
                            confirmPasswordError = true
                        }

                    },
                    shape = RoundedCornerShape(Constant.buttonRadios),
                    modifier =
                    Modifier
                        .fillMaxWidth()
                        .padding(
                            top = Constant.normalPadding,
                            start = Constant.paddingComponentFromScreen,
                            end = Constant.paddingComponentFromScreen,
                            bottom = Constant.normalPadding
                        )
                        .align(alignment = Alignment.CenterHorizontally),
                    colors =
                    ButtonDefaults.buttonColors(
                        containerColor = colorResource(id = R.color.button_color)
                    ),
                ) {
                    Text(
                        text = "Sign up",
                        fontSize = 16.sp,
                        modifier = Modifier.padding(Constant.smallPadding),
                        fontWeight = FontWeight(700),
                        style = MaterialTheme.typography.titleLarge,
                        color = Color.White,
                    )
                }
                if (verifyState) {
                    AlertDialog(
                        onDismissRequest = { verifyState = false },
                        title = {
                            Text(
                                text = "Go to your email and verify your account 😇😇",
                                style = MaterialTheme.typography.titleMedium,
                                fontSize = 14.sp,
                                fontWeight = FontWeight(600)
                            )
                        },
                        confirmButton = {
                            Button(modifier = Modifier
                                .fillMaxWidth()
                                .padding(
                                    start = Constant.paddingComponentFromScreen,
                                    end = Constant.paddingComponentFromScreen
                                ),
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = buttonColor
                                ), onClick = {
                                    verifyState = false
                                    navController.navigate(Screens.LoginScreen.route)
                                }) {
                                Text(
                                    color = Color.White,
                                    text = "Ok",
                                    style = MaterialTheme.typography.titleLarge,
                                    fontSize = 14.sp,
                                    fontWeight = FontWeight(600)
                                )
                            }
                        })
                }

            }
            Column(
                modifier = Modifier
                    .padding(top = Constant.normalPadding)
                    .align(alignment = Alignment.BottomCenter)
            ) {
                Text(
                    modifier =
                    Modifier
                        .padding(bottom = Constant.normalPadding)
                        .align(alignment = Alignment.CenterHorizontally),
                    text = "Or continue with",
                )
                Row {
                    Button(
                        modifier =
                        Modifier
                            .padding(Constant.normalPadding)
                            .border(
                                1.dp,
                                borderButton,
                                RoundedCornerShape(Constant.buttonRadios)
                            ),
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
                            .border(
                                1.dp,
                                borderButton,
                                RoundedCornerShape(Constant.buttonRadios)
                            ),
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
                            .border(
                                1.dp,
                                borderButton,
                                RoundedCornerShape(Constant.buttonRadios)
                            ),
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
                        text = "Already have an account?",
                        modifier = Modifier.padding(top = 17.dp),
                        fontSize = 14.sp,
                        style = MaterialTheme.typography.headlineSmall,
                    )
                    TextButton(onClick = { navController.navigate(Screens.LoginScreen.route) }) {
                        Text(
                            text = "sign in",
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


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DropDown(navController: NavController) {
    // this is wrong

    val university = listOf("tanta", "other")
    var isExpandedForUniversity by remember { mutableStateOf(false) }
    var universityChoiceState by remember { mutableStateOf("Pick your University") }

    Box(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
        ) {
            ExposedDropdownMenuBox(
                expanded = isExpandedForUniversity,
                onExpandedChange = { isExpandedForUniversity = !isExpandedForUniversity },
            ) {
                TextField(
                    value = universityChoiceState,
                    onValueChange = {},
                    readOnly = true,
                    trailingIcon = {
                        ExposedDropdownMenuDefaults.TrailingIcon(expanded = isExpandedForUniversity)
                    },
                    colors =
                    ExposedDropdownMenuDefaults.textFieldColors(
                        unfocusedContainerColor = Color.Transparent,
                        focusedContainerColor = Color.Transparent,
                    ),
                    modifier =
                    Modifier
                        .menuAnchor()
                        .width(350.dp)
                        .border(1.dp, Color.Gray, RoundedCornerShape(10.dp)),
                )
                ExposedDropdownMenu(
                    expanded = isExpandedForUniversity,
                    onDismissRequest = { isExpandedForUniversity = false },
                ) {
                    university.forEachIndexed { index, text ->
                        DropdownMenuItem(
                            text = { Text(text = text) },
                            onClick = {
                                universityChoiceState = university[index]
                                isExpandedForUniversity = false
                            },
                        )
                    }
                }
            }
            //            Spacer(
            //                modifier = Modifier
            //                    .width(327.dp)
            //                    .height(44.dp)
            //            )
            //            Button(
            //                onClick = {
            //                    //there will be code to send data to back
            //                    //this data is the state of university and major
            //// will nav to dashboard screen
            //
            //                    navController.navigate(Screens.HomeScreen.route)
            //                }, shape = RoundedCornerShape(10.dp),
            //                colors = ButtonDefaults.buttonColors(
            //                    containerColor = colorResource(
            //                        id = R.color.light_green
            //                    )
            //                ),
            //                modifier = Modifier
            //                    .fillMaxWidth()
            //                    .height(50.dp)
            //            ) {
            //                Text(text = "Continue", fontSize = 20.sp)
            //            }
        }
    }
}
