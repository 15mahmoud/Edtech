package com.example.student_project.ui.screen.ai

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.navigation.NavController
import com.example.student_project.data.repo.StudentRepo

@Composable
fun ChatBootScreen(navController: NavController, studentRepo: StudentRepo){

    var aiPromptState by remember {
        mutableStateOf<Result<String>?>(null)
    }

}