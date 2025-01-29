package com.example.student_project.ui.screen.home

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import coil.transform.CircleCropTransformation
import com.example.student_project.R
import com.example.student_project.data.model.Course
import com.example.student_project.data.model.Mentor
import com.example.student_project.data.repo.CourseRepo
import com.example.student_project.data.repo.MentorRepo
import com.example.student_project.ui.navigation.Screens
import com.example.student_project.screen.home.uidata.BottomNavItem
import com.example.student_project.ui.theme.lightGray
import com.example.student_project.ui.theme.starFillingColor

val courseRepo = CourseRepo()
val mentorRepo = MentorRepo()

data class HomeScreenState(
    val courses: List<Course>,
    val trendingCourses: List<Course>,
    val mentor: List<Mentor>,
)

@Composable
fun HomeScreen(navController: NavController) {
    val configuration = LocalConfiguration.current
    val screenHeight = configuration.screenHeightDp.dp
    val screenWidth = configuration.screenWidthDp.dp
    // it will be remembered if user rotate the screen
    val selectedItemIndex by rememberSaveable { mutableStateOf(0) }
    var searchState by rememberSaveable { mutableStateOf("") }
    val scrollState = rememberScrollState()
    val scope = rememberCoroutineScope()

    val state = remember { mutableStateOf(HomeScreenState(emptyList(), emptyList(), emptyList())) }
    LaunchedEffect(scope) {
        val courseList = courseRepo.getCourseList()
        val trendingCoursesList = courseRepo.getTrendingCourse()
        val mentorList = mentorRepo.getMentorList()
        state.value = HomeScreenState(courseList, trendingCoursesList, mentorList)
    }
    Scaffold(
        Modifier.fillMaxSize().background(Color.White),
        topBar = { ScaffoldTopAppBar() },
        bottomBar = { BottomNavBar(selectedItemIndex, navController) },
    ) { innerPadding ->
        Box(Modifier.fillMaxSize().padding(19.dp)) {
            Column(Modifier.padding(innerPadding).fillMaxSize().verticalScroll(scrollState)) {
                Row(modifier = Modifier.fillMaxWidth()) {
                    TextField(
                        modifier =
                            Modifier.border(
                                width = 3.dp,
                                color = Color.White,
                                shape = RoundedCornerShape(10.dp),
                            ),
                        value = searchState,
                        onValueChange = {
                            // here when value change we will give this value to back end to search
                            // and then we get the result
                            searchState = it
                        },
                        placeholder = {
                            Text(text = "search", fontSize = 22.sp, color = Color.Gray)
                        },
                        leadingIcon = {
                            Image(
                                modifier = Modifier,
                                painter = painterResource(id = R.drawable.baseline_search_24),
                                contentDescription = null,
                            )
                            // here we can make trailing icon that can be clickable
                        },
                        colors =
                            TextFieldDefaults.colors(
                                unfocusedContainerColor = Color.Transparent,
                                focusedContainerColor = Color.Transparent,
                            ),
                    )
                    // this is filter button
                    // we will need to modify this
                    Button(
                        onClick = { navController.navigate(Screens.MentorFilterScreen.route) },
                        colors = ButtonDefaults.buttonColors(containerColor = Color.Black),
                    ) {
                        Image(
                            painter = painterResource(id = R.drawable.move_to_filter),
                            contentDescription = null,
                        )
                    }
                }
                Spacer(modifier = Modifier.fillMaxWidth().height(10.dp))

                // we will make pager(slider)
                Box(
                    modifier =
                        Modifier.clip(shape = RoundedCornerShape(32.dp))
                            // we will make black shadow with opacity 0.25
                            // .shadow(elevation = 15.dp, ambientColor = Color.Black)
                            .width(screenWidth * 91 / 100)
                            .height(screenHeight * 21 / 100)
                            .background(Color.Black)
                ) {
                    Image(
                        modifier =
                            Modifier.width(screenWidth * 91 / 100).height(screenHeight * 21 / 100),
                        painter = painterResource(id = R.drawable.pager_background),
                        contentDescription = null,
                    )
                    Row(modifier = Modifier.fillMaxSize().padding(20.dp)) {
                        Column(modifier = Modifier.fillMaxHeight()) {
                            Text(
                                text = "40% OFF",
                                fontSize = 14.sp,
                                style = MaterialTheme.typography.titleMedium,
                                color = Color.White,
                            )
                            Text(
                                modifier = Modifier.padding(top = 8.dp, bottom = 8.dp),
                                text = "Today's Special",
                                fontSize = 22.sp,
                                style = MaterialTheme.typography.headlineLarge,
                                color = Color.White,
                            )
                            Text(
                                modifier = Modifier.width(screenWidth * 41 / 100),
                                text =
                                    "Get a discount for every course order! Only valid for today",
                                fontSize = 13.sp,
                                style = MaterialTheme.typography.titleMedium,
                                color = Color.White,
                            )
                        }

                        Spacer(modifier = Modifier.width(10.dp))

                        Image(
                            modifier =
                                Modifier.width(screenWidth * 36 / 100)
                                    .height(screenHeight * 16 / 100),
                            alignment = Alignment.Center,
                            painter = painterResource(id = R.drawable.pager_image),
                            contentDescription = null,
                        )
                    }
                }

                Box(modifier = Modifier.fillMaxWidth()) {
                    Text(
                        text = "Trending Courses",
                        fontSize = 15.sp,
                        style = MaterialTheme.typography.titleMedium,
                        modifier = Modifier.padding(top = 15.dp),
                    )

                    Button(
                        modifier = Modifier.align(alignment = Alignment.CenterEnd),
                        onClick = {
                            // here we write code to navigate to all subject
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent),
                    ) {
                        Row {
                            Text(text = "All subject", fontSize = 10.sp, color = Color.Blue)
                            // here we put image for a right arrow
                        }
                    }
                }
                LazyRow(modifier = Modifier.fillMaxWidth().padding(top = 5.dp, bottom = 5.dp)) {
                    // we will change this subject list with another list we will get from api
                    // we suppose to modify this size to match all device
                    itemsIndexed(state.value.trendingCourses) { index, course ->
                        CourseRaw(course) {
                            // navigate
                            navController.navigate(
                                Screens.CourseDetailScreen.route + "/${it.title}"
                            )
                        }
                    }
                }
                Text(
                    modifier = Modifier.padding(bottom = 5.dp),
                    text = "Weekly TopLive Tutors ",
                    fontSize = 15.sp,
                    style = MaterialTheme.typography.titleMedium,
                )
                LazyRow(modifier = Modifier.fillMaxWidth().padding(top = 5.dp, bottom = 5.dp)) {
                    // we will change this subject list with another list we will get from api
                    // we suppose to modify this size to match all device
                    itemsIndexed(state.value.mentor) { index, mentor ->
                        Box(
                            modifier =
                                Modifier.width(140.dp)
                                    .height(136.dp)
                                    .padding(top = 5.dp, bottom = 5.dp)
                                    .clip(RoundedCornerShape(15.dp))
                                    .border(
                                        width = 2.dp,
                                        color = lightGray,
                                        shape = RoundedCornerShape(14.dp),
                                    )
                                    .background(Color.White)
                                    .clickable {
                                        // here i will put code to navigate to the disired course
                                    }
                        ) {
                            AsyncImage(
                                model =
                                    ImageRequest.Builder(LocalContext.current)
                                        .data(mentor.image)
                                        .crossfade(true)
                                        .transformations(CircleCropTransformation())
                                        .build(),
                                contentDescription = null,
                                modifier =
                                    Modifier.width(60.dp)
                                        .height(60.dp)
                                        .padding(top = 10.dp, bottom = 10.dp)
                                        .align(alignment = Alignment.TopCenter),
                            )
                            Text(
                                text = mentor.mentorName,
                                style = MaterialTheme.typography.headlineLarge,
                                fontSize = 15.sp,
                                modifier =
                                    Modifier.padding(top = 25.dp)
                                        .align(alignment = Alignment.Center),
                            )
                            Text(
                                modifier =
                                    Modifier.align(Alignment.BottomCenter).padding(bottom = 25.dp),
                                text = mentor.jopTitle,
                                style = MaterialTheme.typography.titleMedium,
                                color = Color.Gray,
                            )
                        }
                    }
                }
                Box(modifier = Modifier.fillMaxWidth()) {
                    Text(
                        text = "Top New Courses",
                        fontSize = 15.sp,
                        style = MaterialTheme.typography.titleMedium,
                        modifier = Modifier.padding(top = 15.dp, start = 7.dp),
                    )

                    Button(
                        modifier = Modifier.align(alignment = Alignment.CenterEnd),
                        onClick = {
                            // here we write code to navigate to all subject
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent),
                    ) {
                        Row {
                            Text(text = "All subject", fontSize = 10.sp, color = Color.Blue)
                            // here we put image for a right arrow
                        }
                    }
                }
                LazyRow(modifier = Modifier.fillMaxWidth().padding(top = 5.dp, bottom = 5.dp)) {
                    // we will change this subject list with another list we will get from api
                    // we suppose to modify this size to match all device
                    itemsIndexed(state.value.courses) { index, course ->
                        CourseRaw(course) {
                            navController.navigate(
                                Screens.CourseDetailScreen.route + "/${it.title}"
                            )
                        }
                    }
                }
            }
        }
    }
}

