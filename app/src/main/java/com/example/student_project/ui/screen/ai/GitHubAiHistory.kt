package com.example.student_project.ui.screen.ai

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.student_project.data.model.Allin
import com.example.student_project.data.repo.StudentRepo
import com.example.student_project.ui.screen.home.content.BottomNavBar

@Composable
fun GitHubAiHistoryScreen(navController: NavController, studentRepo: StudentRepo) {
    val selectedItemIndex by rememberSaveable { mutableStateOf(2) }

    val context = LocalContext.current

    var aiHistoryResult by remember {
        mutableStateOf<Result<List<Allin>?>?>(null)
    }

    val scope = rememberCoroutineScope()

//    var aiPromptTextField by remember {
//        mutableStateOf("")
//    }
    LaunchedEffect(scope) {
        aiHistoryResult = studentRepo.codeExplainerHistory()
    }
    Scaffold(
        Modifier
            .fillMaxSize()
            .background(Color.White),
        topBar = {
            Row(modifier = Modifier.fillMaxWidth()) {
//                TextField(
//                    modifier = Modifier.width(200.dp),
//                    value = aiPromptTextField,
//                    onValueChange = {
//                        aiPromptTextField = it
//                    }
//                )
//                Button(modifier = Modifier.size(20.dp), onClick = {
//                    CoroutineScope(Dispatchers.IO).launch {
//                        aiResult = studentRepo.codeExplainer(aiPromptTextField)
//                    }
//                }) {
//                    Text(text = "enter")
//                }
            }
        },
        bottomBar = { BottomNavBar(selectedItemIndex, navController) },
    ) { innerPadding ->
        LazyColumn(
            Modifier
                .padding(innerPadding)
                .fillMaxSize()
        ) {
            aiHistoryResult?.onSuccess { result ->
                result?.let { results ->
                    results.forEach { data ->
                        item(data.repoUrl){
                            Text(text = data.repoUrl)
                        }
                        items(data.tutorialFiles) { result ->
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
            }?.onFailure {
              Toast.makeText(context, "can't load data", Toast.LENGTH_SHORT).show()
            }
        }
    }
}