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
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.student_project.R
import com.example.student_project.data.model.Student
import com.example.student_project.ui.navigation.Screens
import com.example.student_project.ui.screen.uiconstant.PopBackStackEntry
import com.example.student_project.ui.theme.headLineColor
import com.example.student_project.ui.theme.textFieldColor

@Composable
fun AdditionalInfoScreen(navController: NavController, email: String?, password: String?) {
    var signupViewModel: SignupViewModel = viewModel()

    val configuration = LocalConfiguration.current
    val screenHeight = configuration.screenHeightDp.dp
    val screenWidth = configuration.screenWidthDp.dp
    val scope = rememberCoroutineScope()
    val context = LocalContext.current
    var nameState by remember { mutableStateOf("") }
    var nameError by remember { mutableStateOf(false) }
    var nicknameState by remember { mutableStateOf("") }
    var dateOfBirthState by remember { mutableStateOf("") }
    var emailState by remember { mutableStateOf("") }
    var phoneState by remember { mutableStateOf("") }
    var phoneEmptyError by remember { mutableStateOf(false) }

    Column(modifier = Modifier.verticalScroll(rememberScrollState())) {
        Row(modifier = Modifier.padding(top = 50.dp)) {
            PopBackStackEntry(navController)
            Text(
                text = "Fill Your Profile",
                style = MaterialTheme.typography.headlineLarge,
                fontSize = 24.sp,
                color = headLineColor,
                modifier = Modifier.padding(top = 30.dp, start = 10.dp),
            )
        }
        Row(
            modifier = Modifier.fillMaxWidth().padding(10.dp),
            horizontalArrangement = Arrangement.Center,
        ) {
            Card(
                modifier = Modifier.size(150.dp).align(alignment = Alignment.CenterVertically),
                shape = CircleShape,
                colors = CardDefaults.cardColors(containerColor = Color.Transparent),
            ) {
                Image(
                    painter = painterResource(id = R.drawable.profile_pics),
                    contentDescription = "profile pic",
                    modifier = Modifier.size(150.dp),
                )
            }
        }
        TextField(
            value = nameState,
            onValueChange = {
                nameState = it
                nameError = it.isEmpty()
            },
            modifier =
                Modifier.width(screenWidth * 90 / 100)
                    .align(alignment = Alignment.CenterHorizontally)
                    .padding(5.dp)
                    .shadow(
                        elevation = 6.dp,
                        shape = MaterialTheme.shapes.small,
                        ambientColor = Color.Gray,
                        spotColor = Color.LightGray,
                    ),
            label = {
                Text(
                    text = "Full Name",
                    style = MaterialTheme.typography.headlineSmall,
                    color = colorResource(id = R.color.icon_gray),
                )
            },
            isError = nameError,
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
            value = nicknameState,
            onValueChange = { nicknameState = it },
            modifier =
                Modifier.width(screenWidth * 90 / 100)
                    .align(alignment = Alignment.CenterHorizontally)
                    .padding(5.dp)
                    .shadow(
                        elevation = 6.dp,
                        shape = MaterialTheme.shapes.small,
                        ambientColor = Color.Gray,
                        spotColor = Color.LightGray,
                    ),
            label = {
                Text(
                    text = "Nickname",
                    style = MaterialTheme.typography.headlineSmall,
                    color = colorResource(id = R.color.icon_gray),
                )
            },
            singleLine = true,
            colors =
                TextFieldDefaults.colors(
                    unfocusedContainerColor = textFieldColor,
                    focusedContainerColor = textFieldColor,
                    unfocusedIndicatorColor = textFieldColor,
                    focusedIndicatorColor = textFieldColor,
                ),
        )
        TextField(
            value = dateOfBirthState,
            onValueChange = { dateOfBirthState = it },
            modifier =
                Modifier.width(screenWidth * 90 / 100)
                    .align(alignment = Alignment.CenterHorizontally)
                    .padding(5.dp)
                    // i need to add border
                    .shadow(
                        elevation = 6.dp,
                        shape = MaterialTheme.shapes.small,
                        ambientColor = Color.Gray,
                        spotColor = Color.LightGray,
                    ),
            label = {
                Text(
                    text = "Date of Birth",
                    style = MaterialTheme.typography.headlineSmall,
                    color = colorResource(id = R.color.icon_gray),
                )
            },
            singleLine = true,
            colors =
                TextFieldDefaults.colors(
                    unfocusedContainerColor = textFieldColor,
                    focusedContainerColor = textFieldColor,
                    unfocusedIndicatorColor = textFieldColor,
                    focusedIndicatorColor = textFieldColor,
                ),
        )
        TextField(
            value = emailState,
            onValueChange = { emailState = it },
            modifier =
                Modifier.width(screenWidth * 90 / 100)
                    .align(alignment = Alignment.CenterHorizontally)
                    .padding(5.dp)
                    // i need to add border
                    .shadow(
                        elevation = 6.dp,
                        shape = MaterialTheme.shapes.small,
                        ambientColor = Color.Gray,
                        spotColor = Color.LightGray,
                    ),
            label = {
                Text(
                    text = "Email",
                    style = MaterialTheme.typography.headlineSmall,
                    color = colorResource(id = R.color.icon_gray),
                )
            },
            singleLine = true,
            colors =
                TextFieldDefaults.colors(
                    unfocusedContainerColor = textFieldColor,
                    focusedContainerColor = textFieldColor,
                    unfocusedIndicatorColor = textFieldColor,
                    focusedIndicatorColor = textFieldColor,
                ),
        )
        TextField(
            value = phoneState,
            onValueChange = {
                phoneState = it
                // this need modify need to make sure this is number
                phoneEmptyError = it.isEmpty()
            },
            modifier =
                Modifier.width(screenWidth * 90 / 100)
                    .align(alignment = Alignment.CenterHorizontally)
                    .padding(5.dp)
                    .shadow(
                        elevation = 6.dp,
                        shape = MaterialTheme.shapes.small,
                        ambientColor = Color.Gray,
                        spotColor = Color.LightGray,
                    ),
            label = {
                Text(
                    text = "Phone Number",
                    style = MaterialTheme.typography.headlineSmall,
                    color = colorResource(id = R.color.icon_gray),
                )
            },
            isError =
                if (
                    phoneEmptyError || (phoneState.length != 11 && phoneState.all { it.isLetter() })
                )
                    true
                else phoneState.isEmpty(),
            singleLine = true,
            //            supportingText = {
            //                if (phoneEmptyError) {
            //                    Text(
            //                        text = "Phone cannot be empty ",
            //                        color = MaterialTheme.colorScheme.error
            //                    )
            //                }else if (phoneState.length != 11 && phoneState.all { !it.isLetter()
            // }){
            //                 Text(text = "Please enter a valid Phone ",
            //                     color = MaterialTheme.colorScheme.error)
            //                }
            //            },
            colors =
                TextFieldDefaults.colors(
                    unfocusedContainerColor = textFieldColor,
                    focusedContainerColor = textFieldColor,
                    unfocusedIndicatorColor = textFieldColor,
                    focusedIndicatorColor = textFieldColor,
                ),
        )

        // DropDown(navController)

        Button(
            onClick = {
                // Handle sign-up logic here, including validation
                if (
                    nameState.isNotEmpty() &&
                        phoneState.isNotEmpty() &&
                        phoneState.length == 11 &&
                        phoneState.all { it.isDigit() }
                ) {
                    val student =
                        Student(
                            nameState,
                            nicknameState,
                            email.toString(),
                            password.toString(),
                            password.toString(),
                            "Student",
                        )
                    signupViewModel.addStudent(student)
                    navController.navigate(Screens.LoginScreen.route)
                    // Proceed to next screen or perform sign-up actions
                    // this one will change
                } else {
                    Toast.makeText(context, "Please enter valid info", Toast.LENGTH_SHORT).show()
                    //                    scope.launch {
                    //                        SnackbarHostState()
                    //                            .showSnackbar(
                    //                                message = "You invalid info",
                    //                                duration = SnackbarDuration.Short,
                    //                            )
                    //                    }
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

// we need to modify this code to make it usable for every place in our code
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
                        Modifier.menuAnchor()
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
