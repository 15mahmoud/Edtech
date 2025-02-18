package com.example.student_project.ui.screen.home.trendingcourses

import android.widget.Toast
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.student_project.data.model.Category
import com.example.student_project.data.model.Course
import com.example.student_project.data.repo.CourseRepo
import com.example.student_project.ui.navigation.Screens
import com.example.student_project.ui.screen.widgets.CategoryRow
import com.example.student_project.ui.screen.widgets.CourseColumn
import com.example.student_project.ui.screen.widgets.ScaffoldFilterScreenTopBar
import com.example.student_project.ui.theme.buttonColor
import com.example.student_project.util.Constant

@Composable
fun TrendingCourseScreen(navController: NavController, courseRepo: CourseRepo) {
    val scope = rememberCoroutineScope()
    val context = LocalContext.current
    var courseState by remember { mutableStateOf<Result<List<Course>?>?>(null) }
    var categoryState by remember {
        mutableStateOf<Result<List<Category>?>?>(null)
    }

    var focused by remember {
        mutableStateOf(true)
    }
    var focusedForLazyColumn by remember {
        mutableStateOf(false)
    }

    var focusedCategory by remember {
        mutableStateOf("")
    }
    LaunchedEffect(scope) {
        courseState = courseRepo.getAllCourses()
        categoryState = courseRepo.showAllCategories()
    }
    Scaffold(
        topBar = {
            ScaffoldFilterScreenTopBar(navController = navController, text = "Trending Courses")
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
        ) {
            Row {
                Button(
                    colors = ButtonDefaults.buttonColors(
                        containerColor = if (focused) buttonColor else Color.Transparent
                    ),
                    shape = RoundedCornerShape(100.dp),
                    modifier = Modifier
                        .padding(
                            top = Constant.paddingComponentFromScreen,
                            end = Constant.mediumPadding,
                            bottom = Constant.paddingComponentFromScreen
                        )
                        .border(2.dp, color = buttonColor, RoundedCornerShape(100.dp)),

                    onClick = {
                        focused = !focused
                    }) {
                    Text(
                        text = "All",
                        style = MaterialTheme.typography.titleLarge,
                        fontSize = 16.sp,
                        fontWeight = FontWeight(600),
                        color = if (focused) Color.White else buttonColor
                    )
                }
                LazyRow(modifier = Modifier.padding(Constant.paddingComponentFromScreen)) {
                    categoryState?.onSuccess { category ->
                        category?.let {
                            items(it) { notNullCategory ->
                                CategoryRow(category = notNullCategory, false) {
                                    focusedForLazyColumn = !focusedForLazyColumn
                                    focusedCategory = notNullCategory.name
                                }
                            }

                        }

                    }?.onFailure {
                        Toast.makeText(context, "failed to load category", Toast.LENGTH_SHORT)
                            .show()
                    }
                }
            }
            LazyColumn(modifier = Modifier.fillMaxWidth()) {
                courseState?.onSuccess { course ->
                    course?.let { notNollCourse ->

                        if (focused) {
                            items(notNollCourse.sortedBy { it.averageRating }) { item ->
                                CourseColumn(course = item, context = context) {
                                    navController.navigate(Screens.CourseDetailScreen.route + "/${item.id}")
                                }
                            }
                        } else if (focusedForLazyColumn) {
                            items(notNollCourse.filter { it.category.name == focusedCategory }
                                .sortedBy { it.averageRating }) { item ->
                                CourseColumn(course = item, context = context) {
                                    navController.navigate(Screens.CourseDetailScreen.route + "/${item.id}")
                                }
                            }
                        }
                    }

                }?.onFailure {
                    Toast.makeText(context, "failed to load courses", Toast.LENGTH_SHORT).show()
                }
            }
        }

    }
}