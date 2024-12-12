package com.example.student_project.screen.home.filtering

import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.student_project.R
import com.example.student_project.screen.Screens
import com.example.student_project.ui.theme.lightGreen

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MentorFilterScreen(navController: NavController) {
    val major = listOf("MAT 116", "ARCH 117", "other")
    var isExpandedForMajor by remember { mutableStateOf(false) }
    var majorChoiceState by remember { mutableStateOf("MATH 116") }
    val yearOfExperience = listOf("1-3 Years", "3-6 Years", "+6 Years")
    var isExpandedForExperience by remember { mutableStateOf(false) }
    var yearOfExperienceChoiceState by remember { mutableStateOf("1-3 Years") }
    val degreeAndCertificate =
        listOf("Master's in Applied Mathematics", "Bachelor's in Applied Mathematics")
    var isExpandedForDegree by remember { mutableStateOf(false) }
    var degreeAndCertificateChoiceState by remember {
        mutableStateOf("Master's in Applied Mathematics")
    }
    val daySelected = mutableListOf<String>()
    val timeSlotSelected = mutableListOf<String>()

    Scaffold(
        topBar = {
            ScaffoldFilterScreenTopBar(navController = navController, Screens.HomeScreen.route)
        },
        bottomBar = {
            NavigationBar {
                Row(modifier = Modifier.fillMaxWidth()) {
                    Button(onClick = { navController.navigate(Screens.HomeScreen.route) }) {
                        Text(text = "Reset Filter")
                    }
                    Button(
                        onClick = {
                            // filled object first then we will send it to second screen
                            // var filterationRequest = FilterationRequest()

                            // navigate with arguments "filteration object" to search result
                        }
                    ) {
                        Text(text = "Apply Filter")
                    }
                }
            }
        },
    ) { innerPadding ->
        Column(Modifier.padding(innerPadding).verticalScroll(rememberScrollState())) {
            Text(text = "Subject")

            ExposedDropdownMenuBox(
                expanded = isExpandedForMajor,
                onExpandedChange = { isExpandedForMajor = !isExpandedForMajor },
            ) {
                TextField(
                    value = majorChoiceState,
                    onValueChange = { majorChoiceState = it },
                    readOnly = true,
                    trailingIcon = {
                        ExposedDropdownMenuDefaults.TrailingIcon(expanded = isExpandedForMajor)
                    },
                    colors =
                        ExposedDropdownMenuDefaults.textFieldColors(
                            unfocusedContainerColor = Color.Transparent,
                            focusedContainerColor = Color.Transparent,
                        ),
                    modifier =
                        Modifier.menuAnchor()
                            .fillMaxWidth()
                            .border(1.dp, Color.Gray, RoundedCornerShape(10.dp)),
                )
                ExposedDropdownMenu(
                    expanded = isExpandedForMajor,
                    onDismissRequest = { isExpandedForMajor = false },
                ) {
                    major.forEachIndexed { index, text ->
                        DropdownMenuItem(
                            text = { Text(text = text) },
                            onClick = {
                                majorChoiceState = major[index]
                                isExpandedForMajor = false
                            },
                        )
                    }
                }
            }

            Text(text = "Availability", Modifier.padding(top = 10.dp))
            // implement lazy row for days ass button
            Row(modifier = Modifier.fillMaxWidth().horizontalScroll(rememberScrollState())) {
                DayBickerButton(timeSelected = daySelected, time = "Saterday")

                DayBickerButton(timeSelected = daySelected, time = "Sunday")

                DayBickerButton(timeSelected = daySelected, time = "Monday")

                DayBickerButton(timeSelected = daySelected, time = "Tuesday")

                DayBickerButton(timeSelected = daySelected, time = "Wednesday")

                DayBickerButton(timeSelected = daySelected, time = "Tharseday")

                DayBickerButton(timeSelected = daySelected, time = "Friday")
            }

            Text(text = "Time slot", Modifier.padding(top = 10.dp))
            // same as the top one
            Row(modifier = Modifier.fillMaxWidth().horizontalScroll(rememberScrollState())) {
                DayBickerButton(timeSelected = timeSlotSelected, time = "Morning")

                DayBickerButton(timeSelected = timeSlotSelected, time = "Afternoon")

                DayBickerButton(timeSelected = timeSlotSelected, time = "Evening")
            }

            Text(text = "Tutoring Experience", Modifier.padding(top = 10.dp))
            // implement drop down menu
            ExposedDropdownMenuBox(
                expanded = isExpandedForExperience,
                onExpandedChange = { isExpandedForExperience = !isExpandedForExperience },
            ) {
                TextField(
                    value = yearOfExperienceChoiceState,
                    onValueChange = { yearOfExperienceChoiceState = it },
                    readOnly = true,
                    trailingIcon = {
                        ExposedDropdownMenuDefaults.TrailingIcon(expanded = isExpandedForExperience)
                    },
                    colors =
                        ExposedDropdownMenuDefaults.textFieldColors(
                            unfocusedContainerColor = Color.Transparent,
                            focusedContainerColor = Color.Transparent,
                        ),
                    modifier =
                        Modifier.menuAnchor()
                            .fillMaxWidth()
                            .border(1.dp, Color.Gray, RoundedCornerShape(10.dp)),
                )
                ExposedDropdownMenu(
                    expanded = isExpandedForExperience,
                    onDismissRequest = { isExpandedForExperience = false },
                ) {
                    yearOfExperience.forEachIndexed { index, text ->
                        DropdownMenuItem(
                            text = { Text(text = text) },
                            onClick = {
                                yearOfExperienceChoiceState = yearOfExperience[index]
                                isExpandedForExperience = false
                            },
                        )
                    }
                }
            }
            Text(text = "Degrees and certificates", Modifier.padding(top = 10.dp))
            // same as top
            ExposedDropdownMenuBox(
                expanded = isExpandedForDegree,
                onExpandedChange = { isExpandedForDegree = !isExpandedForDegree },
            ) {
                TextField(
                    value = degreeAndCertificateChoiceState,
                    onValueChange = { degreeAndCertificateChoiceState = it },
                    readOnly = true,
                    trailingIcon = {
                        ExposedDropdownMenuDefaults.TrailingIcon(expanded = isExpandedForDegree)
                    },
                    colors =
                        ExposedDropdownMenuDefaults.textFieldColors(
                            unfocusedContainerColor = Color.Transparent,
                            focusedContainerColor = Color.Transparent,
                        ),
                    modifier =
                        Modifier.menuAnchor()
                            .fillMaxWidth()
                            .border(1.dp, Color.Gray, RoundedCornerShape(10.dp)),
                )
                ExposedDropdownMenu(
                    expanded = isExpandedForDegree,
                    onDismissRequest = { isExpandedForDegree = false },
                ) {
                    degreeAndCertificate.forEachIndexed { index, text ->
                        DropdownMenuItem(
                            text = { Text(text = text) },
                            onClick = {
                                degreeAndCertificateChoiceState = degreeAndCertificate[index]
                                isExpandedForDegree = false
                            },
                        )
                    }
                }
            }

            Text(text = "Rating", Modifier.padding(top = 10.dp))
            // implement stars that can filled

            Text(text = "Hourly Rate", Modifier.padding(top = 10.dp))
            // implement slider

        }
    }
}

@Composable
fun DayBickerButton(timeSelected: MutableList<String>, time: String) {
    var selected by remember { mutableStateOf(false) }
    val color = if (selected) lightGreen else Color.White
    Button(
        onClick = {
            selected = !selected
            timeSelected.add(time)
        },
        shape = RoundedCornerShape(10.dp),
        colors = ButtonDefaults.buttonColors(containerColor = color),
        modifier = Modifier.padding(5.dp),
    ) {
        Text(
            text = time,
            style = MaterialTheme.typography.bodyMedium,
            color = Color.Black,
            fontSize = 12.sp,
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ScaffoldFilterScreenTopBar(navController: NavController, route: String) {
    TopAppBar(
        title = {
            Column(modifier = Modifier.padding(20.dp)) {
                Row(modifier = Modifier.fillMaxWidth()) {
                    Button(onClick = { navController.navigate(route) }) {
                        Image(
                            painter = painterResource(id = R.drawable.move_back),
                            contentDescription = null,
                        )
                    }
                    Text(
                        text = "Filters",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight(400),
                    )
                }
            }
        }
    )
}
