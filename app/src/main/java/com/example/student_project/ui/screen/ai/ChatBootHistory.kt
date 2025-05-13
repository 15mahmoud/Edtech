package com.example.student_project.ui.screen.ai

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.navigation.NavController
import com.example.student_project.R
import com.example.student_project.data.model.AiChatHistoryResponse
import com.example.student_project.data.repo.StudentRepo
import com.example.student_project.ui.navigation.Screens
import com.example.student_project.ui.screen.home.content.BottomNavBar

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChatBootHistoryScreen(navController: NavController, studentRepo: StudentRepo){

    val selectedItemIndex by rememberSaveable { mutableIntStateOf(2) }
    var expanded by remember {
        mutableStateOf(false)
    }

    var aiHistoryState by remember {
        mutableStateOf<Result<List<AiChatHistoryResponse>?>?>(null)
    }

    val scope = rememberCoroutineScope()

    LaunchedEffect (scope){
        aiHistoryState = studentRepo.getAiChatHistory()
    }

    Scaffold(
        Modifier
            .fillMaxSize()
            .background(Color.White),
        topBar = {
            TopAppBar(title = {
                Text(text = "GitHubApi")
            },
                actions = {
                    IconButton(onClick = {expanded  = !expanded}) {
                        Icon(
                            imageVector = ImageVector.vectorResource(id = R.drawable.ai_baseline_menu_24),
                            contentDescription = "menu button"
                        )
                    }
                    DropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
                        DropdownMenuItem(text = {
                            Text(text = "GithubAi")
                        }, onClick = {
                            navController.navigate(Screens.GitHubAiScreen.route)
                        })
                        DropdownMenuItem(text = { Text(text = "ChatBoot") }, onClick = {
                            navController.navigate(Screens.ChatBootScreen.route)
                        })
                    }
                })
        },
        bottomBar = { BottomNavBar(selectedItemIndex, navController) },
    ){innerPadding->

    }
}