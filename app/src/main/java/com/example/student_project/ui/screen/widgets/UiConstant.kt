package com.example.student_project.ui.screen.widgets

import android.content.Context
import android.widget.Toast
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import coil.request.ImageRequest
import coil.transform.CircleCropTransformation
import com.example.student_project.R
import com.example.student_project.data.model.Category
import com.example.student_project.data.model.ChattingRoom
import com.example.student_project.data.model.Course
import com.example.student_project.data.model.Instructor
import com.example.student_project.data.model.RatingAndReview
import com.example.student_project.data.model.SubSection
import com.example.student_project.data.repo.CourseRepo
import com.example.student_project.data.repo.StudentRepo
import com.example.student_project.ui.navigation.Screens
import com.example.student_project.ui.theme.buttonColor
import com.example.student_project.ui.theme.cardContainerColor
import com.example.student_project.ui.theme.darkerGrayColor
import com.example.student_project.ui.theme.editProfileTextColor
import com.example.student_project.ui.theme.jopTitleColor
import com.example.student_project.ui.theme.spotShadowColor
import com.example.student_project.ui.theme.starFillingColor
import com.example.student_project.util.Constant
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

// we will change this to lambda fun that will take string and will return -> string
// then take the returning string and put it in a a list
@Composable
fun BickerButton(timeSlot: String?, onClick: (String?) -> Unit) {
    var selected by remember { mutableStateOf(false) }
    val color = if (selected) buttonColor else Color.White
    Button(
        onClick = {
            selected = !selected
            onClick(timeSlot)
        },
        shape = RoundedCornerShape(10.dp),
        colors = ButtonDefaults.buttonColors(containerColor = color),
        modifier = Modifier.padding(5.dp),
    ) {
        Text(
            text = timeSlot.toString(),
            style = MaterialTheme.typography.titleMedium,
            color = darkerGrayColor,
            fontSize = 12.sp,
        )
    }
}

// we need to add these
// after modify it
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ScaffoldFilterScreenTopBar(navController: NavController, text: String) {
    TopAppBar(
        title = {
            Row(modifier = Modifier.fillMaxWidth()) {
                IconButton(onClick = { navController.popBackStack() }) {
                    Image(
                        painter = painterResource(id = R.drawable.baseline_arrow_back_24),
                        contentDescription = null,
                    )
                }
                Text(
                    text = text,
                    modifier = Modifier.padding(top = 12.dp),
                    style = MaterialTheme.typography.headlineLarge,
                    fontSize = 24.sp,
                    fontWeight = FontWeight(700),
                )
            }
        }
    )
}

@Composable
fun CourseFilterScreenText(text: String) {
    Text(
        text = text,
        Modifier.padding(10.dp),
        fontSize = 14.sp,
        style = MaterialTheme.typography.headlineLarge,
    )
}

@Composable
fun PopBackStackEntry(navController: NavController) {
    IconButton(
        onClick = { navController.popBackStack() },
        modifier = Modifier.padding(top = Constant.normalPadding),
    ) {
        Icon(
            painter = painterResource(id = R.drawable.baseline_arrow_back_24),
            contentDescription = null,
        )
    }
}

@Composable
fun ProfileCommonButton(
    imgVector: ImageVector,
    text: String,
    route: String,
    navController: NavController,
    modifier: Modifier,
) {
    Button(
        onClick = { navController.navigate(route) },
        modifier = modifier
            .fillMaxWidth()
            .padding(bottom = 5.dp),
        colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent),
    ) {
        Row(modifier = modifier.fillMaxWidth()) {
            Icon(
                imageVector = imgVector,
                contentDescription = "button icon",
                tint = editProfileTextColor,
                modifier = modifier.padding(start = 10.dp),
            )
            Text(
                text = text,
                modifier = Modifier.padding(start = 10.dp, top = 5.dp),
                style = MaterialTheme.typography.titleMedium,
                fontSize = 18.sp,
                color = editProfileTextColor,
            )
        }
    }
}


