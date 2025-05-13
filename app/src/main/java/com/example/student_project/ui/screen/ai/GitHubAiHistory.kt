package com.example.student_project.ui.screen.ai

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.student_project.R
import com.example.student_project.data.model.Allin
import com.example.student_project.data.repo.StudentRepo
import com.example.student_project.ui.navigation.Screens
import com.example.student_project.ui.screen.home.content.BottomNavBar
import com.example.student_project.ui.theme.aiBoxColor
import com.example.student_project.ui.theme.buttonColor
import com.example.student_project.ui.theme.jopTitleColor
import com.example.student_project.util.Constant

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GitHubAiHistoryScreen(navController: NavController, studentRepo: StudentRepo) {
    val selectedItemIndex by rememberSaveable { mutableIntStateOf(2) }

    val context = LocalContext.current


    var aiHistoryResult by remember {
        mutableStateOf<Result<List<Allin>?>?>(null)
    }

    var expanded by remember {
        mutableStateOf(false)
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
            Column() {
                TopAppBar(modifier = Modifier.background(Color.White), title = {
                    Text(text = "GitHubAi")
                },
                    actions = {
                        IconButton(onClick = { expanded = !expanded }) {
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
                RowButtonToTransformFromAiToHistoryForGithubAi(
                    ai = "Ai",
                    history = "History",
                    onAiClick = {
                        navController.navigate(Screens.GitHubAiScreen.route)
                    },
                    onHistoryClick = {
                        navController.navigate(Screens.GitHubAiHistoryScreen.route)
                    }, aiButtonSelected = false, historySelected = true

                )

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
                        item(data.repoUrl) {
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(
                                        top = Constant.smallPadding,
                                        bottom = Constant.smallPadding,
                                        start = Constant.normalPadding,
                                        end = Constant.normalPadding
                                    )
                                    .clip(RoundedCornerShape(12.dp))
                                    .border(
                                        1.dp,
                                        color = Color.Gray,
                                        shape = RoundedCornerShape(12.dp)
                                    )
                                    .background(buttonColor)

                            ) {

                                Text(
                                    modifier = Modifier.padding(
                                        start = Constant.smallPadding,
                                        end = Constant.smallPadding
                                    ),
                                    text = data.repoUrl,
                                    color = Color.White,
                                    fontSize = 15.sp,
                                    style = MaterialTheme.typography.titleLarge,
                                    fontWeight = FontWeight(600)
                                )
                            }
                        }

                        items(data.tutorialFiles) { result ->
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(
                                        bottom = Constant.smallPadding,
                                        start = Constant.normalPadding,
                                        end = Constant.normalPadding
                                    )
                                    .clip(RoundedCornerShape(12.dp))
                                    .border(
                                        1.dp,
                                        color = Color.Gray,
                                        shape = RoundedCornerShape(12.dp)
                                    )
                                    .background(buttonColor)

                            ) {
                                Text(
                                    modifier = Modifier
                                        .padding(
                                            start = Constant.smallPadding,
                                            end = Constant.smallPadding
                                        )
                                        .align(Alignment.Center),
                                    text = result.fileName,
                                    color = Color.White,
                                    fontSize = 15.sp,
                                    style = MaterialTheme.typography.titleLarge,
                                    fontWeight = FontWeight(600)
                                )
                            }
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(
                                        bottom = Constant.smallPadding,
                                        start = Constant.normalPadding,
                                        end = Constant.normalPadding
                                    )
                                    .clip(RoundedCornerShape(12.dp))
                                    .border(
                                        1.dp,
                                        color = Color.Gray,
                                        shape = RoundedCornerShape(12.dp)
                                    )
                                    .background(aiBoxColor)
                            ) {
                                Text(
                                    modifier = Modifier.padding(
                                        start = Constant.smallPadding,
                                        end = Constant.smallPadding
                                    ),
                                    text = result.content,
                                    color = buttonColor,
                                    fontSize = 15.sp,
                                    style = MaterialTheme.typography.titleLarge,
                                    fontWeight = FontWeight(600)
                                )
                            }
                        }
                    }
                }
            }?.onFailure {
                Toast.makeText(context, "can't load data", Toast.LENGTH_SHORT).show()
            }
        }
    }
}

@Composable
fun RowButtonToTransformFromAiToHistoryForGithubAi(
    ai: String,
    history: String,
    onAiClick: () -> Unit = {},
    onHistoryClick: () -> Unit = {},
    aiButtonSelected: Boolean,
    historySelected: Boolean
) {


    Row(modifier = Modifier.fillMaxWidth()) {
        Button(modifier = Modifier.weight(.5f),
            colors = ButtonDefaults.buttonColors(
                containerColor = if (aiButtonSelected) buttonColor else Color.White
            ),
            onClick = {
                onAiClick()
            }) {
            Text(text = ai, color = if (aiButtonSelected) Color.White else buttonColor)
        }
        Button(modifier = Modifier.weight(.5f), colors = ButtonDefaults.buttonColors(
            containerColor = if (historySelected) buttonColor else Color.White
        ), onClick = {
            onHistoryClick()
        }) {
            Text(text = history, color = if (historySelected) Color.White else buttonColor)
        }
    }

}