package com.example.student_project.ui.screen.details

import android.widget.Toast
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.student_project.data.model.Course
import com.example.student_project.data.repo.CourseRepo

@Composable
fun CourseDetailsScreen(navController: NavController, courseId: String?,courseRepo: CourseRepo) {
    var courseDetailsState by remember { mutableStateOf<Result<Course?>?>(null) }
    val scope = rememberCoroutineScope()
    val context = LocalContext.current


    LaunchedEffect(scope) {
        val courseDetails = courseRepo.getFullCourseDetails(courseId.toString())
        courseDetailsState = courseDetails
    }
    Column (modifier = Modifier.fillMaxSize()){
        Text(text = courseId.toString())
        courseDetailsState?.onSuccess { course->
            Text(text = course?.courseName.toString(), fontSize = 40.sp)
        }?.onFailure {
            Toast.makeText(context,"failed to load data" , Toast.LENGTH_SHORT).show()
        }
    }
}