// i should move those 2 function from here may be i make another file for them
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ScaffoldTopAppBar() {

    TopAppBar(
        title = {
            Column(modifier = Modifier.padding(20.dp)) {
                Row(modifier = Modifier.fillMaxWidth()) {
                    Image(
                        painter = painterResource(id = R.drawable.profile),
                        contentDescription = null,
                        Modifier.padding(10.dp)
                            .size(40.dp)
                            .border(2.dp, color = Color.White, shape = CircleShape),
                    )
                    Column {
                        Spacer(modifier = Modifier.fillMaxWidth().height(15.dp))
                        Text(
                            text = "Hello",
                            style = MaterialTheme.typography.headlineSmall,
                            color = Color.Gray,
                            fontWeight = FontWeight(400),
                        )
                        Text(text = "Tarek", style = MaterialTheme.typography.titleMedium)
                    }
                }
            }
        }
    )
}

@Composable
fun BottomNavBar(selectedState: Int, navController: NavController) {
    val items =
        listOf(
            BottomNavItem(
                route = "home_screen",
                selectedIcon = Icons.Filled.Home,
                unselectedIcon = Icons.Outlined.Home,
                label = "Home",
            ),
            BottomNavItem(
                route = "learning_screen",
                selectedIcon = Icons.Filled.Search,
                unselectedIcon = Icons.Outlined.Search,
                label = "Learning",
            ),
            BottomNavItem(
                route = "profile_screen",
                selectedIcon = Icons.Filled.Person,
                unselectedIcon = Icons.Outlined.Person,
                label = "Profile",
            ),
        )
    var selectedItemIndex by rememberSaveable { mutableStateOf(selectedState) }

    NavigationBar {
        items.forEachIndexed { index, bottomNavItem ->
            NavigationBarItem(
                selected = selectedItemIndex == index,
                onClick = {
                    selectedItemIndex = index
                    navController.navigate(bottomNavItem.route)
                },
                label = { Text(text = bottomNavItem.label) },
                icon = {
                    Icon(
                        imageVector =
                            if (index == selectedItemIndex) {
                                bottomNavItem.selectedIcon
                            } else bottomNavItem.unselectedIcon,
                        contentDescription = null,
                    )
                },
            )
        }
    }
}

