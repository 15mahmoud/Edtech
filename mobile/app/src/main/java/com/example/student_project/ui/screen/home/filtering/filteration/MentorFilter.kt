package com.example.student_project.ui.screen.home.filtering.filteration

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.student_project.ui.navigation.Screens
import com.example.student_project.ui.screen.widgets.BickerButton
import com.example.student_project.ui.screen.widgets.ScaffoldFilterScreenTopBar
import com.example.student_project.ui.theme.buttonColor
import com.example.student_project.ui.theme.darkerGrayColor

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MentorFilterScreen(navController: NavController) {
    val major = listOf("MATH 116", "ARCH 117", "other")
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
    var selectedRating by remember { mutableStateOf(0.0) }
    var hourlyRate by remember { mutableStateOf(20.0) }

    //
    val daySelected = mutableListOf<String?>()

    val timeSlotSelected = mutableListOf<String?>()

    Scaffold(
        modifier = Modifier.padding(16.dp),
        topBar = { ScaffoldFilterScreenTopBar(navController = navController, "Filters") },
        bottomBar = {
            NavigationBar {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(10.dp)
                        .background(Color.Transparent),
                    horizontalArrangement =
                    Arrangement.spacedBy(8.dp, alignment = Alignment.CenterHorizontally),
                ) {
                    Button(
                        modifier = Modifier.weight(0.4f),
                        border = BorderStroke(1.dp, Color.Gray),
                        shape = RoundedCornerShape(120.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent),
                        onClick = { navController.navigate(Screens.MentorFilterScreen.route) },
                    ) {
                        Text(
                            text = "Reset Filter",
                            style = MaterialTheme.typography.titleMedium,
                            color = buttonColor,
                        )
                    }
                    Button(
                        modifier = Modifier.weight(0.4f),
                        shape = RoundedCornerShape(120.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = buttonColor),
                        onClick = {
                            // filled object first then we will send it to TopMentor screen
                            // var filterationRequest = FilterationRequest()
                            // note that every arg must have /
                            // its like web url
                            navController.navigate(
                                Screens.MentorFilterResultScreen.route +
                                        "/$majorChoiceState" +
                                        "/$selectedRating" +
                                        "/$hourlyRate"
                            )
                            // navigate with arguments "filteration object" to search result
                        },
                    ) {
                        Text(text = "Apply Filter", style = MaterialTheme.typography.titleMedium)
                    }
                }
            }
        },
    ) { innerPadding ->
        Column(
            Modifier
                .padding(innerPadding)
                .verticalScroll(rememberScrollState())) {
            Text(
                text = "I'm looking for",
                Modifier.padding(10.dp),
                fontSize = 14.sp,
                style = MaterialTheme.typography.headlineLarge,
                color = buttonColor,
            )
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement =
                Arrangement.spacedBy(8.dp, alignment = Alignment.CenterHorizontally),
            ) {
                Button(
                    modifier = Modifier.weight(0.4f),
                    border = BorderStroke(1.dp, Color.Gray),
                    shape = RoundedCornerShape(120.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = buttonColor),
                    onClick = {},
                ) {
                    Text(
                        text = "Tutor",
                        color = darkerGrayColor,
                        fontSize = 14.sp,
                        fontWeight = FontWeight(500),
                        style = MaterialTheme.typography.titleMedium,
                    )
                }
                Button(
                    modifier = Modifier.weight(0.4f),
                    border = BorderStroke(1.dp, Color.Gray),
                    shape = RoundedCornerShape(120.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent),
                    onClick = { navController.navigate(Screens.CourseFilterScreen.route) },
                ) {
                    Text(
                        text = "Course",
                        fontSize = 14.sp,
                        fontWeight = FontWeight(500),
                        color = darkerGrayColor,
                        style = MaterialTheme.typography.titleMedium,
                    )
                }
            }

            Text(
                text = "Job Title",
                Modifier.padding(10.dp),
                fontSize = 14.sp,
                style = MaterialTheme.typography.headlineLarge,
                color = buttonColor,
            )

            ExposedDropdownMenuBox(
                modifier = Modifier,
                //                .border(
                //                width = 1.dp, color = Color.Gray,
                //                RoundedCornerShape(10.dp)
                //            )
                expanded = isExpandedForMajor,
                onExpandedChange = { isExpandedForMajor = !isExpandedForMajor },
            ) {
                // here we will make stack layout and we will put BasicTextField
                // and we will but icon that indicate to dropDownMenuBox
                TextField(
                    value = majorChoiceState,
                    onValueChange = { majorChoiceState = it },
                    readOnly = true,
                    trailingIcon = {
                        ExposedDropdownMenuDefaults.TrailingIcon(expanded = isExpandedForMajor)
                    },
                    colors =
                    ExposedDropdownMenuDefaults.textFieldColors(
                        unfocusedContainerColor = Color.Green,
                        focusedContainerColor = Color.Transparent,
                    ),
                    modifier = Modifier
                        .menuAnchor()
                        .fillMaxWidth(),
                    //                        .border(
                    //                            6.dp, Color.Green,
                    //                            RoundedCornerShape(10.dp)
                    //                        )

                )
                ExposedDropdownMenu(
                    modifier =
                    Modifier.border(
                        width = 1.dp,
                        color = Color.Gray,
                        RoundedCornerShape(10.dp),
                    ),
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

            Text(
                text = "Availability",
                Modifier.padding(10.dp),
                fontSize = 14.sp,
                style = MaterialTheme.typography.headlineLarge,
                color = buttonColor,
            )
            // implement lazy row for days ass button
            Row(modifier = Modifier
                .fillMaxWidth()
                .horizontalScroll(rememberScrollState())) {
                BickerButton(timeSlot = "Saturday", onClick = { s -> daySelected.add(s) })

                BickerButton(timeSlot = "Sunday", onClick = { s -> daySelected.add(s) })

                BickerButton(timeSlot = "Monday", onClick = { s -> daySelected.add(s) })

                BickerButton(timeSlot = "Tuesday", onClick = { s -> daySelected.add(s) })

                BickerButton(timeSlot = "Wednesday", onClick = { s -> daySelected.add(s) })

                BickerButton(timeSlot = "Thursday", onClick = { s -> daySelected.add(s) })

                BickerButton(timeSlot = "Friday", onClick = { s -> daySelected.add(s) })
            }
            Text(
                text = "Time slot",
                Modifier.padding(10.dp),
                fontSize = 14.sp,
                style = MaterialTheme.typography.headlineLarge,
                color = buttonColor,
            )
            // same as the top one
            Row(modifier = Modifier
                .fillMaxWidth()
                .horizontalScroll(rememberScrollState())) {
                BickerButton(timeSlot = "Morning", onClick = { s -> timeSlotSelected.add(s) })

                BickerButton(timeSlot = "Afternoon", onClick = { s -> timeSlotSelected.add(s) })

                BickerButton(timeSlot = "Evening", onClick = { s -> timeSlotSelected.add(s) })
            }

            Text(
                text = "Tutoring Experience",
                Modifier.padding(10.dp),
                fontSize = 14.sp,
                style = MaterialTheme.typography.headlineLarge,
                color = buttonColor,
            )
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
                    Modifier
                        .menuAnchor()
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
            Text(
                text = "Degrees and certificates",
                Modifier.padding(10.dp),
                fontSize = 14.sp,
                style = MaterialTheme.typography.headlineLarge,
                color = buttonColor,
            )
            // same as top
            // drop down box
            ExposedDropdownMenuBox(
                modifier =
                Modifier.border(width = 1.dp, color = Color.Gray, RoundedCornerShape(10.dp)),
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
                    Modifier
                        .menuAnchor()
                        .fillMaxWidth()
                        .border(1.dp, Color.Gray, RoundedCornerShape(10.dp)),
                )
                ExposedDropdownMenu(
                    modifier =
                    Modifier.border(
                        width = 1.dp,
                        color = Color.Gray,
                        RoundedCornerShape(10.dp),
                    ),
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

            Text(
                text = "Rating",
                Modifier.padding(10.dp),
                fontSize = 14.sp,
                style = MaterialTheme.typography.headlineLarge,
                color = buttonColor,
            )
            // implement stars that can filled
            Row(Modifier.padding(top = 8.dp), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                (1..5).forEach { number ->
                    IconButton(onClick = { selectedRating = number.toDouble() }) {
                        Icon(
                            imageVector = Icons.Filled.Star,
                            contentDescription = "Rating Star",
                            tint =
                            if (number.toDouble() <= selectedRating) Color.Yellow
                            else Color.Gray,
                        )
                    }
                }
                // we will delete this text
                Text(
                    modifier = Modifier.padding(start = 8.dp, top = 12.dp),
                    text = "$selectedRating",
                    fontSize = 14.sp,
                    color = Color.Gray,
                )
            }

            Text(
                text = "Hourly Rate",
                Modifier.padding(10.dp),
                fontSize = 14.sp,
                style = MaterialTheme.typography.headlineLarge,
                color = buttonColor,
            )

            // implement slider
            // Hourly Rate Section
            Column {
                Slider(
                    thumb = {
                        Spacer(
                            modifier =
                            Modifier
                                .size(20.dp)
                                .background(Color.White, CircleShape)
                                .border(width = 2.dp, color = buttonColor, shape = CircleShape)
                        )
                    },
                    modifier = Modifier.padding(start = 10.dp),
                    value = hourlyRate.toFloat(),
                    onValueChange = { hourlyRate = it.toDouble() },
                    valueRange = 0f..100f,
                    colors =
                    SliderDefaults.colors(
                        thumbColor = buttonColor,
                        activeTrackColor = buttonColor,
                    ),
                )
                Text(text = "${hourlyRate} USD/hour", fontSize = 14.sp, color = Color.Gray)
            }

            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}

// we will change this to lambda fun that will take string and will return -> string
// then take the returning string and put it in a a list
