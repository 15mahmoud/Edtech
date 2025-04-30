package com.example.student_project.ui.screen.inbox

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.util.Log
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Done
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import coil.request.ImageRequest
import coil.transform.CircleCropTransformation
import com.example.student_project.data.model.InboxChat
import com.example.student_project.data.model.Meeting
import com.example.student_project.data.repo.StudentRepo
import com.example.student_project.ui.navigation.Screens
import com.example.student_project.ui.screen.home.content.BottomNavBar
import com.example.student_project.ui.theme.anotherColorForFillingStar
import com.example.student_project.ui.theme.buttonColor
import com.example.student_project.ui.theme.editProfileLogoutColor
import com.example.student_project.ui.theme.headLineColor
import com.example.student_project.ui.theme.jopTitleColor
import com.example.student_project.ui.theme.unselectedButton
import com.example.student_project.util.Constant
import com.example.student_project.util.Utils
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter


@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun InboxScreen(navController: NavController, studentRepo: StudentRepo) {
    var chatButtonSelected by rememberSaveable { mutableStateOf(true) }
    val context = LocalContext.current
    var meetingButtonSelected by rememberSaveable { mutableStateOf(false) }
    val selectedItemIndex by rememberSaveable { mutableStateOf(3) }
    var chatState by remember {
        mutableStateOf<Result<List<InboxChat>?>?>(null)
    }
    var meetingState by remember {
        mutableStateOf<Result<List<Meeting>?>?>(null)
    }
    Scaffold(
        Modifier
            .fillMaxSize()
            .background(Color.White),
        topBar = {
            TopAppBar(
                modifier = Modifier.padding(top = Constant.paddingComponentFromScreen),
                backgroundColor = Color.White,
                title = {
                    Text(
                        text = "Inbox",
                        style = MaterialTheme.typography.titleLarge,
                        color = headLineColor,
                        fontSize = 16.sp,
                        fontWeight = FontWeight(700),
                        modifier = Modifier.padding(Constant.paddingComponentFromScreen)
                    )
                })
        },
        bottomBar = { BottomNavBar(selectedItemIndex, navController) },
    ) { innerPadding ->
        Column(
            Modifier
//                .consumeWindowInsets(innerPadding)
                .padding( innerPadding)
        ) {
//            Text(
//                modifier = Modifier
////                    .windowInsetsPadding(WindowInsets.systemBars)
//                    .padding(
//                        top = Constant.paddingComponentFromScreen,
//                        bottom = Constant.paddingComponentFromScreen,
//                        start = Constant.paddingComponentFromScreen
//                    ),
//                text = "Inbox",
//                fontSize = 20.sp,
//                fontWeight = FontWeight(700),
//                style = MaterialTheme.typography.headlineLarge
//            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceAround
            ) {
                Button(
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color.Transparent
                    ),
                    modifier = Modifier,
                    onClick = {
                        chatButtonSelected = true
                        meetingButtonSelected = false
                    }
                ) {
                    Text(
                        text = "Chats",
                        style = MaterialTheme.typography.titleMedium,
                        color = if (chatButtonSelected) buttonColor else unselectedButton,
                        fontSize = 18.sp,
                        fontWeight = FontWeight(600)
                    )
                }
                Button(
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color.Transparent
                    ),
                    modifier = Modifier,
                    onClick = {
                        meetingButtonSelected = true
                        chatButtonSelected = false
                    }
                ) {

                    Text(
                        text = "Meeting",
                        style = MaterialTheme.typography.titleMedium,
                        color = if (meetingButtonSelected) buttonColor else unselectedButton,
                        fontSize = 18.sp,
                        fontWeight = FontWeight(600)
                    )


                }
            }
            HorizontalDivider(
                modifier = Modifier.padding(
                    start = Constant.paddingComponentFromScreen,
                    end = Constant.paddingComponentFromScreen
                )
            )

            AnimatedVisibility(visible = chatButtonSelected) {
                LazyColumn(
                    modifier = Modifier.padding(
//                        top = Constant.normalPadding,
//                        start = Constant.paddingComponentFromScreen,
                        end = Constant.paddingComponentFromScreen
                    )
                ) {
                    CoroutineScope(Dispatchers.IO).launch {
                        chatState = studentRepo.getAllChat()
                    }
                    chatState?.onSuccess { chatList ->
                        chatList?.let { chats ->
                            items(chats) { chat ->
                                ChatRow(chat, context = context) { chatId, chatName ->
                                    navController.navigate(Screens.InboxChatScreen.route + "/$chatId" + "/$chatName")
                                }
                            }
                        }
                    }?.onFailure {
                        Log.e("failed to get chats", it.message.toString())
                    }

                }
            }
            AnimatedVisibility(visible = meetingButtonSelected) {
                LazyColumn(
                    modifier = Modifier.padding(
//                        top = Constant.normalPadding,
//                        start = Constant.paddingComponentFromScreen,
                        end = Constant.paddingComponentFromScreen
                    )
                ) {
                    CoroutineScope(Dispatchers.IO).launch {
                        meetingState = studentRepo.getAllMeeting()
                    }
                    meetingState?.onSuccess { meetingList ->
                        meetingList?.let { nonNullMeetingList ->
                            items(nonNullMeetingList) { meeting ->
                                MeetingRow(meeting = meeting, context = context) {
                                    val webpage: Uri =
                                        Uri.parse(meeting.url)
                                    val intent = Intent(Intent.ACTION_VIEW, webpage)
                                    try {
                                        context.startActivity(intent)
                                    } catch (e: Exception) {
                                        // Handle the exception (e.g., show a message to the user)
                                        e.printStackTrace()
                                    }
                                }
                            }
                        }

                    }?.onFailure {
                        Toast.makeText(context, "failed to get meeting", Toast.LENGTH_SHORT).show()
                    }

                }

            }
        }
    }
}


