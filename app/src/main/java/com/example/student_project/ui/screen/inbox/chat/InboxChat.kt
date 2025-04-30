package com.example.student_project.ui.screen.inbox.chat

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
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
import androidx.compose.material3.surfaceColorAtElevation
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
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
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.window.embedding.SplitAttributes
import com.example.student_project.data.model.Message
import com.example.student_project.data.repo.StudentRepo
import com.example.student_project.ui.theme.buttonColor
import com.example.student_project.ui.theme.chatReceiverColor
import com.example.student_project.ui.theme.jopTitleColor
import com.example.student_project.ui.theme.progressBar
import com.example.student_project.ui.theme.textFieldColor
import com.example.student_project.util.Constant
import com.example.student_project.util.Utils
import com.example.student_project.util.Utils.containsArabic
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import okhttp3.internal.format
import org.intellij.lang.annotations.Language
import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.Locale.LanguageRange

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

//    var singleLine = remember {
//        mutableStateOf(true)
//    }
    LaunchedEffect(scope) {
        chatState = studentRepo.getMessage(chatId.toString())
        userSate = studentRepo.getCurrentStudent()?.id
    }
    Scaffold(
        modifier = Modifier.fillMaxSize()
//            .systemBarsPadding() // this use to handle hidden navigation and statues bar
        ,
        topBar = {
            Box(
                modifier = Modifier
                    .padding(top = Constant.paddingComponentFromScreen)
            ) {
                Row {
                    IconButton(modifier = Modifier
//                        .windowInsetsPadding(WindowInsets.systemBars)
                        .padding(
//                            top = Constant.smallPadding,
//                            bottom = Constant.paddingComponentFromScreen,
//                            start = Constant.smallPadding
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
//                            .windowInsetsPadding(WindowInsets.systemBars)
                            .padding(
                                top = Constant.mediumPadding,
//                                bottom = Constant.paddingComponentFromScreen,
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
                modifier = Modifier.imePadding(),//this use to make bottom bar go up with keyboard
                containerColor = Color.White
            ) {
                Box(modifier = Modifier.fillMaxSize()) {
                    TextField(
                        value = sendMessageTextField,
                        textStyle = MaterialTheme.typography.titleMedium.copy(
                            fontSize = 15.sp, color = buttonColor
                        ),
                        onValueChange = {
                            sendMessageTextField = it
                            voiceFlag = it.isEmpty()
//                            singleLine.value = !it.contains('\n') //This one useless
                        },
//                        singleLine = singleLine.value,
                        modifier =
                        Modifier
                            .padding(start = Constant.paddingComponentFromScreen)
                            .width(screenWidth * 77 / 100)
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
                    androidx.compose.animation.AnimatedVisibility(
                        modifier = Modifier
                            .align(
                                Alignment.CenterEnd
                            )
                            .padding(
                                bottom = Constant.paddingComponentFromScreen,
                                end = Constant.smallPadding
                            ), visible = voiceFlag
                    ) {
                        IconButton(modifier = Modifier
                            .height(screenHeight * 7 / 100)
                            .width(screenWidth * 14 / 100),
                            colors = IconButtonDefaults.iconButtonColors(
                                containerColor = buttonColor
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

                    androidx.compose.animation.AnimatedVisibility(
                        modifier = Modifier
                            .align(
                                Alignment.CenterEnd
                            )
                            .padding(
                                bottom = Constant.paddingComponentFromScreen,
                                end = Constant.smallPadding
                            ), visible = !voiceFlag
                    ) {
                        IconButton(modifier = Modifier
                            .height(screenHeight * 7 / 100)
                            .width(screenWidth * 14 / 100),
                            colors = IconButtonDefaults.iconButtonColors(
                                containerColor = buttonColor
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
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
//                .consumeWindowInsets(innerPadding)
                .padding(innerPadding)

        ) {
            chatState?.onSuccess { messageList ->
                messageList?.let { nonNullMessageList ->
                    items(nonNullMessageList) { message ->
                        MessageRow(
                            message = message,
                            isCurrentUser = message.sender.id == userSate,
                            screenWidth = screenWidth
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
fun MessageRow(message: Message, isCurrentUser: Boolean, screenWidth: Dp) {
//    val configuration = LocalConfiguration.current
//    val screenWidth = configuration.screenWidthDp.dp
//    var idState by remember {
//        mutableStateOf<String?>(null)
//    }
//    val scope = rememberCoroutineScope()
//    LaunchedEffect(scope) {
////        idState = studentRepo.getCurrentStudent()?.id
//    }
    val isArabic = message.content.containsArabic()
    val bubbleColor = if (isCurrentUser) progressBar else chatReceiverColor
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 8.dp, vertical = 2.dp),
        horizontalArrangement = if (isCurrentUser) Arrangement.End else Arrangement.Start
    ) {
        Box(
            modifier = Modifier
                .shadow(
                    elevation = 4.dp,
                    shape = when {
                        isCurrentUser -> RoundedCornerShape(
                            topStart = 16.dp,
                            bottomStart = 16.dp,
                            bottomEnd = 16.dp
                        )

                        else -> RoundedCornerShape(
                            topEnd = 16.dp,
                            bottomStart = 16.dp,
                            bottomEnd = 16.dp
                        )
                    },
                    //this feature is so good
                    ambientColor = MaterialTheme.colorScheme.surfaceColorAtElevation(4.dp)
                )
                .widthIn(max = screenWidth * .75f)
                .background(
                    color = bubbleColor,
                    shape = when {
                        isCurrentUser -> RoundedCornerShape(
                            topStart = 16.dp,
                            bottomStart = 16.dp,
                            bottomEnd = 16.dp
                        )

                        else -> RoundedCornerShape(
                            topEnd = 16.dp,
                            bottomStart = 16.dp,
                            bottomEnd = 16.dp
                        )
                    }

                )

        ) {

            Column(
                modifier = Modifier.padding(Constant.smallPadding)
            ) {

                //to control if RTL OR LTR
                CompositionLocalProvider(
                    LocalLayoutDirection provides if (isArabic) LayoutDirection.Rtl else LayoutDirection.Ltr
                ) {

                    Text(
                        text = message.content,
                        style = MaterialTheme.typography.titleMedium,
                        fontSize = 15.sp,
                        modifier = Modifier.align(Alignment.End),
                        color = Color.White
                    )
                }
                Text(
                    text = Utils.fromDateToTime(message.createdAt, "d MMM hh:mm"),
                    style = MaterialTheme.typography.titleMedium,
                    fontSize = 11.sp,
                    modifier = Modifier.align(
                        if (isCurrentUser) {
                            Alignment.End
                        } else {
                            Alignment.End
                        }
                    ),
                    color = jopTitleColor
                )
            }
        }
    }

}