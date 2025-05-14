package com.example.student_project.ui.screen.ai

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.student_project.R
import com.example.student_project.data.repo.StudentRepo
import com.example.student_project.ui.navigation.Screens
import com.example.student_project.ui.screen.home.content.BottomNavBar
import com.example.student_project.ui.theme.aiBoxColor
import com.example.student_project.ui.theme.buttonColor
import com.example.student_project.ui.theme.textFieldColor
import com.example.student_project.util.Constant
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChatBootScreen(navController: NavController, studentRepo: StudentRepo) {

    val configuration = LocalConfiguration.current
    val screenWidth = configuration.screenWidthDp.dp
    val screenHeight = configuration.screenHeightDp.dp

    val selectedItemIndex by rememberSaveable { mutableIntStateOf(2) }
    var expanded by remember {
        mutableStateOf(false)
    }

    var chatBootResultState by remember {
        mutableStateOf<Result<String>?>(null)
    }

    var aiPromptTextField by remember {
        mutableStateOf("")
    }

    Scaffold(
        Modifier
            .fillMaxSize()
            .background(Color.White),
        topBar = {
            Column {
                TopAppBar(title = {
                    Text(text = "ChatAi")
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

                    }, aiButtonSelected = true, historySelected = false
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
                chatBootResultState?.onSuccess { chatBootResult ->
                    item(chatBootResult) {
                        Text(
                            modifier = Modifier.padding(
                                start = Constant.normalPadding,
                                end = Constant.normalPadding,
                                bottom = Constant.smallPadding,
                                top = Constant.smallPadding
                            ),
                            text = aiPromptTextField,
                            color = Color.White,
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight(500)
                        )
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
                                    color = buttonColor,
                                    shape = RoundedCornerShape(12.dp)
                                )
                                .background(aiBoxColor)
                        ) {
                            Text(
                                modifier = Modifier.padding(
                                    start = Constant.smallPadding,
                                    end = Constant.smallPadding,
                                    top = Constant.smallPadding,
                                    bottom = Constant.smallPadding
                                ),
                                text = chatBootResult,
                                color = Color.White,
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight(400)
                            )
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
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .align(alignment = Alignment.BottomCenter)
                    .consumeWindowInsets(innerPadding)
                    .imePadding()
            ) {
                TextField(
                    value = aiPromptTextField,
                    textStyle = MaterialTheme.typography.titleMedium.copy(
                        fontSize = 15.sp, color = buttonColor
                    ),
                    onValueChange = {
                        aiPromptTextField = it
                    },
                    modifier =
                    Modifier
                        .width(screenWidth * 85 / 100)
                        .heightIn(min = 20.dp, max = 120.dp)
                        .shadow(
                            elevation = 6.dp,
                            shape = MaterialTheme.shapes.small,
                            ambientColor = Color.Gray,
                            spotColor = Color.LightGray,
                        ),
                    placeholder = {
                        Text(
                            text = "Enter your message ...",
                            fontSize = 15.sp,
                            style = MaterialTheme.typography.headlineSmall,
                            color = Color.Gray,
                        )
                    },
                    colors =
                    TextFieldDefaults.colors(
                        unfocusedContainerColor = textFieldColor,
                        focusedContainerColor = textFieldColor,
                        unfocusedIndicatorColor = textFieldColor,
                        focusedIndicatorColor = textFieldColor,
                    ),
                )
                IconButton(modifier = Modifier
                    .height(screenHeight * 7 / 100)
                    .width(screenWidth * 14 / 100)
                    .align(
                        Alignment.CenterEnd
                    ),
                    colors = IconButtonDefaults.iconButtonColors(
                        containerColor = Color.White
                    ), onClick = {
                        CoroutineScope(Dispatchers.IO).launch {
                            chatBootResultState = studentRepo.createAiChat(aiPromptTextField)
                        }
                    }) {
                    Icon(
                        modifier = Modifier
                            .width(screenWidth * 8 / 100)
                            .height(screenHeight * 6 / 100)
                            .padding(Constant.smallPadding),
                        imageVector = Icons.AutoMirrored.Filled.Send,
                        tint = buttonColor,
                        contentDescription = "send message"
                    )
                }
            }

        }

    }
}