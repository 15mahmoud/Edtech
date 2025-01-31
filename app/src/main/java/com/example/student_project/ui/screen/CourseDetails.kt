package com.example.student_project.ui.screen

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.navigation.NavController

@Composable
fun CourseDetailsScreen(navController: NavController, courseTitle: String?) {
    Text(text = courseTitle.toString())
}
