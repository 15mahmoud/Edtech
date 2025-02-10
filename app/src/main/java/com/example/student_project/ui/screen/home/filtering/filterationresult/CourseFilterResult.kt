package com.example.student_project.ui.screen.home.filtering.filterationresult

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import com.example.student_project.data.model.Course
import com.example.student_project.data.repo.CourseRepo
import com.example.student_project.ui.theme.buttonColor
import com.example.student_project.ui.theme.jopTitleColor
import com.example.student_project.ui.theme.starFillingColor

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CourseFilterResultScreen(
    navController: NavController,
    courseCategory: String?,
    difficultyLevel: String?,
    releasedDate: String?,
    rating: Float?,
    hourlyRate: Float?,
    courseRepo: CourseRepo,
) {
    val scope = rememberCoroutineScope()
    var state by remember { mutableStateOf<Result<List<Course>?>?>(null) }
    LaunchedEffect(key1 = scope) {
        val courseList = courseRepo.getAllCourses()
        state = courseList
    }
    var togel by remember { mutableStateOf(false) }
    // val newCourseList =
    //        state?.onSuccess {
    //           it?.filter { course->
    //               course.category.name == courseCategory &&
    //                       //he didnt use diff
    //                   //course.difficulty == difficultyLevel &&
    //                       //he didnt use released date
    //                   //course.releasedDate == releasedDate &&
    //                       //he didnt use rating
    //                  // course.rating.toFloat() >= rating!! &&
    //                   course.price.toFloat() <= hourlyRate!! }
    //        }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Row(modifier = Modifier.fillMaxWidth()) {
                        IconButton(onClick = { navController.popBackStack() }) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                                contentDescription = null,
                            )
                        }
                        Text(
                            text = "Top Courses",
                            modifier = Modifier.padding(top = 10.dp),
                            style = MaterialTheme.typography.headlineLarge,
                            fontSize = 24.sp,
                            fontWeight = FontWeight(700),
                        )
                        Spacer(modifier = Modifier.width(175.dp))
                        IconButton(
                            modifier = Modifier.padding(top = 5.dp),
                            onClick = { togel = !togel },
                        ) {
                            Icon(imageVector = Icons.Default.Search, contentDescription = null)
                        }
                    }
                }
            )
        }
    ) { innerPadding ->
        Column(modifier = Modifier.padding(innerPadding)) {
            AnimatedVisibility(togel) { Text(text = "this will be search") }
            Text(
                text = "All Courses",
                style = MaterialTheme.typography.headlineLarge,
                fontSize = 15.sp,
                modifier = Modifier.padding(start = 15.dp, bottom = 5.dp, top = 5.dp),
            )
            LazyColumn(modifier = Modifier.padding(start = 15.dp)) {
                state?.onSuccess {
                    it?.let {
                        items(it) { course ->
                            CourseColumn(
                                course = course,
                                // here we will send id to details screen
                                onClickListener = { string ->
                                    // here we will navigate to details screen based on id
                                },
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun CourseColumn(course: Course, onClickListener: (String) -> Unit) {
    val configuration = LocalConfiguration.current
    val screenHeight = configuration.screenHeightDp.dp
    val screenWidth = configuration.screenWidthDp.dp
    Card(
        modifier =
            Modifier.padding(10.dp)
                .fillMaxWidth()
                // .height(screenHeight * 15/100)
                .clickable { onClickListener(course.id) }
    ) {
        Row(modifier = Modifier.fillMaxWidth()) {
            Image(
                painter = rememberAsyncImagePainter(model = course.thumbnail),
                contentDescription = "course image",
            )
            Column {
                Text(
                    text = course.courseName,
                    style = MaterialTheme.typography.titleMedium,
                    fontSize = 20.sp,
                    modifier = Modifier.padding(5.dp),
                )
                Text(
                    text = course.instructor.firstName + " " + course.instructor.lastName,
                    style = MaterialTheme.typography.headlineSmall,
                    fontSize = 15.sp,
                    modifier = Modifier.padding(5.dp),
                    color = jopTitleColor,
                )
                HorizontalDivider()
                Row {
                    Text(
                        text = "${course.price}$",
                        style = MaterialTheme.typography.titleMedium,
                        fontSize = 15.sp,
                        modifier = Modifier.padding(5.dp),
                        color = buttonColor,
                    )
                    Spacer(modifier = Modifier.width(150.dp))
                    Icon(
                        imageVector = Icons.Filled.Star,
                        contentDescription = "rating star",
                        tint = starFillingColor,
                    )
                    Text(
                        // he didn't use rating
                        text = "4.5",
                        style = MaterialTheme.typography.titleMedium,
                        fontSize = 15.sp,
                        modifier = Modifier.padding(5.dp),
                        color = buttonColor,
                    )
                }
            }
        }
    }
}
