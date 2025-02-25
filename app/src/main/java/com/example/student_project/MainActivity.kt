package com.example.student_project

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.example.student_project.data.model.Instructor
import com.example.student_project.data.repo.CourseRepo
import com.example.student_project.data.repo.InstructorRepo
import com.example.student_project.data.repo.StudentRepo
import com.example.student_project.ui.navigation.Navigation
import com.example.student_project.ui.theme.StudentProjectTheme
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    @Inject lateinit var studentRepo: StudentRepo

    @Inject lateinit var courseRepo: CourseRepo

    @Inject lateinit var instructorRepo: InstructorRepo

    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge()
        installSplashScreen()
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent { StudentProjectTheme { Navigation(DepContainer(studentRepo, courseRepo,instructorRepo)) } }
    }
}

class DepContainer(var studentRepo: StudentRepo, var courseRepo: CourseRepo,var instructorRepo: InstructorRepo)
