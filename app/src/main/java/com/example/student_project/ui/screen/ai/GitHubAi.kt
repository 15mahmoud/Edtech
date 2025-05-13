package com.example.student_project.ui.screen.ai

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
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
import com.example.student_project.data.model.Allin
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
fun GitHubAiScreen(navController: NavController, studentRepo: StudentRepo) {
    val selectedItemIndex by rememberSaveable { mutableIntStateOf(2) }
    var expanded by remember {
        mutableStateOf(false)
    }

    val configuration = LocalConfiguration.current
    val screenWidth = configuration.screenWidthDp.dp
    val screenHeight = configuration.screenHeightDp.dp

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
            Column {
                TopAppBar(title = {
                    Text(text = "GithubAi")
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
        ) {

            LazyColumn {
                aiResult?.onSuccess { results ->
                    items(results.tutorialFiles) { result ->
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
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .imePadding()
                    .align(alignment = Alignment.BottomCenter)
            ) {
                TextField(
                    value = aiPromptTextField,
                    textStyle = MaterialTheme.typography.titleMedium.copy(
                        fontSize = 15.sp, color = buttonColor
                    ),
                    onValueChange = {
                        aiPromptTextField = it

//                            singleLine.value = !it.contains('\n') //This one useless
                    },
//                        singleLine = singleLine.value,
                    modifier =
                    Modifier
                        .padding(start = Constant.paddingComponentFromScreen)
                        .width(screenWidth * 81 / 100)
                        .heightIn(min = 20.dp, max = 120.dp)
//                                .height(screenHeight * 5 / 100)
//                                .align(alignment = Alignment.CenterVertically)
                        .shadow(
                            elevation = 6.dp,
                            shape = MaterialTheme.shapes.small,
                            ambientColor = Color.Gray,
                            spotColor = Color.LightGray,
                        ),
                    //  .shadow(elevation = 2.dp, ambientColor = Color.Gray),
                    placeholder = {
                        Text(
                            text = "Enter Github link ...",
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
                        containerColor = buttonColor
                    ), onClick = {
                        CoroutineScope(Dispatchers.IO).launch {
                            aiResult = studentRepo.codeExplainer(aiPromptTextField)
                        }
                    }) {
                    Icon(
                        modifier = Modifier
                            .width(screenWidth * 8 / 100)
                            .height(screenHeight * 6 / 100)
                            .padding(Constant.smallPadding),
                        imageVector = Icons.AutoMirrored.Filled.Send,
                        tint = Color.White,
                        contentDescription = "send message"
                    )
                }
            }
        }
    }
}