package com.example.student_project.ui.screen.details

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController

@Composable
fun MentorDetailsScreen(
    navController: NavController,
    instructorId: String?,
){
Text(text = instructorId.toString(), fontSize = 25.sp)
}