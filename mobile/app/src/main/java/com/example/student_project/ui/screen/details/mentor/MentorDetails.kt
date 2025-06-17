package com.example.student_project.ui.screen.details.mentor

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.util.Log
import android.widget.Toast
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import coil.request.ImageRequest
import coil.transform.CircleCropTransformation
import com.example.student_project.data.model.ChattingRoom
import com.example.student_project.data.model.Course
import com.example.student_project.data.model.Instructor
import com.example.student_project.data.model.User
import com.example.student_project.data.repo.InstructorRepo
import com.example.student_project.data.repo.StudentRepo
import com.example.student_project.ui.navigation.Screens
import com.example.student_project.ui.theme.buttonColor
import com.example.student_project.ui.theme.editProfileTextColor
import com.example.student_project.ui.theme.headLineColor
import com.example.student_project.ui.theme.spotShadowColor
import com.example.student_project.ui.theme.unselectedButton
import com.example.student_project.util.Constant
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MentorDetailsScreen(
    navController: NavController,
    instructorId: String?,
    instructorRepo: InstructorRepo,
    studentRepo: StudentRepo
) {
    val scope = rememberCoroutineScope()

    val context = LocalContext.current
    val configuration = LocalConfiguration.current
    val screenHeight = configuration.screenHeightDp.dp
    val screenWidth = configuration.screenWidthDp.dp


    var instructor by remember {
        mutableStateOf<Result<Instructor>?>(null)
    }

    var chattingRoomState by remember {
        mutableStateOf<Result<ChattingRoom>?>(null)
    }

    var courseButtonState by remember {
        mutableStateOf(true)
    }
    var studentButtonState by remember {
        mutableStateOf(false)
    }
    LaunchedEffect(scope) {
        instructor = instructorRepo.getInstructorDetails(instructorId.toString())
    }

    instructor?.onSuccess { mentor ->
        Scaffold(
            topBar = {
                TopAppBar(
                    modifier = Modifier
                        .padding(Constant.paddingComponentFromScreen)
                        .background(color = Color.White),
                    title = {
                        IconButton(onClick = {
                            navController.popBackStack()
                        }) {
                            Icon(
                                modifier = Modifier.size(25.dp),
                                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                                tint = headLineColor,
                                contentDescription = "arrow back icon"
                            )

                        }
                    })
            },
            modifier = Modifier.fillMaxSize()
        ) { innerPadding ->
            Column(modifier = Modifier.padding(innerPadding)) {

                AsyncImage(
                    modifier = Modifier
                        .width(screenWidth * 28 / 100)
                        .height(screenHeight * 14 / 100)
                        .align(Alignment.CenterHorizontally)
                        .padding(Constant.mediumPadding),
                    model = ImageRequest.Builder(context).crossfade(true)
                        .transformations(CircleCropTransformation()).data(mentor.image).build(),
                    contentDescription = "instructor image"
                )
                Text(
                    text = mentor.firstName + " " + mentor.lastName,
                    style = MaterialTheme.typography.titleLarge,
                    fontSize = 24.sp,
                    color = headLineColor,
                    fontWeight = FontWeight(700),
                    modifier = Modifier
                        .padding(bottom = Constant.normalPadding)
                        .align(Alignment.CenterHorizontally)
                )
                Text(
                    text = mentor.additionalDetails.about.toString(),
                    style = MaterialTheme.typography.titleMedium,
                    fontSize = 14.sp,
                    color = editProfileTextColor,
                    fontWeight = FontWeight(600),
                    modifier = Modifier
                        .padding(bottom = Constant.paddingComponentFromScreen)
                        .align(Alignment.CenterHorizontally)
                )
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(
                            bottom = Constant.paddingComponentFromScreen,
                            start = Constant.paddingComponentFromScreen,
                            end = Constant.paddingComponentFromScreen
                        ),
                    horizontalArrangement = Arrangement.spacedBy(
                        Constant.veryLargePadding,
                        alignment = Alignment.CenterHorizontally
                    )
                ) {
                    Column {
                        Text(
                            text = mentor.courses.size.toString(),
                            style = MaterialTheme.typography.titleLarge,
                            fontSize = 24.sp,
                            color = headLineColor,
                            fontWeight = FontWeight(700),
                            modifier = Modifier
                                .padding(bottom = Constant.normalPadding)
                                .align(Alignment.CenterHorizontally)

                        )
                        Text(
                            text = "Courses",
                            style = MaterialTheme.typography.titleMedium,
                            fontSize = 14.sp,
                            color = editProfileTextColor,
                            fontWeight = FontWeight(700),
                            modifier = Modifier

                        )
                    }
                    VerticalDivider(modifier = Modifier.height(screenHeight * 7 / 100))
                    Column {
                        Text(
                            text = mentor.students.size.toString(),
                            style = MaterialTheme.typography.titleLarge,
                            fontSize = 24.sp,
                            color = headLineColor,
                            fontWeight = FontWeight(700),
                            modifier = Modifier
                                .padding(bottom = Constant.normalPadding)
                                .align(Alignment.CenterHorizontally)

                        )
                        Text(
                            text = "Students",
                            style = MaterialTheme.typography.titleMedium,
                            fontSize = 14.sp,
                            color = editProfileTextColor,
                            fontWeight = FontWeight(700),
                            modifier = Modifier

                        )
                    }
                }
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(Constant.paddingComponentFromScreen),
                    horizontalArrangement = Arrangement.spacedBy(
                        Constant.normalPadding,
                        alignment = Alignment.CenterHorizontally
                    )
                ) {
                    OutlinedButton(
                        shape = RoundedCornerShape(100.dp),
                        modifier = Modifier
                            .weight(.5f)
//                            .border(1.dp, buttonColor, RoundedCornerShape(120.dp))
                            .shadow(4.dp, RoundedCornerShape(100.dp)),
                        onClick = {
                            CoroutineScope(Dispatchers.IO).launch {
                                chattingRoomState = studentRepo.createChat(instructorId.toString())
                            }
                            chattingRoomState?.onSuccess { chat ->
                                for (user in chat.users) {
                                    if (user.id == instructorId) {
                                        navController.navigate(Screens.InboxChatScreen.route + "/${chat.id}" + "/${user.firstName + " " + user.lastName}")
                                    }
                                }

                            }?.onFailure {
                                Toast.makeText(context, "chat already exist", Toast.LENGTH_SHORT)
                                    .show()
                            }
                        }, colors = ButtonDefaults.buttonColors(containerColor = buttonColor)
                    ) {
                        Text(
                            text = "Message",
                            style = MaterialTheme.typography.titleMedium,
                            fontSize = 18.sp,
                            color = Color.White,
                            fontWeight = FontWeight(700)
                        )
                    }
                    OutlinedButton(
                        shape = RoundedCornerShape(100.dp),
                        modifier = Modifier
                            .weight(.5f)
//                            .border(2.dp, color = buttonColor, RoundedCornerShape(120.dp))
                            .shadow(4.dp, RoundedCornerShape(100.dp)),
                        onClick = {
                            if (mentor.additionalDetails.linkedIn != null) {

                                val webpage: Uri =
                                    Uri.parse(mentor.additionalDetails.linkedIn)
                                val intent = Intent(Intent.ACTION_VIEW, webpage)
                                try {
                                    context.startActivity(intent)
                                } catch (e: Exception) {
                                    // Handle the exception (e.g., show a message to the user)
                                    e.printStackTrace()
                                }
                            } else {
                                Toast.makeText(context, "no linkin found", Toast.LENGTH_SHORT)
                                    .show()
                            }
                        }, colors = ButtonDefaults.buttonColors(containerColor = Color.White)
                    ) {
                        Text(
                            text = "Website",
                            style = MaterialTheme.typography.titleMedium,
                            fontSize = 18.sp,
                            color = buttonColor,
                            fontWeight = FontWeight(700)
                        )
                    }
                }
                HorizontalDivider(
                    modifier = Modifier.padding(
                        start = Constant.paddingComponentFromScreen,
                        end = Constant.paddingComponentFromScreen,
                        bottom = Constant.normalPadding
                    )
                )
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(
                            start = Constant.paddingComponentFromScreen,
                            end = Constant.paddingComponentFromScreen,
                            bottom = Constant.smallPadding
                        ),
                    horizontalArrangement = Arrangement.spacedBy(
                        Constant.paddingComponentFromScreen,
                        Alignment.CenterHorizontally
                    )
                ) {
                    Button(modifier = Modifier,
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color.Transparent
                        ),
                        onClick = {
                            courseButtonState = true
                            studentButtonState = false
                        }) {
                        Text(
                            text = "Courses",
                            style = MaterialTheme.typography.titleLarge,
                            fontSize = 18.sp,
                            color = if (courseButtonState) headLineColor else unselectedButton,
                            fontWeight = FontWeight(600)
                        )
                    }
                    Button(modifier = Modifier,
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color.Transparent
                        ),
                        onClick = {
                            courseButtonState = false
                            studentButtonState = true
                        }) {
                        Text(
                            text = "Students",
                            style = MaterialTheme.typography.titleLarge,
                            fontSize = 18.sp,
                            color = if (studentButtonState) buttonColor else unselectedButton,
                            fontWeight = FontWeight(600)
                        )
                    }
                }
                HorizontalDivider(
                    modifier = Modifier.padding(
                        start = Constant.paddingComponentFromScreen,
                        end = Constant.paddingComponentFromScreen,
                        bottom = Constant.paddingComponentFromScreen
                    )
                )
                AnimatedVisibility(visible = courseButtonState) {
                    LazyColumn(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(
                                start = Constant.paddingComponentFromScreen,
                                end = Constant.paddingComponentFromScreen
                            )
                    ) {
                        items(mentor.courses) { course ->
                            CourseColumnForMentorDetails(course = course, context = context) {
                                navController.navigate(Screens.CourseDetailScreen.route + "/${course.id}")
                            }
                        }
                    }
                }
                AnimatedVisibility(visible = studentButtonState) {
                    LazyColumn(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(
                                start = Constant.paddingComponentFromScreen,
                                end = Constant.paddingComponentFromScreen
                            )
                    ) {
                        items(mentor.students) { student ->
                            StudentRow(student = student, context = context)
                        }
                    }
                }
            }
        }
    }?.onFailure {
        Toast.makeText(context, it.message.toString(), Toast.LENGTH_SHORT).show()
        Log.d("MentorDetailsScreen", it.message.toString())
    }
}

