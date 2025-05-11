package com.example.student_project.ui.screen.mycourses

import android.content.Context
import android.widget.Toast
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
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
import com.example.student_project.R
import com.example.student_project.data.model.Course
import com.example.student_project.data.repo.CourseRepo
import com.example.student_project.ui.navigation.Screens
import com.example.student_project.ui.screen.home.content.BottomNavBar
import com.example.student_project.ui.screen.widgets.CourseColumn
import com.example.student_project.ui.theme.buttonColor
import com.example.student_project.ui.theme.cardContainerColor
import com.example.student_project.ui.theme.colorForProgressParFrom50To75
import com.example.student_project.ui.theme.colorForProgressParFrom75To100
import com.example.student_project.ui.theme.headLineColor
import com.example.student_project.ui.theme.jopTitleColor
import com.example.student_project.ui.theme.progressBar
import com.example.student_project.ui.theme.spotShadowColor
import com.example.student_project.ui.theme.starFillingColor
import com.example.student_project.ui.theme.unselectedButton
import com.example.student_project.util.Constant
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MyCoursesScreen(navController: NavController, courseRepo: CourseRepo) {
    val selectedItemIndex by rememberSaveable { mutableStateOf(1) }
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val configuration = LocalConfiguration.current
    val screenHeight = configuration.screenHeightDp.dp
    val screenWidth = configuration.screenWidthDp.dp

    var courseState by remember {
        mutableStateOf<Result<List<Course>?>?>(null)
    }
    var savedCourseState by remember {
        mutableStateOf<Result<List<Course>?>?>(null)
    }
    var ongoingButtonVisibilityState by remember {
        mutableStateOf(true)
    }
    var savesButtonVisibilityState by remember {
        mutableStateOf(false)
    }
    var completedButtonVisibilityState by remember {
        mutableStateOf(false)
    }

    LaunchedEffect(scope) {
        courseState = courseRepo.getAllCourseProgress()

    }
    Scaffold(
        Modifier
            .fillMaxSize()
            .background(Color.White),
        topBar = {
            TopAppBar(
                modifier = Modifier
                    .padding(top = Constant.paddingComponentFromScreen)
                    .background(Color.White),

                title = {
                    Text(
                        text = "My Courses",
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
        Column(Modifier.padding(top = 70.dp, bottom = innerPadding.calculateBottomPadding())) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(
                        start = Constant.paddingComponentFromScreen,
                        end = Constant.paddingComponentFromScreen,
//                        bottom = Constant.smallPadding
                    ),
                horizontalArrangement = Arrangement.spacedBy(
                    Constant.verySmallPadding,
                    Alignment.CenterHorizontally
                )
            ) {
                Button(colors = ButtonDefaults.buttonColors(
                    containerColor = Color.Transparent
                ), onClick = {
                    ongoingButtonVisibilityState = true
                    completedButtonVisibilityState = false
                    savesButtonVisibilityState = false
                }) {
                    Text(
                        text = "Ongoing",
                        style = MaterialTheme.typography.titleMedium,
                        fontSize = 16.sp,
                        fontWeight = FontWeight(600),
                        color = if (ongoingButtonVisibilityState) buttonColor else unselectedButton
                    )
                }
                Button(colors = ButtonDefaults.buttonColors(
                    containerColor = Color.Transparent
                ), onClick = {
                    ongoingButtonVisibilityState = false
                    completedButtonVisibilityState = true
                    savesButtonVisibilityState = false
                }) {
                    Text(
                        text = "Completed",
                        style = MaterialTheme.typography.titleMedium,
                        fontSize = 16.sp,
                        fontWeight = FontWeight(600),
                        color = if (completedButtonVisibilityState) buttonColor else unselectedButton
                    )
                }
                Button(colors = ButtonDefaults.buttonColors(
                    containerColor = Color.Transparent
                ), onClick = {
                    ongoingButtonVisibilityState = false
                    completedButtonVisibilityState = false
                    savesButtonVisibilityState = true
                    CoroutineScope(Dispatchers.IO).launch {
                        savedCourseState = courseRepo.getSavedCourses()
                    }
                }) {
                    Text(
                        text = "Saved",
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        style = MaterialTheme.typography.titleMedium,
                        fontSize = 16.sp,
                        fontWeight = FontWeight(600),
                        color = if (savesButtonVisibilityState) buttonColor else unselectedButton
                    )
                }
            }

            HorizontalDivider(
                modifier = Modifier.padding(
                    start = Constant.paddingComponentFromScreen,
                    end = Constant.paddingComponentFromScreen
                )
            )
            courseState?.onSuccess { courseList ->
                AnimatedVisibility(
                    visible = ongoingButtonVisibilityState,
                    modifier = Modifier.align(Alignment.CenterHorizontally)
                ) {
                    LazyColumn(modifier = Modifier) {
                        courseList?.let { course ->
                            items(course.filter { it.totalLessons != it.completedLessons }) { item ->
                                CourseProgressColumn(course = item, context = context) {
                                    navController.navigate(Screens.CourseDetailScreen.route + "/${item.id}")
                                }
                            }
                        }
                    }
                }

                AnimatedVisibility(
                    visible = completedButtonVisibilityState,
                    modifier = Modifier.align(Alignment.CenterHorizontally)
                ) {
                    LazyColumn(modifier = Modifier) {
                        courseList?.let { course ->
                            items(course.filter { it.totalLessons == it.completedLessons }) { item ->
                                CourseProgressColumn(course = item, context = context) {
                                    navController.navigate(Screens.CourseDetailScreen.route + "/${item.id}")
                                }
                            }
                        }
                    }
                }

            }?.onFailure {
                Toast.makeText(context, "Failed to load data", Toast.LENGTH_SHORT).show()
            }
            savedCourseState?.onSuccess { courseList ->

                //here
                AnimatedVisibility(visible = savesButtonVisibilityState) {
                    LazyColumn {
                        courseList?.let { course ->
                            items(course.filter { it.totalLessons == it.completedLessons }) { item ->
                                SavedCourseColumn(
                                    course = item,
                                    context = context
                                ) {
                                    navController.navigate(Screens.CourseDetailScreen.route + "/${item.id}")
                                }
                            }
                        }
                    }
                }
            }?.onFailure {
                Toast.makeText(context, "Failed to load saved courses", Toast.LENGTH_SHORT).show()
            }
        }
    }
}

@Composable
fun SavedCourseColumn(
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
                Text(
                    modifier = Modifier.padding(
                        start = Constant.normalPadding,
//                        top = Constant.smallPadding
                    ),
                    text =
                    AnnotatedString(
                        "EGP ",
                        spanStyle = SpanStyle(color = jopTitleColor, fontSize = 16.sp)
                    )
                            + AnnotatedString(
                        course.price.toString(),
                        SpanStyle(
                            fontSize = 16.sp,
                            fontStyle = FontStyle.Normal,
                            fontWeight = FontWeight(600)
                        )
                    ),
                )
            }

        }
    }
}

