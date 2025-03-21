package com.example.student_project

import android.os.Build
import android.os.Bundle
import android.view.View
import android.view.WindowInsetsController
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.annotation.RequiresApi
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.core.view.WindowInsetsCompat
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

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge()
        installSplashScreen()
        //this one is so important
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            // For Android 11 (API level 30) and higher
            window.insetsController?.let {
                it.hide(WindowInsetsCompat.Type.statusBars() or WindowInsetsCompat.Type.navigationBars())
                it.systemBarsBehavior = WindowInsetsController.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
            }
        } else {
            // For older Android versions (deprecated, but still works)
            @Suppress("DEPRECATION")
            window.decorView.systemUiVisibility = (View.SYSTEM_UI_FLAG_HIDE_NAVIGATION or View.SYSTEM_UI_FLAG_FULLSCREEN)
        }

        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent { StudentProjectTheme { Navigation(DepContainer(studentRepo, courseRepo,instructorRepo)) } }
    }
}

class DepContainer(var studentRepo: StudentRepo, var courseRepo: CourseRepo,var instructorRepo: InstructorRepo)
