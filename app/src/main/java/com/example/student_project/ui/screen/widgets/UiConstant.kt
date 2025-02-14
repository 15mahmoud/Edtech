package com.example.student_project.ui.screen.widgets

import android.content.Context
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
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
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.student_project.R
import com.example.student_project.data.model.Category
import com.example.student_project.data.model.Course
import com.example.student_project.ui.theme.buttonColor
import com.example.student_project.ui.theme.darkerGrayColor
import com.example.student_project.ui.theme.editProfileTextColor
import com.example.student_project.ui.theme.jopTitleColor
import com.example.student_project.ui.theme.starFillingColor
import com.example.student_project.util.Constant

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
        modifier = Modifier.padding(top = 20.dp),
    ) {
        Icon(
            painter = painterResource(id = R.drawable.baseline_arrow_back_24),
            contentDescription = null,
        )
    }
}

@Composable
fun EditProfileButton(
    imgVector: ImageVector,
    text: String,
    route: String,
    navController: NavController,
    modifier: Modifier,
) {
    Button(
        onClick = { navController.navigate(route) },
        modifier = modifier.fillMaxWidth().padding(bottom = 5.dp),
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
fun CourseColumn(course: Course, context: Context, onClickListener: (String) -> Unit) {
    val configuration = LocalConfiguration.current
    val screenHeight = configuration.screenHeightDp.dp
    val screenWidth = configuration.screenWidthDp.dp
    Card(
        modifier =
        Modifier
            .padding(10.dp)
            .fillMaxWidth()
            // .height(screenHeight * 15/100)
            .clickable { onClickListener(course.id) }
    ) {
        Row(modifier = Modifier.fillMaxWidth()) {
            AsyncImage(
                model = ImageRequest.Builder(context = context).crossfade(true)
                    .data(course.thumbnail).build(), contentDescription = "course image",
                modifier = Modifier
                    .width(screenWidth * 37 / 100)
                    .height(screenHeight * 14 / 100)
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