@Composable
fun CourseProgressColumn(course: Course, context: Context, onClickListener: (String) -> (Unit)) {
    val configuration = LocalConfiguration.current
    val screenHeight = configuration.screenHeightDp.dp
    val screenWidth = configuration.screenWidthDp.dp
    Card(
        shape = RoundedCornerShape(20.dp),
        modifier =
        Modifier
            .padding(
                bottom = Constant.smallPadding,
                top = Constant.smallPadding
            )
            .width(screenWidth * 90 / 100)
            // .height(screenHeight * 15/100)
            .clickable { onClickListener(course.id) }
            .shadow(4.dp, RoundedCornerShape(20.dp)), colors = CardDefaults.cardColors(
            containerColor = Color.White
        )
    ) {
        Row(modifier = Modifier.fillMaxWidth()) {
            Card(
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier
                    .width(screenWidth * 37 / 100)
                    .height(screenHeight * 12 / 100)
                    .padding(
                        top = Constant.mediumPadding,
                        bottom = Constant.mediumPadding,
                        start = Constant.mediumPadding,
                        end = Constant.mediumPadding
                    )
                    .border(
                        width = 1.dp,
                        shape = RoundedCornerShape(12.dp),
                        color = Color(0xAAAAAAAA)
                    )
            ) {
                AsyncImage(
                    model = ImageRequest.Builder(context = context).crossfade(true)
                        .data(course.thumbnail).build(),
                    contentScale = ContentScale.Crop,
                    contentDescription = "course image"
                )
            }
            Column {
                Text(
                    text = course.courseName,
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight(600),
                    overflow = TextOverflow.Ellipsis,
                    maxLines = 1,
                    fontSize = 15.sp,
                    lineHeight = 10.sp,
                    color = buttonColor,
                    modifier = Modifier
                        .align(alignment = Alignment.CenterHorizontally)
                        .padding(
                            start = Constant.smallPadding,
                            top = Constant.normalPadding,
                            //bottom = Constant.mediumPadding
                        ),
                )
                Text(
                    text = course.totalDuration,
                    style = MaterialTheme.typography.titleMedium,
                    fontSize = 12.sp,
                    modifier = Modifier
                        .padding(
                            top = Constant.verySmallPadding
//                            Constant.normalPadding
                        )
                        .align(Alignment.CenterHorizontally),
                    color = jopTitleColor,
                    fontWeight = FontWeight(400)
                )

                Text(
                    text = course.completedLessons.toString() + "/" + course.totalLessons.toString(),
                    style = MaterialTheme.typography.titleMedium,
                    fontSize = 12.sp,
                    modifier = Modifier
                        .padding(5.dp)
                        .align(Alignment.CenterHorizontally),
                    color = jopTitleColor,
                    fontWeight = FontWeight(600)
                )
                LinearProgressIndicator(

                    modifier = Modifier.width(175.dp),
                    trackColor = MaterialTheme.colorScheme.surfaceVariant,
                    color = if (course.progressPercentage!! > 0 && course.progressPercentage <= 50) progressBar else if (course.progressPercentage in 50.0..75.0) colorForProgressParFrom50To75 else if (course.progressPercentage in 75.0..100.0) colorForProgressParFrom75To100 else MaterialTheme.colorScheme.surfaceVariant,
                    progress = {
                        course.progressPercentage.toFloat() / 100
                    }
                )


//                HorizontalDivider()
//                Row {
//                    Text(
//                        text = "${course.price}$",
//                        style = MaterialTheme.typography.titleMedium,
//                        fontSize = 15.sp,
//                        modifier = Modifier.padding(5.dp),
//                        color = buttonColor,
//                    )
//                    Spacer(modifier = Modifier.width(150.dp))
//                    Icon(
//                        imageVector = Icons.Filled.Star,
//                        contentDescription = "rating star",
//                        tint = starFillingColor,
//                    )
//                    Text(
//                        // he didn't use rating
//                        text = "4.5",
//                        style = MaterialTheme.typography.titleMedium,
//                        fontSize = 15.sp,
//                        modifier = Modifier.padding(5.dp),
//                        color = buttonColor,
//                    )
//                }
//            }
                //      }
            }
        }
    }
}