package com.example.student_project.ui.screen.profile.editprofile

import android.widget.Toast
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.core.text.isDigitsOnly
import androidx.navigation.NavController
import com.example.student_project.data.model.User
import com.example.student_project.data.network.request.StudentUpdateRequest
import com.example.student_project.data.repo.StudentRepo
import com.example.student_project.ui.navigation.Screens
import com.example.student_project.ui.screen.widgets.ScaffoldFilterScreenTopBar
import com.example.student_project.ui.theme.ambientShadowColor
import com.example.student_project.ui.theme.buttonColor
import com.example.student_project.ui.theme.spotShadowColor
import com.example.student_project.util.Constant
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Composable
fun EditProfileScreen(navController: NavController, studentRepo: StudentRepo) {
    var studentState by remember { mutableStateOf<User?>(null) }
    var studentResponseState by remember { mutableStateOf<Result<User?>?>(null) }

    val configuration = LocalConfiguration.current
    val screenHeight = configuration.screenHeightDp.dp
    val screenWidth = configuration.screenWidthDp.dp

    val context = LocalContext.current
    // var countryState by remember { mutableStateOf("") }
    var genderState by remember { mutableStateOf<String?>(null) }
    //    val countryList = listOf("United States", "Canada", "United Kingdom", "Australia",
    // "Germany")
    val genderList = listOf("null", "Male", "Female")
    val scope = rememberCoroutineScope()
    var firstName by remember { mutableStateOf("") }
    var lastName by remember { mutableStateOf("") }
    var phoneNumber by remember { mutableStateOf("") }
    val phoneNumberError by remember { mutableStateOf(false) }
    var aboutState by remember { mutableStateOf("") }
    LaunchedEffect(scope) {
        val student = studentRepo.getCurrentStudent()
        studentState = student
        firstName = studentState?.firstName.toString()
        lastName = studentState?.lastName.toString()
        phoneNumber = studentState?.additionalDetails?.contactNumber.toString()
        aboutState = studentState?.additionalDetails?.about.toString()
        genderState = studentState?.additionalDetails?.gender.toString()
    }
    Scaffold(
        topBar = {
            ScaffoldFilterScreenTopBar(navController = navController, text = "Edit Profile")
        }
    ) { innerPadding ->
        Column(modifier = Modifier
            .padding(innerPadding)
            .verticalScroll(rememberScrollState())) {
            OutlinedTextField(
                value = firstName,
                onValueChange = { firstName = it },
                textStyle = MaterialTheme.typography.headlineLarge,
                shape = RoundedCornerShape(15.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(15.dp),
                // .shadow(),
                label = { Text(text = "First Name") },
            )
            OutlinedTextField(
                value = lastName,
                textStyle = MaterialTheme.typography.headlineLarge,
                onValueChange = { lastName = it },
                shape = RoundedCornerShape(15.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(15.dp),
                label = { Text(text = "Last Name") },
            )
            // here will be text for date
            OutlinedTextField(
                value = studentState?.email.toString(),
                onValueChange = {},
                readOnly = true,
                textStyle = MaterialTheme.typography.headlineLarge,
                shape = RoundedCornerShape(15.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(15.dp),
                label = { Text(text = "Email") },
            )

            //   DropdownBox(list = countryList) { countryState = it }
            // of course there is no phone number
            OutlinedTextField(
                value = phoneNumber,
                textStyle = MaterialTheme.typography.headlineLarge,
                onValueChange = { phoneNumber = it },
                keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number),
                isError =
                if (
                    phoneNumber.isDigitsOnly() &&
                    phoneNumber.length == 11
//                            phoneNumber.startsWith("010") &&
//                            phoneNumber.startsWith("011") &&
//                            phoneNumber.startsWith("012") &&
//                            phoneNumber.startsWith("015")
                )
                    phoneNumberError
                else !phoneNumberError,
                shape = RoundedCornerShape(15.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(15.dp),
                label = { Text(text = "Phone Number") },
            )
            // of course there is no gender
            genderState?.let { DropdownBox(list = genderList, gender = it) { genderState = it } }

            OutlinedTextField(
                value = aboutState,
                textStyle = MaterialTheme.typography.headlineLarge,
                onValueChange = { aboutState = it },
                shape = RoundedCornerShape(15.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(15.dp),
                label = { Text(text = "About") },
            )
            OutlinedTextField(
                value = "Student",
                textStyle = MaterialTheme.typography.headlineLarge,
                onValueChange = {},
                readOnly = true,
                shape = RoundedCornerShape(15.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(15.dp),
            )
            Button(
                shape = RoundedCornerShape(Constant.buttonRadios),
                modifier =
                Modifier
                    .padding(top = 40.dp, bottom = Constant.veryLargePadding)
                    .align(alignment = Alignment.CenterHorizontally)
                    .width(screenWidth * 90 / 100)
                    .shadow(
                        elevation = 10.dp,
                        RoundedCornerShape(Constant.buttonRadios),
                        spotColor = spotShadowColor.copy(alpha = 0.4f),
                        ambientColor = ambientShadowColor.copy(alpha = 0.35f),
                    ),
                colors =
                ButtonDefaults.buttonColors(
                    containerColor = buttonColor,
                    contentColor = Color.White,
                ),
                onClick = {
                    // here we will send this data to server
                    // and then take response
                    if (
                        phoneNumber.isDigitsOnly() &&
                        phoneNumber.length == 11
//                            phoneNumber.startsWith("010") &&
//                            phoneNumber.startsWith("011") &&
//                            phoneNumber.startsWith("012") &&
//                            phoneNumber.startsWith("015")
                    ) {
                        //                        studentUpdateRequestState =
                        // studentUpdateRequestState?.copy()
                        val student =
                            StudentUpdateRequest(
                                about = aboutState,
                                contactNumber = "+$phoneNumber",
                                dateOfBirth = "1990-01-01",
                                firstName = firstName,
                                lastName = lastName,
                                gender = genderState,
                            )

                        CoroutineScope(Dispatchers.IO).launch {
                            studentResponseState = studentRepo.updateProfile(student)
                        }

                        studentResponseState
                            ?.onSuccess {
                                Toast.makeText(context, "Updated", Toast.LENGTH_SHORT).show()
                                navController.navigate(Screens.ProfileScreen.route)
                            }
                            ?.onFailure {
                                Toast.makeText(context, it.message, Toast.LENGTH_SHORT).show()
                            }
                    }
                },
            ) {
                Text(text = "Update")
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DropdownBox(list: List<String>, gender: String, onChoseItem: (String) -> (Unit)) {
    var expanded by remember { mutableStateOf(false) }
    var selectedText by remember { mutableStateOf(gender) }

    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = { expanded = !expanded },
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
    ) {
        OutlinedTextField(
            value = selectedText,
            onValueChange = { selectedText = it },
            readOnly = true,
            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
            modifier = Modifier
                .menuAnchor()
                .fillMaxWidth(),
            colors =
            OutlinedTextFieldDefaults.colors(
                unfocusedBorderColor = MaterialTheme.colorScheme.outline,
                focusedBorderColor = MaterialTheme.colorScheme.primary,
            ),
        )

        ExposedDropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
            modifier = Modifier.verticalScroll(rememberScrollState()),
        ) {
            list.forEach { gender ->
                DropdownMenuItem(
                    text = { Text(text = gender) },
                    onClick = {
                        selectedText = gender
                        expanded = false
                        onChoseItem(selectedText)
                    },
                )
            }
        }
    }
}