@Composable
fun CategoryRow(category: Category, focused: Boolean, onclick: (String) -> Unit) {
    var focus by remember {
        mutableStateOf(focused)
    }
    Button(
        colors = ButtonDefaults.buttonColors(
            containerColor = if (focus) buttonColor else Color.Transparent
        ),
        shape = RoundedCornerShape(100.dp),
        modifier = Modifier
            .padding(
                end = Constant.mediumPadding,
                bottom = Constant.paddingComponentFromScreen
            )
            .border(2.dp, color = buttonColor, RoundedCornerShape(100.dp)),

        onClick = {
            focus = !focus
            onclick(category.name)
        }) {
        Text(
            text = category.name,
            style = MaterialTheme.typography.titleLarge,
            fontSize = 16.sp,
            fontWeight = FontWeight(600),
            color = if (focus) Color.White else buttonColor
        )
    }
}

@Composable
fun CourseColumn(
    courseRepo: CourseRepo,
    course: Course,
    context: Context,
    onClickListener: (String) -> Unit
) {
    var savedCourseState by remember {
        mutableStateOf<Result<String>?>(null)
    }
    var bookmarkState by remember {
        mutableStateOf(course.isSaved)
    }
    var isLoading by remember {
        mutableStateOf(false)
    }
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
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = Constant.smallPadding)
                ) {
                    Card(
                        modifier = Modifier
                            .align(Alignment.CenterStart)
//                                                    .height(screenHeight * 6/100)
                            .padding(
                                top = Constant.smallPadding,
//                                bottom = Constant.normalPadding,
                                start = Constant.normalPadding
                            ),
                        shape = RoundedCornerShape(6.dp),
                        colors =
                        CardDefaults.cardColors(
                            containerColor = cardContainerColor
                        ),
                    ) {
                        Text(
                            modifier =
                            Modifier
                                .align(Alignment.CenterHorizontally)
                                .padding(
//                                                        top = Constant.verySmallPadding,
//                                                        bottom = Constant.verySmallPadding,
                                    start = Constant.mediumPadding,
                                    end = Constant.mediumPadding,
                                ),
                            text = course.category.name,
                            color = buttonColor,
                            fontSize = 10.sp,
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight(600),
                        )
                    }
                    IconButton(
                        modifier = Modifier
                            .align(Alignment.CenterEnd)
                            .width(screenWidth * 8 / 100)
                            .height(screenHeight * 4 / 100)
                            .padding(top = Constant.smallPadding),
                        onClick = {
                            CoroutineScope(Dispatchers.IO).launch {
                                isLoading = true
                                savedCourseState = courseRepo.savedCourse(course.id)
                            }

                        }) {
                        savedCourseState?.onSuccess {
                            isLoading = false
                            bookmarkState = !bookmarkState
                        }?.onFailure {
                            Toast.makeText(context, "Failed to save course", Toast.LENGTH_SHORT)
                                .show()
                        }

                        when{
                             bookmarkState -> Icon(
                                modifier = Modifier
//                                    .width(screenWidth * 8 / 100)
////                                    .height(screenHeight * 6 / 100)
                                ,
                                imageVector = ImageVector.vectorResource(id = R.drawable.baseline_bookmark_24),
                                tint = buttonColor,
                                contentDescription = "bookmark"
                            )

                            isLoading -> CircularProgressIndicator(
                                modifier = Modifier.width(64.dp),
                                color = MaterialTheme.colorScheme.secondary,
                                trackColor = MaterialTheme.colorScheme.surfaceVariant,
                            )

                            else -> Icon(
                                modifier = Modifier
//                                    .width(screenWidth * 8 / 100)
//                                    .height(screenHeight * 6 / 100)
                                ,
                                imageVector = ImageVector.vectorResource(id = R.drawable.baseline_bookmark_border_24),
                                tint = buttonColor,
                                contentDescription = "bookmark"
                            )
                        }
                    }
                }
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
                Text(
                    modifier = Modifier.padding(
                        start = Constant.normalPadding,
//                        top = Constant.smallPadding
                    ),
                    text =
                    AnnotatedString(
                        "EGP ", spanStyle = SpanStyle(color = jopTitleColor, fontSize = 16.sp),
                    )
                            +
                            AnnotatedString(
                                course.price.toString(),
                                SpanStyle(
                                    fontSize = 16.sp,
                                    fontStyle = FontStyle.Normal,
                                    fontWeight = FontWeight(600)
                                )
                            ),
//                    style = MaterialTheme.typography.titleLarge,
                )
