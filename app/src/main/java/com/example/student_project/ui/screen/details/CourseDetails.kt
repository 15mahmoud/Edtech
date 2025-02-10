package com.example.student_project.ui.screen.details

import android.widget.Toast
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.student_project.R
import com.example.student_project.data.model.Course
import com.example.student_project.data.repo.CourseRepo
import com.example.student_project.ui.theme.buttonColor
import com.example.student_project.ui.theme.cardContainerColor
import com.example.student_project.ui.theme.editProfileTextColor
import com.example.student_project.ui.theme.headLineColor
import com.example.student_project.ui.theme.starFillingColor

@Composable
fun CourseDetailsScreen(navController: NavController, courseId: String?, courseRepo: CourseRepo) {
    var courseDetailsState by remember { mutableStateOf<Result<Course?>?>(null) }
    val scope = rememberCoroutineScope()
    val context = LocalContext.current
    val configuration = LocalConfiguration.current
    val screenHeight = configuration.screenHeightDp.dp
    val screenWidth = configuration.screenWidthDp.dp

    LaunchedEffect(scope) {
        val courseDetails = courseRepo.getFullCourseDetails(courseId.toString())
        courseDetailsState = courseDetails
    }

    Column(modifier = Modifier.fillMaxSize()) {
        courseDetailsState
            ?.onSuccess { course ->
                AsyncImage(
                    modifier = Modifier.width(screenWidth).height(screenHeight * 34 / 100),
                    model = course?.thumbnail,
                    contentDescription = "course image",
                )
                Row(Modifier.fillMaxWidth()) {
                    Text(
                        modifier = Modifier.padding(15.dp),
                        text = course?.courseName.toString(),
                        fontSize = 32.sp,
                        color = headLineColor,
                        style = MaterialTheme.typography.headlineLarge,
                        fontWeight = FontWeight(700),
                    )
                    Spacer(modifier = Modifier.width(20.dp))
                    Icon(
                        modifier = Modifier.padding(15.dp),
                        imageVector = Icons.Default.Add,
                        contentDescription = "we do it latter",
                    )
                }

                LazyRow(modifier = Modifier.fillMaxWidth()) {
                    course?.let {
                        items(it.tag) { item ->
                            Card(
                                modifier = Modifier.padding(15.dp),
                                colors =
                                    CardDefaults.cardColors(containerColor = cardContainerColor),
                            ) {
                                Text(
                                    text = item,
                                    color = buttonColor,
                                    fontSize = 10.sp,
                                    style = MaterialTheme.typography.headlineLarge,
                                )
                            }
                        }
                    }
                }
                Row(modifier = Modifier.fillMaxWidth()) {
                    Text(
                        text = "$" + course?.price.toString(),
                        style = MaterialTheme.typography.headlineLarge,
                        fontSize = 32.sp,
                        color = buttonColor,
                        fontWeight = FontWeight(700),
                        modifier = Modifier.padding(15.dp),
                    )
                    Spacer(modifier = Modifier.width(30.dp))
                    Icon(
                        modifier = Modifier.padding(start = 15.dp, bottom = 15.dp, top = 15.dp),
                        imageVector = Icons.Filled.Star,
                        tint = starFillingColor,
                        contentDescription = "rating icon",
                    )
                    Text(
                        text = "4.5",
                        color = editProfileTextColor,
                        style = MaterialTheme.typography.titleMedium,
                        fontSize = 16.sp,
                    )
                }
                Row(modifier = Modifier.fillMaxWidth()) {
                    Icon(
                        modifier =
                            Modifier.padding(
                                start = 15.dp,
                                bottom = 15.dp,
                                top = 15.dp,
                                end = 7.5.dp,
                            ),
                        imageVector = ImageVector.vectorResource(id = R.drawable.add_friends),
                        tint = buttonColor,
                        contentDescription = "participant",
                    )
                    Text(
                        text = course?.studentsEnrolled?.size.toString() + " students",
                        style = MaterialTheme.typography.titleMedium,
                        color = editProfileTextColor,
                        fontSize = 16.sp,
                        fontWeight = FontWeight(500),
                        modifier = Modifier.padding(top = 15.dp, bottom = 15.dp, end = 15.dp),
                    )
                    // this icon need to change
                    Icon(
                        modifier = Modifier.padding(top = 15.dp, bottom = 15.dp, end = 7.5.dp),
                        imageVector = Icons.Filled.AddCircle,
                        contentDescription = "time icon",
                    )
                    Text(
                        text = course?.totalDuration.toString() + " Hours",
                        style = MaterialTheme.typography.titleMedium,
                        color = editProfileTextColor,
                        fontSize = 16.sp,
                        fontWeight = FontWeight(500),
                        modifier = Modifier.padding(top = 15.dp, bottom = 15.dp, end = 15.dp),
                    )
                }
                HorizontalDivider()
            }
            ?.onFailure {
                Toast.makeText(context, "failed to load data", Toast.LENGTH_SHORT).show()
            }
    }
}
