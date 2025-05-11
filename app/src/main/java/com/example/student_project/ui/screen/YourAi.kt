package com.example.student_project.ui.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.student_project.data.model.Allin
import com.example.student_project.data.repo.StudentRepo
import com.example.student_project.ui.screen.home.content.BottomNavBar
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Composable
fun YourAiScreen(navController: NavController, studentRepo: StudentRepo) {
    val selectedItemIndex by rememberSaveable { mutableStateOf(2) }

    var aiResult by remember {
        mutableStateOf<Result<Allin>?>(null)
    }

    var aiPromptTextField by remember {
        mutableStateOf("")
    }
//    LaunchedEffect {
//        aiResult = studentRepo.codeExplainer()
//    }
    Scaffold(
        Modifier
            .fillMaxSize()
            .background(Color.White),
        topBar = {
            Row(modifier = Modifier.fillMaxWidth()) {
                TextField(
                    modifier = Modifier.width(200.dp),
                    value = aiPromptTextField,
                    onValueChange = {
                        aiPromptTextField = it
                    }
                )
                Button(modifier = Modifier.size(20.dp), onClick = {
                    CoroutineScope(Dispatchers.IO).launch {
                        aiResult = studentRepo.codeExplainer(aiPromptTextField)
                    }
                }) {
                    Text(text = "enter")
                }
            }
        },
        bottomBar = { BottomNavBar(selectedItemIndex, navController) },
    ) { innerPadding ->
        LazyColumn(
            Modifier
                .padding(innerPadding)
                .fillMaxSize()
        ) {
            aiResult?.onSuccess { result ->
                result.let { results ->
                    items(results.tutorialFiles) { result ->
                        Text(
                            text = result.fileName,
                            fontSize = 15.sp,
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight(600)
                        )
                        Text(text = result.content, fontSize = 12.sp)
                    }
                }
            }
        }
    }
}