package com.example.student_project.ui.screen.ai

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
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
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.student_project.R
import com.example.student_project.data.model.AiChatHistoryResponse
import com.example.student_project.data.repo.StudentRepo
import com.example.student_project.ui.navigation.Screens
import com.example.student_project.ui.screen.home.content.BottomNavBar
import com.example.student_project.ui.theme.aiBoxColor
import com.example.student_project.ui.theme.buttonColor
import com.example.student_project.ui.theme.jopTitleColor
import com.example.student_project.util.Constant
import com.example.student_project.util.Utils

@SuppressLint("NewApi")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChatBootHistoryScreen(navController: NavController, studentRepo: StudentRepo) {

    val selectedItemIndex by rememberSaveable { mutableIntStateOf(2) }
    var expanded by remember {
        mutableStateOf(false)
    }

    var aiHistoryState by remember {
        mutableStateOf<Result<List<AiChatHistoryResponse>?>?>(null)
    }

    val scope = rememberCoroutineScope()

    LaunchedEffect(scope) {
        aiHistoryState = studentRepo.getAiChatHistory()
    }

    Scaffold(
        Modifier
            .fillMaxSize()
            .background(Color.White),
        topBar = {
            Column {
                TopAppBar(modifier = Modifier.background(Color.White), title = {
                    Text(text = "ChatAiHistory")
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
                    ai = "ChatBoot",
                    history = "History",
                    onAiClick = {
                        navController.navigate(Screens.ChatBootScreen.route)
                    },
                    onHistoryClick = {
                        navController.navigate(Screens.ChatBootHistoryScreen.route)
                    }, aiButtonSelected = false, historySelected = true

                )

            }
        },
        bottomBar = { BottomNavBar(selectedItemIndex, navController) },
    ) { innerPadding ->
        Box(
            Modifier
                .padding(innerPadding)
                .fillMaxSize()
                .background(color = buttonColor)
        ) {
            LazyColumn {
                aiHistoryState?.onSuccess { nullableHistory ->
                    nullableHistory?.let { notNullableHistory ->
                        items(notNullableHistory) { history ->
                            Box(
                                modifier = Modifier
                                    .padding(
                                        top = Constant.smallPadding,
                                        start = Constant.normalPadding,
                                        end = Constant.normalPadding,
                                        bottom = Constant.smallPadding
                                    )
                                    .clip(RoundedCornerShape(12.dp))
                                    .border(
                                        2.dp,
                                        color = when {
                                            history.role == "user" -> aiBoxColor
                                            else -> buttonColor
                                        },
                                        shape = RoundedCornerShape(12.dp)
                                    )
                                    .background(
                                        color = when {
                                            history.role == "user" -> buttonColor
                                            else -> aiBoxColor
                                        }
                                    )
                            ) {
                                Column {

                                    Text(
                                        modifier = Modifier.padding(
                                            start = Constant.smallPadding,
                                            end = Constant.smallPadding,
                                            top = Constant.smallPadding,
//                                            bottom = Constant.smallPadding
                                        ),
                                        text = history.content,
                                        color = when {
                                            history.role == "user" -> aiBoxColor
                                            else -> buttonColor
                                        },
                                        style = MaterialTheme.typography.titleMedium,
                                        fontWeight = FontWeight(400)
                                    )
                                    Text(
                                        modifier = Modifier
                                            .padding(
                                                start = Constant.smallPadding,
                                                end = Constant.smallPadding,
//                                                top = Constant.paddingTextFromText,
                                                bottom = Constant.smallPadding
                                            )
                                            .align(Alignment.End),
                                        text = Utils.fromDateToTime(
                                            history.timestamp,
                                            "d MMM hh:mm"
                                        ),
                                        color = when {
                                            history.role == "user" -> Color.White
                                            else -> jopTitleColor
                                        },
                                        style = MaterialTheme.typography.titleMedium,
                                        fontWeight = FontWeight(400)
                                    )
                                }
                            }
                        }
                    }
                }?.onFailure {
                    item {
                        Text(
                            modifier = Modifier.padding(
                                start = Constant.smallPadding,
                                end = Constant.smallPadding
                            ),
                            text = "Failed can't load due to " + it.message.toString(),
                            color = buttonColor,
                            fontSize = 15.sp,
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight(600)
                        )
                    }
                }
            }
        }
    }
}