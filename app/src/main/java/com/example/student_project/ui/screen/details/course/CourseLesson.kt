package com.example.student_project.ui.screen.details.course

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController

@Composable
fun CourseLessonScreen(navController: NavController, videoUrl: String?){
Text(text = videoUrl.toString(), fontSize = 22.sp)
}