@Composable
fun CourseColumnForMentorDetails(
    course: Course,
    context: Context,
    onClickListener: (String) -> Unit
) {
    val configuration = LocalConfiguration.current
    val screenHeight = configuration.screenHeightDp.dp
    val screenWidth = configuration.screenWidthDp.dp
    Card(
        modifier =
        Modifier
            .padding(
                start = Constant.normalPadding,
                end = Constant.normalPadding,
                bottom = Constant.mediumPadding
            )
            .fillMaxWidth()
//            .height(screenHeight * 18 / 100)
            .shadow(
                8.dp,
                RoundedCornerShape(Constant.buttonRadios),
//                ambientColor = Color.Blue,
                spotColor = spotShadowColor.copy(.33f)
            )
            // .height(screenHeight * 15/100)
            .clickable { onClickListener(course.id) }, colors = CardDefaults.cardColors(
            containerColor = Color.White
        ), shape = RoundedCornerShape(20.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(
//                    top = Constant.mediumPadding,
                    start = Constant.normalPadding,
                    end = Constant.smallPadding
                )
        ) {
            Card(
                modifier = Modifier
                    .align(Alignment.CenterVertically)

                    .width(screenWidth * 30 / 100)
                    .height(screenHeight * 15 / 100)
                    .padding(top = Constant.smallPadding, bottom = Constant.smallPadding),
//                    .padding(bottom = Constant.normalPadding),
                shape = RoundedCornerShape(15.dp)

            ) {
                AsyncImage(
                    model = ImageRequest.Builder(context = context).crossfade(true)
                        .data(course.thumbnail).build(), contentDescription = "course image",
                    modifier = Modifier,
//                    .padding(Constant)
                    contentScale = ContentScale.Crop
                )
            }
            Column {
                Text(
                    text = course.courseName,
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight(700),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    fontSize = 17.sp,
//                    lineHeight = (-5).sp,
                    modifier = Modifier.padding(
                        start = Constant.normalPadding,
//                        top = Constant.smallPadding
                    ),
                )
//                Text(
//                    modifier = Modifier.padding(
//                        start = Constant.normalPadding,
////                        top = Constant.smallPadding
//                    ),
//                    text =
//                    AnnotatedString("EGP ", spanStyle = SpanStyle(color = jopTitleColor, fontSize = 16.sp))
//                            + AnnotatedString(
//                        course.price.toString(),
//                        SpanStyle(
//                            fontSize = 16.sp,
//                            fontStyle = FontStyle.Normal,
//                            fontWeight = FontWeight(600)
//                        )
//                    ),
//                )
            }

        }
    }
}

