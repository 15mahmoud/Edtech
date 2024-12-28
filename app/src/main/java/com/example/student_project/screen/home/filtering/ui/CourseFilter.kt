package com.example.student_project.screen.home.filtering.ui

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.student_project.navigation.Screens
import com.example.student_project.screen.home.filtering.constant.ScaffoldFilterScreenTopBar
import com.example.student_project.ui.theme.buttonColor
import com.example.student_project.ui.theme.darkerGrayColor

@Composable
fun CourseFilterScreen(navController: NavController) {
    Scaffold(
        topBar = { ScaffoldFilterScreenTopBar(navController = navController) },
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
        }
    }
}