//                HorizontalDivider()
                Row(
                    modifier = Modifier.padding(
                        start = Constant.mediumPadding,
                        bottom = Constant.normalPadding
//                        top = Constant.verySmallPadding
                    )
                ) {

//                    Spacer(modifier = Modifier.width(150.dp))
                    Icon(
                        imageVector = Icons.Filled.Star,
                        contentDescription = "rating star",
                        tint = starFillingColor,
                        modifier = Modifier.size(Constant.starSize)
//                        padding(bottom = 33.dp)
                    )
                    Text(
                        text = ("%.1f".format(course.averageRating)),
                        style = MaterialTheme.typography.titleMedium,
                        fontSize = 15.sp,
                        modifier = Modifier.padding(
                            start = Constant.smallPadding,
//                            top = Constant.smallPadding
                        ),
                        color = buttonColor,
                    )


                }
            }

        }
    }
}

@Composable
fun LessonsColumn(
    subSection: SubSection,
    index: Int,
    lock: Boolean,
    context: Context,
    onClickListener: (String) -> Unit,
) {
    val configuration = LocalConfiguration.current
    val screenHeight = configuration.screenHeightDp.dp
    val screenWidth = configuration.screenWidthDp.dp

    Card(
        colors = CardDefaults.cardColors(containerColor = Color.White),
        modifier =
        Modifier
            .clickable {
                if (lock) {
                    Toast
                        .makeText(context, "This section is locked", Toast.LENGTH_SHORT)
                        .show()
                } else {
                    onClickListener(subSection.videoUrl)
                }
            }
            //            .clip(RoundedCornerShape(200.dp))
            .shadow(4.dp),
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(15.dp)
        ) {
            Card(
                shape = CircleShape,
                colors = CardDefaults.cardColors(containerColor = cardContainerColor),
                modifier =
                Modifier
                    .width(screenWidth * 10 / 100)
                    .height(screenHeight * 5 / 100)

                    // .padding(10.dp)
                    .shadow(4.dp, CircleShape)
                    .align(Alignment.CenterVertically),
            ) {
                Box(modifier = Modifier.fillMaxSize()) {
                    Text(
                        text = (index + 1).toString(),
                        fontSize = 18.sp,
                        fontWeight = FontWeight(700),
                        color = buttonColor,
                        style = MaterialTheme.typography.titleMedium,
                        modifier = Modifier.align(Alignment.Center),
                    )
                }
            }
            Column(modifier = Modifier.padding(start = 10.dp, top = 10.dp, bottom = 10.dp)) {
                Text(
                    text = subSection.title,
                    fontWeight = FontWeight(700),
                    color = buttonColor,
                    style = MaterialTheme.typography.titleMedium,
                    fontSize = 18.sp,
                    modifier = Modifier.padding(bottom = 7.5.dp),
                )
                Text(
                    text = subSection.timeDuration + " mins",
                    fontWeight = FontWeight(700),
                    color = jopTitleColor,
                    style = MaterialTheme.typography.titleMedium,
                    fontSize = 14.sp,
                )
            }
            Spacer(modifier = Modifier.width(130.dp))
            AnimatedVisibility(visible = lock) {
                Icon(
                    imageVector = Icons.Filled.Lock,
                    contentDescription = "Lock icon",
                    modifier = Modifier.padding(10.dp),
                )
            }
        }
    }
}

@Composable
fun ReviewColumn(ratingAndReview: RatingAndReview, context: Context) {
    val configuration = LocalConfiguration.current
    val screenHeight = configuration.screenHeightDp.dp
    val screenWidth = configuration.screenWidthDp.dp

    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = Color.White),
    ) {
        Column(modifier = Modifier.padding(15.dp)) {

            Row {
                AsyncImage(
                    model =
                    ImageRequest.Builder(context = context)
                        .transformations(CircleCropTransformation())
                        .crossfade(true)
                        .data(ratingAndReview.user.image)
                        .build(),
                    contentDescription = "user image",
                    modifier =
                    Modifier
                        .padding(end = 7.5.dp, bottom = 5.dp)
                        .width(screenWidth * 11 / 100)
                        .height(screenHeight * 6 / 100),
                )
                Text(
                    text = ratingAndReview.user.firstName + " " + ratingAndReview.user.lastName,
                    style = MaterialTheme.typography.titleLarge,
                    fontSize = 16.sp,
                    fontWeight = FontWeight(700),
                    modifier = Modifier,
                )
                Spacer(modifier = Modifier.width(50.dp))

                Box(
                    Modifier
                        .width(screenWidth * 14 / 100)
                        .height(screenHeight * 4 / 100)
                        .clip(RoundedCornerShape(100))
                        .border(width = 2.dp, color = buttonColor, RoundedCornerShape(99.dp))
                ) {
                    Icon(
                        imageVector = Icons.Default.Star,
                        tint = buttonColor,
                        contentDescription = "rating icon",
                        modifier = Modifier
                            .align(Alignment.CenterStart)
                            .padding(start = 2.dp),
                    )
                    Text(
                        text = ratingAndReview.rating,
                        style = MaterialTheme.typography.titleLarge,
                        fontSize = 16.sp,
                        color = buttonColor,
                        fontWeight = FontWeight(700),
                        modifier = Modifier
                            .align(Alignment.CenterEnd)
                            .padding(end = 8.dp),
                    )
                }
            }
            Text(
                text = ratingAndReview.review,
                style = MaterialTheme.typography.titleMedium,
                fontSize = 14.sp,
                fontWeight = FontWeight(400),
                modifier = Modifier.padding(top = 7.5.dp),
            )
        }
    }
}