@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun MeetingRow(
    meeting: Meeting, context: Context, onClick: (String) -> Unit
) {
    val configuration = LocalConfiguration.current
    val screenWidth = configuration.screenWidthDp.dp
    val screenHeight = configuration.screenHeightDp.dp



    Card(
        colors = CardDefaults.cardColors(
            containerColor = Color.Transparent
        ),
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = Constant.mediumPadding),
        onClick = {
            onClick(meeting.url)
        }) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(end = Constant.normalPadding)
        ) {

            Row {


                AsyncImage(
                    modifier = Modifier
                        .width(screenWidth * 18 / 100)
                        .height(screenHeight * 8 / 100)
                        .padding(top = Constant.normalPadding, start = Constant.smallPadding),
                    model = ImageRequest.Builder(context).crossfade(true)
                        .transformations(CircleCropTransformation()).data(meeting.host.image)
                        .build(),
                    contentDescription = "user image"
                )
                Box(
                    modifier = Modifier
                        .padding(
                            start = Constant.normalPadding
                        )
                ) {
                    Text(
                        modifier = Modifier.padding(top = Constant.normalPadding),
                        text = meeting.host.firstName + " " + meeting.host.lastName,
                        style = MaterialTheme.typography.headlineLarge,
                        fontSize = 20.sp,
                        color = buttonColor,
                        fontWeight = FontWeight(700)
                    )
                    Row {
                        Card(
                            //we need to change size
                            modifier = Modifier.padding(
                                top = 37.5.dp,
                                end = Constant.verySmallPadding
                            ),
                            shape = RoundedCornerShape(15.dp), colors = CardDefaults.cardColors(
                                containerColor = if (meeting.isEnded) editProfileLogoutColor else anotherColorForFillingStar
                            )
                        ) {
                            if (meeting.isEnded) {
                                Icon(
                                    modifier = Modifier.size(10.dp),
                                    imageVector = Icons.Default.Close,
                                    contentDescription = "ended"
                                )

                            } else {
                                Icon(
                                    modifier = Modifier.size(10.dp),
                                    imageVector = Icons.Default.Done, contentDescription = "ended"
                                )

                            }
                        }
                        Text(
                            modifier = Modifier.padding(top = Constant.veryLargePadding),
                            text = if (meeting.isEnded) "Ended" else "Outgoing",
                            style = MaterialTheme.typography.titleLarge,
                            fontSize = 15.sp,
                            color = jopTitleColor,
                            fontWeight = FontWeight(400)
                        )
                        Text(
                            modifier = Modifier.padding(
                                top = Constant.veryLargePadding,
                                start = Constant.verySmallPadding,
                                end = Constant.verySmallPadding
                            ),
                            text = "|",
                            style = MaterialTheme.typography.titleLarge,
                            fontSize = 15.sp,
                            color = jopTitleColor,
                            fontWeight = FontWeight(400)
                        )
                        Text(
                            modifier = Modifier.padding(top = Constant.veryLargePadding),
                            text = Utils.fromDateToTime(meeting.date," MMM d ,yyyy"),
                            style = MaterialTheme.typography.titleLarge,
                            fontSize = 15.sp,
                            color = jopTitleColor,
                            fontWeight = FontWeight(400)
                        )
                    }
                }
            }
        }
    }
}


//@Preview(showBackground = true)
@Composable
fun ChatRow(
    chat: InboxChat, context: Context, onClick: (String, String) -> Unit
) {
    val configuration = LocalConfiguration.current
    val screenWidth = configuration.screenWidthDp.dp
    val screenHeight = configuration.screenHeightDp.dp


    Card(
        colors = CardDefaults.cardColors(
            containerColor = Color.Transparent
        ),
        modifier = Modifier
//            .shadow(0.dp)
            .padding(
                top = Constant.mediumPadding,
                bottom = Constant.verySmallPadding,
                start = screenWidth * 2 / 100
            )
            .fillMaxWidth(),
        onClick = {
            onClick(chat.id, chat.user.firstName + " " + chat.user.lastName)
        }) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(end = Constant.normalPadding)
        ) {

            AsyncImage(
                modifier = Modifier
                    .width(screenWidth * 16 / 100)
                    .height(screenHeight * 7 / 100)
                    .padding(
//                        top = Constant.smallPadding,
                        start = Constant.smallPadding
                    ),
                model = ImageRequest.Builder(context).crossfade(true)
                    .transformations(CircleCropTransformation()).data(chat.user.image)
                    .build(),
                contentDescription = "user image"
            )
            Box(
                modifier = Modifier
                    .padding(
                        start = Constant.mediumPadding,
                        top = Constant.normalPadding
                    )
            ) {
                Text(
                    text = chat.user.firstName + " " + chat.user.lastName,
                    style = MaterialTheme.typography.headlineLarge,
                    fontSize = 16.sp,
                    color = buttonColor,
                    fontWeight = FontWeight(700)
                )
                Text(
                    modifier = Modifier.padding(
                        top = Constant.normalPadding
                    ),
                    text = chat.latestMessage?.content.toString(),
                    style = MaterialTheme.typography.headlineMedium,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    fontSize = 14.sp,
                    color = jopTitleColor,
                    fontWeight = FontWeight(400)
                )
            }
        }
    }

}