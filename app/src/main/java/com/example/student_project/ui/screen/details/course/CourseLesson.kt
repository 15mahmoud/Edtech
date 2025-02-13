package com.example.student_project.ui.screen.details.course

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import java.net.URLDecoder
import java.nio.charset.StandardCharsets

@Composable
fun CourseLessonScreen(navController: NavController, encodedUrl: String?) {

    val videoUrl = URLDecoder.decode(encodedUrl.toString(), StandardCharsets.UTF_8.toString())

    Text(text = videoUrl.toString(), fontSize = 22.sp)
}