@Composable
fun MentorColumn(
    navController: NavController,
    instructor: Instructor,
    studentRepo: StudentRepo,
    context: Context,
    onClickListener: (String) -> Unit
) {
    val configuration = LocalConfiguration.current
    val screenHeight = configuration.screenHeightDp.dp
    val screenWidth = configuration.screenWidthDp.dp
    var chattingRoomState by remember {
        mutableStateOf<Result<ChattingRoom>?>(null)
    }
    var isLoading by remember {
        mutableStateOf(false)
    }

    androidx.compose.material.Card(
        modifier = Modifier
            .shadow(0.dp)
            .padding(bottom = Constant.mediumPadding)
            .fillMaxWidth()
            .clickable { onClickListener(instructor.id) },
        contentColor = Color.White,
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(end = Constant.normalPadding)
        ) {

            Row {
                AsyncImage(
                    model = ImageRequest.Builder(context = context).crossfade(true)
                        .data(instructor.image)
                        .transformations(CircleCropTransformation()).build(),
                    contentDescription = "mentor image",
                    modifier =
                    Modifier
                        .padding(top = Constant.normalPadding, start = Constant.smallPadding)
                        .height(screenHeight * 8 / 100)
                        .width(screenWidth * 18 / 100),
                )
                Box(
                    modifier = Modifier.padding(
                        start = Constant.mediumPadding,
                        top = Constant.paddingComponentFromScreen + Constant.verySmallPadding
                    )
                ) {
                    Text(
                        text = instructor.firstName + " " + instructor.lastName,
                        style = MaterialTheme.typography.headlineLarge,
                        fontWeight = FontWeight(700),
                        fontSize = 20.sp,
                        color = buttonColor,
                    )
                    Text(
                        modifier = Modifier.padding(top = Constant.paddingComponentFromScreen),
                        text = instructor.additionalDetails.about.toString(),
                        style = MaterialTheme.typography.headlineMedium,
                        fontSize = 14.sp,
                        color = jopTitleColor,
                    )
                }
            }
            IconButton(modifier = Modifier.align(Alignment.CenterEnd), onClick = {
                CoroutineScope(Dispatchers.IO).launch {
                    isLoading = true
                    chattingRoomState = studentRepo.createChat(instructor.id)
                    isLoading = false
                }
                chattingRoomState?.onSuccess { chattingRoom ->
                    for (user in chattingRoom.users) {
                        if (user.id == instructor.id) {
                            navController.navigate(Screens.InboxChatScreen.route + "/${chattingRoom.id}/${instructor.firstName + " " + instructor.lastName}")
                        }
                    }
                }?.onFailure {
                    Toast.makeText(context, "failed to get in chat", Toast.LENGTH_SHORT).show()
                }
            }) {
                when {
                    //why it take  time to start
                    isLoading -> CircularProgressIndicator(
                        modifier = Modifier.width(64.dp),
                        color = MaterialTheme.colorScheme.secondary,
                        trackColor = MaterialTheme.colorScheme.surfaceVariant,
                    )

                    else -> Icon(
                        imageVector = ImageVector.vectorResource(R.drawable.baseline_sms_24),
                        tint = buttonColor,
                        contentDescription = "chatting"
                    )
                }

            }
        }
    }
}