@Composable
fun StudentRow(student: User, context: Context) {
    val configuration = LocalConfiguration.current
    val screenHeight = configuration.screenHeightDp.dp
    val screenWidth = configuration.screenWidthDp.dp

    Row(modifier = Modifier.padding(top = Constant.mediumPadding)) {
        AsyncImage(
            modifier = Modifier
                .width(screenWidth * 14 / 100)
                .height(screenHeight * 7 / 100)
                .padding(end = Constant.mediumPadding)
                .align(Alignment.CenterVertically),
            model = ImageRequest.Builder(context).crossfade(true)
                .transformations(CircleCropTransformation()).data(student.image).build(),
            contentDescription = "student image"
        )
        Column {
            Text(
                text = student.firstName + " " + student.lastName,
                style = MaterialTheme.typography.titleLarge,
                fontSize = 18.sp,
                color = headLineColor,
                fontWeight = FontWeight(700),
                modifier = Modifier
                    .padding()
//                    .align(Alignment.CenterHorizontally)
            )
            Text(
                text = student.email,
                style = MaterialTheme.typography.titleMedium,
                fontSize = 14.sp,
                color = editProfileTextColor,
                fontWeight = FontWeight(500),
                modifier = Modifier
                    .padding(bottom = Constant.paddingComponentFromScreen)
                    .align(Alignment.CenterHorizontally)
            )
        }
    }
}