package com.example.student_project.ui.screen.inbox.chat

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Surface
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material.icons.filled.Call
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.student_project.data.model.Message
import com.example.student_project.data.repo.StudentRepo
import com.example.student_project.ui.theme.buttonColor
import com.example.student_project.ui.theme.chatReceiverColor
import com.example.student_project.ui.theme.progressBar
import com.example.student_project.ui.theme.textFieldColor
import com.example.student_project.util.Constant
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun InboxChatScreen(
    navController: NavController,
    chatId: String?,
    chatName: String?,
    studentRepo: StudentRepo
) {
    val configuration = LocalConfiguration.current
    val screenWidth = configuration.screenWidthDp.dp
    val screenHeight = configuration.screenHeightDp.dp
    val scope = rememberCoroutineScope()
    var sendMessageTextField by remember {
        mutableStateOf("")
    }
    var voiceFlag by remember {
        mutableStateOf(true)
    }
    var chatState by remember {
        mutableStateOf<Result<List<Message>?>?>(null)
    }
    var messageState by remember {
        mutableStateOf<Result<Message>?>(null)
    }
    var userSate by remember {
        mutableStateOf<String?>(null)
    }
    LaunchedEffect(scope) {
        chatState = studentRepo.getMessage(chatId.toString())
        userSate = studentRepo.getCurrentStudent()?.id
    }
    Scaffold(
        topBar = {
            Box(modifier = Modifier.fillMaxSize()) {
                Row {
                    IconButton(modifier = Modifier
                        .windowInsetsPadding(WindowInsets.systemBars)
                        .padding(
                            top = Constant.smallPadding,
                            bottom = Constant.paddingComponentFromScreen,
                            start = Constant.smallPadding
                        ),
                        onClick = {
                            navController.popBackStack()
                        }) {

                        Icon(
                            imageVector = Icons.AutoMirrored.Default.ArrowBack,
                            contentDescription = "move back"
                        )
                    }
                    Text(
                        modifier = Modifier
                            .windowInsetsPadding(WindowInsets.systemBars)
                            .padding(
                                top = Constant.paddingComponentFromScreen,
                                bottom = Constant.paddingComponentFromScreen,
//                                start = Constant.paddingComponentFromScreen
                            ),
                        text = chatName.toString(),
                        fontSize = 18.sp,
                        fontWeight = FontWeight(700),
                        style = MaterialTheme.typography.titleLarge
                    )
                }
            }
        },
        bottomBar = {
            BottomAppBar(
                modifier = Modifier,
                containerColor = Color.White
            ) {
                Box(modifier = Modifier.fillMaxSize()) {
                    Row {
                        TextField(
                            value = sendMessageTextField,
                            textStyle = MaterialTheme.typography.headlineSmall.copy(
                                fontSize = 15.sp
                            ),
                            onValueChange = {
                                sendMessageTextField = it
                                voiceFlag = false
                            },
                            modifier =
                            Modifier
                                .padding(start = Constant.paddingComponentFromScreen)
                                .width(screenWidth * 80 / 100)
//                                .height(screenHeight * 5 / 100)
                                .align(alignment = Alignment.CenterVertically)
                                .shadow(
                                    elevation = 6.dp,
                                    shape = MaterialTheme.shapes.small,
                                    ambientColor = Color.Gray,
                                    spotColor = Color.LightGray,
                                ),
                            //  .shadow(elevation = 2.dp, ambientColor = Color.Gray),
                            placeholder = {
                                Text(
                                    text = "Message ...",
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
                        AnimatedVisibility(visible = voiceFlag) {
                            IconButton(modifier = Modifier
                                .height(screenHeight * 6 / 100)
                                .width(screenWidth * 12 / 100),
                                colors = IconButtonDefaults.iconButtonColors(
                                    containerColor = progressBar
                                ),
                                onClick = { /*TODO*/ }) {
                                Icon(
                                    modifier = Modifier
                                        .width(screenWidth * 8 / 100)
                                        .height(screenHeight * 6 / 100)
                                        .padding(Constant.smallPadding),
                                    imageVector = Icons.Filled.Call,
                                    tint = Color.White,
                                    contentDescription = "voice message"
                                )
                            }
                        }
                        AnimatedVisibility(visible = !voiceFlag) {
                            IconButton(modifier = Modifier
                                .height(screenHeight * 6 / 100)
                                .width(screenWidth * 12 / 100),
                                colors = IconButtonDefaults.iconButtonColors(
                                    containerColor = progressBar
                                ),
                                onClick = {
                                    //here for send message
                                    CoroutineScope(Dispatchers.IO).launch {
                                        messageState = studentRepo.sendMessage(
                                            chatId.toString(),
                                            sendMessageTextField
                                        )
                                    }
                                    messageState?.onSuccess {
                                        sendMessageTextField = ""
                                    }?.onFailure {
                                        Log.d("sendError", it.message.toString())
                                    }
                                }) {
                                Icon(
                                    modifier = Modifier
                                        .width(screenWidth * 8 / 100)
                                        .height(screenHeight * 6 / 100)
                                        .padding(Constant.smallPadding),
                                    imageVector = Icons.AutoMirrored.Filled.Send,
                                    tint = Color.White,
                                    contentDescription = "voice message"
                                )
                            }
                        }

                    }
                }

            }
        }
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .consumeWindowInsets(innerPadding)
                .padding(top = 80.dp, bottom = 80.dp)

        ) {
            chatState?.onSuccess { messageList ->
                messageList?.let { nonNullMessageList ->
                    items(nonNullMessageList) { message ->
                        MessageRow(
                            message = message,
                            studentRepo = studentRepo
                        )

                    }
                }
            }?.onFailure {
                Log.d("error", it.message.toString())
            }
        }

    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun MessageRow(message: Message, studentRepo: StudentRepo) {
    val configuration = LocalConfiguration.current
    val screenWidth = configuration.screenWidthDp.dp
    var idState by remember {
        mutableStateOf<String?>(null)
    }
    val scope = rememberCoroutineScope()
    LaunchedEffect(scope) {
        idState = studentRepo.getCurrentStudent()?.id
    }
    if (message.sender.id == idState) {
        Column(modifier = Modifier.fillMaxWidth()) {

            Box(
                modifier = Modifier
                    .padding(Constant.normalPadding)
                    .align(Alignment.End)
                    .width(screenWidth * 50 / 100)
                    .background(
                        progressBar,
                        RoundedCornerShape(topStart = 16.dp, bottomStart = 16.dp, bottomEnd = 16.dp)
                    )
                    .shadow(
                        2.dp,
                        shape = RoundedCornerShape(
                            topStart = 16.dp,
                            bottomStart = 16.dp,
                            bottomEnd = 16.dp
                        )
                    )
            ) {
                Surface(
                    color = progressBar,
                    modifier = Modifier
                        .padding(Constant.smallPadding)
                        .clip(shape = RoundedCornerShape(topEnd = 16.dp, bottomStart = 16.dp))
                ) {
                    Column(modifier = Modifier.padding(Constant.smallPadding)) {

                        Text(
                            text = message.content,
                            style = MaterialTheme.typography.titleMedium,
                            fontSize = 11.sp,
                            modifier = Modifier.align(Alignment.End),
                            color = Color.White
                        )
                        Text(
                            text = Instant.parse(message.createdAt).atZone(
                                ZoneId.systemDefault()
                            ).format(DateTimeFormatter.ofPattern("d MMM hh:mm")).toString(),
                            style = MaterialTheme.typography.titleMedium,
                            fontSize = 11.sp,
                            modifier = Modifier,
                            color = Color.White
                        )
                    }
                }
            }
        }
    } else {
        Box(
            modifier = Modifier
                .padding(bottom = Constant.normalPadding)
                .width(screenWidth * 50 / 100)
                .background(
                    chatReceiverColor,
                    RoundedCornerShape(topEnd = 16.dp, bottomStart = 16.dp, bottomEnd = 16.dp)
                )
                .shadow(
                    2.dp,
                    shape = RoundedCornerShape(
                        topEnd = 16.dp,
                        bottomStart = 16.dp,
                        bottomEnd = 16.dp
                    )
                )
        ) {

            Surface(
                color = chatReceiverColor,
                modifier = Modifier

                    .padding(Constant.smallPadding)
                    .clip(shape = RoundedCornerShape(topEnd = 16.dp, bottomStart = 16.dp))
            ) {
                Column(modifier = Modifier.padding(Constant.smallPadding)) {

                    Text(
                        text = message.content,
                        style = MaterialTheme.typography.titleMedium,
                        fontSize = 11.sp,
                        modifier = Modifier.align(Alignment.Start),
                        color = buttonColor
                    )
                    Text(
                        text = Instant.parse(message.createdAt).atZone(
                            ZoneId.systemDefault()
                        ).format(DateTimeFormatter.ofPattern("d MMM hh:mm")).toString(),
                        style = MaterialTheme.typography.titleMedium,
                        fontSize = 11.sp,
                        modifier = Modifier,
                        color = buttonColor
                    )
                }
            }
        }
    }
}


