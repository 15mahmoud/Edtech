package com.example.student_project.ui.screen.profile.editprofile

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
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
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.core.text.isDigitsOnly
import androidx.navigation.NavController
import com.example.student_project.data.model.User
import com.example.student_project.ui.screen.log.login.studentRepo
import com.example.student_project.ui.screen.widgets.ScaffoldFilterScreenTopBar
import kotlinx.coroutines.launch

@Composable
fun EditProfileScreen(navController: NavController) {
    var studentState by remember { mutableStateOf<User?>(null) }
    var countryState by remember { mutableStateOf("") }
    var genderState by remember { mutableStateOf("") }

    var phoneNumber by remember { mutableStateOf("") }
    val countryList = listOf("United States", "Canada", "United Kingdom", "Australia", "Germany")
    val genderList = listOf("Male", "Female")
    val scope = rememberCoroutineScope()
    LaunchedEffect(scope) {
        val student = studentRepo.getAllStudents()
        studentState = student
    }
    Scaffold(
        topBar = {
            ScaffoldFilterScreenTopBar(navController = navController, text = "Edit Profile")
        }
    ) { innerPadding ->
        Column(modifier = Modifier.padding(innerPadding)) {
            TextField(
                value = studentState?.firstName.toString(),
                textStyle = MaterialTheme.typography.headlineSmall,
                onValueChange = { studentState?.firstName = it },
                modifier = Modifier.fillMaxWidth(),
            )
            TextField(
                value = studentState?.lastName.toString(),
                textStyle = MaterialTheme.typography.headlineSmall,
                onValueChange = { studentState?.lastName = it },
                modifier = Modifier.fillMaxWidth(),
            )
            // here will be text for date
            TextField(
                value = studentState?.email.toString(),
                textStyle = MaterialTheme.typography.headlineSmall,
                onValueChange = {},
                readOnly = true,
                modifier = Modifier.fillMaxWidth(),
            )
            DropdownBox(list = countryList) { countryState = it }
            // of course there is no phone number
            TextField(
                value = phoneNumber,
                textStyle = MaterialTheme.typography.headlineSmall,
                onValueChange = { phoneNumber = it },
                label = { Text(text = "Phone Number") },
            )
            // of course there is no gender
            DropdownBox(list = genderList) { genderState = it }
            TextField(
                value = "Student",
                textStyle = MaterialTheme.typography.headlineSmall,
                onValueChange = {},
                readOnly = true,
                modifier = Modifier.fillMaxWidth(),
            )
            Button(
                onClick = {
                    // here we will send this data to server
                    // and then take response
                    if (phoneNumber.isDigitsOnly() && phoneNumber.length == 11) {
                        studentState?.let {
                            scope.launch {
                                // it should be api request

                                // then save response in database
                                studentRepo.updateStudent(it)
                            }
                        }
                    }
                }
            ) {
                Text(text = "Update")
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DropdownBox(list: List<String>, onChoseItem: (String) -> (Unit)) {
    var expanded by remember { mutableStateOf(false) }
    var selectedText by remember { mutableStateOf(list.first()) }

    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = { expanded = !expanded },
        modifier = Modifier.fillMaxWidth().padding(16.dp),
    ) {
        OutlinedTextField(
            value = selectedText,
            onValueChange = { selectedText = it },
            readOnly = true,
            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
            modifier = Modifier.menuAnchor().fillMaxWidth(),
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
            list.forEach { country ->
                DropdownMenuItem(
                    text = { Text(text = country) },
                    onClick = {
                        selectedText = country
                        expanded = false
                        onChoseItem(selectedText)
                    },
                )
            }
        }
    }
}
