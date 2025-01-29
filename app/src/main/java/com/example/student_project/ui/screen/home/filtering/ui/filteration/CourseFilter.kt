package com.example.student_project.ui.screen.home.filtering.ui.filteration

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
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
import com.example.student_project.screen.home.filtering.widgets.CourseFilterScreenText
import com.example.student_project.screen.home.filtering.widgets.ScaffoldFilterScreenTopBar
import com.example.student_project.ui.theme.borderRating
import com.example.student_project.ui.theme.buttonColor
import com.example.student_project.ui.theme.darkerGrayColor

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CourseFilterScreen(navController: NavController) {
    val courseCategory = listOf("programming", "marketing", "other")
    var isExpandedForCourseCategory by remember { mutableStateOf(false) }
    var courseCategoryChoiceState by remember { mutableStateOf("programming") }
    val difficultyLevel = listOf("beginner", "intermediate", "advanced")
    var isExpandedForDifficultyLevel by remember { mutableStateOf(false) }
    var difficultyLevelChoiceState by remember { mutableStateOf("beginner") }
    val releasedDate = listOf("inLessThan3Months", "inLessThan6Months", "inLessThanOneYear")
    var isExpandedForReleasedDate by remember { mutableStateOf(false) }
    var releasedDateChoiceState by remember { mutableStateOf("inLessThan3Months") }
    var selectedRating by remember { mutableStateOf(0.0) }
    var hourlyRate by remember { mutableStateOf(20.0) }
    Scaffold(
        modifier = Modifier.padding(16.dp),
        topBar = { ScaffoldFilterScreenTopBar(navController = navController, "Filters") },
        bottomBar = {
            NavigationBar {
                Row(
                    modifier = Modifier.fillMaxWidth().padding(10.dp),
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
                            // filled object first then we will send it to TopCourse screen
                            // var filterationRequest = FilterationRequest()
                            navController.navigate(
                                Screens.CourseFilterResultScreen.route +
                                    "/$courseCategoryChoiceState" +
                                    "/$difficultyLevelChoiceState" +
                                    "/$releasedDateChoiceState" +
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
        Column(Modifier.padding(innerPadding).verticalScroll(rememberScrollState())) {
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
                    colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent),
                    onClick = { navController.navigate(Screens.MentorFilterScreen.route) },
                ) {
                    Text(
                        text = "Tutor",
                        fontSize = 14.sp,
                        fontWeight = FontWeight(500),
                        color = darkerGrayColor,
                        style = MaterialTheme.typography.titleMedium,
                    )
                }
                Button(
                    modifier = Modifier.weight(0.4f),
                    border = BorderStroke(1.dp, Color.Gray),
                    shape = RoundedCornerShape(120.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = buttonColor),
                    onClick = {},
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
            CourseFilterScreenText(text = "Course Category")

            ExposedDropdownMenuBox(
                modifier =
                    Modifier.border(width = 1.dp, color = Color.Gray, RoundedCornerShape(10.dp)),
                expanded = isExpandedForCourseCategory,
                onExpandedChange = { isExpandedForCourseCategory = !isExpandedForCourseCategory },
            ) {
                TextField(
                    value = courseCategoryChoiceState,
                    onValueChange = { courseCategoryChoiceState = it },
                    readOnly = true,
                    trailingIcon = {
                        ExposedDropdownMenuDefaults.TrailingIcon(
                            expanded = isExpandedForCourseCategory
                        )
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
                    modifier =
                        Modifier.border(
                            width = 1.dp,
                            color = Color.Gray,
                            RoundedCornerShape(10.dp),
                        ),
                    expanded = isExpandedForCourseCategory,
                    onDismissRequest = { isExpandedForCourseCategory = false },
                ) {
                    courseCategory.forEachIndexed { index, text ->
                        DropdownMenuItem(
                            text = { Text(text = text) },
                            onClick = {
                                courseCategoryChoiceState = courseCategory[index]
                                isExpandedForCourseCategory = false
                            },
                        )
                    }
                }
            }
            CourseFilterScreenText(text = "Difficulty Level")

            ExposedDropdownMenuBox(
                modifier =
                    Modifier.border(width = 1.dp, color = Color.Gray, RoundedCornerShape(10.dp)),
                expanded = isExpandedForDifficultyLevel,
                onExpandedChange = { isExpandedForDifficultyLevel = !isExpandedForDifficultyLevel },
            ) {
                TextField(
                    value = difficultyLevelChoiceState,
                    onValueChange = { difficultyLevelChoiceState = it },
                    readOnly = true,
                    trailingIcon = {
                        ExposedDropdownMenuDefaults.TrailingIcon(
                            expanded = isExpandedForDifficultyLevel
                        )
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
                    modifier =
                        Modifier.border(
                            width = 1.dp,
                            color = Color.Gray,
                            RoundedCornerShape(10.dp),
                        ),
                    expanded = isExpandedForDifficultyLevel,
                    onDismissRequest = { isExpandedForDifficultyLevel = false },
                ) {
                    difficultyLevel.forEachIndexed { index, text ->
                        DropdownMenuItem(
                            text = { Text(text = text) },
                            onClick = {
                                difficultyLevelChoiceState = difficultyLevel[index]
                                isExpandedForDifficultyLevel = false
                            },
                        )
                    }
                }
            }
            CourseFilterScreenText(text = "Rating")

            Row(
                Modifier.padding(top = 8.dp)
                    .border(width = 1.dp, color = borderRating, shape = RoundedCornerShape(10.dp)),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
            ) {
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
                    modifier = Modifier.padding(start = 8.dp, top = 12.dp, end = 5.dp),
                    text = "$selectedRating",
                    fontSize = 14.sp,
                    color = Color.Gray,
                )
            }
            CourseFilterScreenText(text = "Hourly Rate")

            Column {
                Slider(
                    thumb = {
                        Spacer(
                            modifier =
                                Modifier.size(20.dp)
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

            CourseFilterScreenText(text = "Released")

            ExposedDropdownMenuBox(
                modifier =
                    Modifier.border(width = 1.dp, color = Color.Gray, RoundedCornerShape(10.dp)),
                expanded = isExpandedForReleasedDate,
                onExpandedChange = { isExpandedForReleasedDate = !isExpandedForReleasedDate },
            ) {
                TextField(
                    value = releasedDateChoiceState,
                    onValueChange = { releasedDateChoiceState = it },
                    readOnly = true,
                    trailingIcon = {
                        ExposedDropdownMenuDefaults.TrailingIcon(
                            expanded = isExpandedForReleasedDate
                        )
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
                    modifier =
                        Modifier.border(
                            width = 1.dp,
                            color = Color.Gray,
                            RoundedCornerShape(10.dp),
                        ),
                    expanded = isExpandedForReleasedDate,
                    onDismissRequest = { isExpandedForReleasedDate = false },
                ) {
                    releasedDate.forEachIndexed { index, text ->
                        DropdownMenuItem(
                            text = { Text(text = text) },
                            onClick = {
                                releasedDateChoiceState = releasedDate[index]
                                isExpandedForReleasedDate = false
                            },
                        )
                    }
                }
            }
        }
    }
}