// here we will make lazy row for courses
// here we make it clickable and send a list of courses to the next screen
@Composable
fun CourseRaw(course: Course, onCLickListener: (Course) -> Unit) {
    val configuration = LocalConfiguration.current
    val screenHeight = configuration.screenHeightDp.dp
    val screenWidth = configuration.screenWidthDp.dp
    Column(
        modifier =
            Modifier.width(screenWidth * 47 / 100)
                .height(screenHeight * 3 / 10)
                .padding(2.dp)
                .clip(RoundedCornerShape(15.dp))
                .clickable { onCLickListener(course) }
    ) {
        //        AsyncImage(
        //            model = course.imgPath,
        //            contentDescription = "course image",
        //            modifier = Modifier
        //                .width(screenWidth * 47 / 100)
        //                .height(screenHeight * 14 / 100)
        //                .padding(5.dp),
        //        )
        Image(
            painter = rememberAsyncImagePainter(model = course.imgPath),
            contentDescription = "course image",
            modifier =
                Modifier.width(screenWidth * 47 / 100).height(screenHeight * 14 / 100).padding(5.dp),
        )
        Text(
            text = course.title,
            style = MaterialTheme.typography.headlineLarge,
            fontSize = 13.sp,
            modifier = Modifier.padding(start = 7.dp),
        )
        Text(
            modifier = Modifier.padding(start = 7.dp, top = 5.dp, bottom = 5.dp),
            text = course.mentorName,
            style = MaterialTheme.typography.titleMedium,
            color = Color(0xFF334155),
        )
        HorizontalDivider()
        Row(modifier = Modifier.padding(top = 5.dp)) {
            Text(
                modifier = Modifier.padding(top = 5.dp),
                text = "$" + course.hourlyRate.toString(),
                style = MaterialTheme.typography.titleMedium,
            )
            Spacer(modifier = Modifier.width(60.dp))

            Icon(
                imageVector = Icons.Filled.Star,
                tint = starFillingColor,
                contentDescription = null,
            )

            Text(
                text = course.rating.toString(),
                fontSize = 12.sp,
                modifier = Modifier.padding(start = 4.dp, top = 1.dp),
            )
        }
    }
}

@Composable fun PagerPageItem(offer: String, offerTitle: String, offerDescription: String) {}
