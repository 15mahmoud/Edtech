package com.example.student_project.ui.screen.inbox

import android.content.Context
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Image
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
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material.TopAppBar
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import coil.request.ImageRequest
import coil.transform.CircleCropTransformation
import com.example.student_project.R
import com.example.student_project.data.model.InboxChat
import com.example.student_project.data.repo.StudentRepo
import com.example.student_project.ui.navigation.Screens
import com.example.student_project.ui.screen.home.content.BottomNavBar
import com.example.student_project.ui.screen.home.content.ScaffoldTopAppBar
import com.example.student_project.ui.theme.StudentProjectTheme
import com.example.student_project.ui.theme.buttonColor
import com.example.student_project.ui.theme.jopTitleColor
import com.example.student_project.ui.theme.unselectedButton
import com.example.student_project.util.Constant
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


@Composable
fun InboxScreen(navController: NavController, studentRepo: StudentRepo) {
    var chatButtonSelected by rememberSaveable { mutableStateOf(true) }
    val context = LocalContext.current
    var meetingButtonSelected by rememberSaveable { mutableStateOf(false) }
    val selectedItemIndex by rememberSaveable { mutableStateOf(3) }
    var chatState by remember {
        mutableStateOf<Result<List<InboxChat>?>?>(null)
    }
    Scaffold(
        Modifier
            .fillMaxSize()
            .background(Color.White),
        bottomBar = { BottomNavBar(selectedItemIndex, navController) },
    ) { innerPadding ->
        Column(
            Modifier
                .consumeWindowInsets(innerPadding)
                .padding(top = Constant.paddingComponentFromScreen)
        ) {
            Text(
                modifier = Modifier
                    .windowInsetsPadding(WindowInsets.systemBars)
                    .padding(
                        top = Constant.paddingComponentFromScreen,
                        bottom = Constant.paddingComponentFromScreen,
                        start = Constant.paddingComponentFromScreen
                    ),
                text = "Inbox",
                fontSize = 18.sp,
                fontWeight = FontWeight(700),
                style = MaterialTheme.typography.titleLarge
            )

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
                        top = Constant.normalPadding,
                        start = Constant.paddingComponentFromScreen,
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

    Column(modifier = Modifier.fillMaxSize()) {

        Card(
            colors = CardDefaults.cardColors(
                containerColor = Color.White
            ),
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = Constant.normalPadding),
            onClick = {
                onClick(chat.id, chat.user.firstName + " " + chat.user.lastName)
            }) {
            Row {

                AsyncImage(
                    modifier = Modifier
                        .width(screenWidth * 16 / 100)
                        .height(screenHeight * 9 / 100)
                        .padding(start = Constant.normalPadding),
                    model = ImageRequest.Builder(context).crossfade(true)
                        .transformations(CircleCropTransformation()).data(chat.user.image)
                        .build(),
                    contentDescription = "user image"
                )
                Box(
                    modifier = Modifier
                        .padding(start = Constant.normalPadding)
                ) {
                    Text(
                        modifier = Modifier.padding(top = Constant.normalPadding),
                        text = chat.user.firstName + " " + chat.user.lastName,
                        style = MaterialTheme.typography.titleLarge,
                        fontSize = 20.sp,
                        color = buttonColor,
                        fontWeight = FontWeight(700)
                    )
                    Text(
                        modifier = Modifier.padding(top = Constant.veryLargePadding),
                        text = chat.latestMessage?.content.toString(),
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


