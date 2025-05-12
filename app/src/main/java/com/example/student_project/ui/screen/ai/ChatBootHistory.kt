package com.example.student_project.ui.screen.ai

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.navigation.NavController
import com.example.student_project.data.model.AiChatHistoryResponse
import com.example.student_project.data.repo.StudentRepo

@Composable
fun ChatBootHistoryScreen(navController: NavController, studentRepo: StudentRepo){

    var aiHistoryState by remember {
        mutableStateOf<Result<List<AiChatHistoryResponse>?>?>(null)
    }
    val scope = rememberCoroutineScope()

    LaunchedEffect (scope){
        aiHistoryState = studentRepo.getAiChatHistory()
    }
}