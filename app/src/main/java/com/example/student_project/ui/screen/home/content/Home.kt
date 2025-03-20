package com.example.student_project.ui.screen.home.content

import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material.icons.rounded.Notifications
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
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
import com.example.student_project.data.model.Course
import com.example.student_project.data.model.Instructor
import com.example.student_project.data.model.User
import com.example.student_project.data.repo.CourseRepo
import com.example.student_project.data.repo.InstructorRepo
import com.example.student_project.data.repo.StudentRepo
import com.example.student_project.ui.navigation.Screens
import com.example.student_project.ui.screen.home.uidata.BottomNavItem
import com.example.student_project.ui.theme.darkerGrayColor
import com.example.student_project.ui.theme.lightGray
import com.example.student_project.ui.theme.starFillingColor
import com.example.student_project.util.Constant


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    navController: NavController,
    courseRepo: CourseRepo,
    studentRepo: StudentRepo,
    instructorRepo: InstructorRepo
) {
    val context = LocalContext.current
    val configuration = LocalConfiguration.current
    val screenHeight = configuration.screenHeightDp.dp
    val screenWidth = configuration.screenWidthDp.dp

    // it will be remembered if user rotate the screen
    val selectedItemIndex by rememberSaveable { mutableStateOf(0) }
    var searchState by rememberSaveable { mutableStateOf("") }
    val scrollState = rememberScrollState()
    val scope = rememberCoroutineScope()
    var instructorState by remember { mutableStateOf<Result<List<Instructor>?>?>(null) }

    var topNewCourseState by remember { mutableStateOf<Result<List<Course>?>?>(null) }
    var studentState by remember { mutableStateOf<User?>(null) }

    LaunchedEffect(scope) {
        val courseList = courseRepo.getAllCourses()
        topNewCourseState = courseList
        studentState = studentRepo.getCurrentStudent()
        //  val trendingCoursesList = courseRepo.getTrendingCourse()
        instructorState = instructorRepo.getAllInstructor()

    }

    // we will make var that be true to show api
    // if it false it will show failed to loading
    Scaffold(
        Modifier
            .fillMaxSize()
            .background(Color.White),
        topBar = {
            TopAppBar(
                title = {
                    Column(modifier = Modifier.clickable { navController.navigate(Screens.ProfileScreen.route) }) {
                        Row(modifier = Modifier.fillMaxSize()) {
                            AsyncImage(
                                ImageRequest.Builder(context)
                                    .data(studentState?.image.toString())
                                    .crossfade(true)
                                    .transformations(CircleCropTransformation())
                                    .build(),
                                contentDescription = "Profile Image",
                                Modifier
                                    .padding(end = Constant.normalPadding)
                                    .size(50.dp)
                                    .border(2.dp, color = Color.White, shape = CircleShape),
                            )
                            Column (modifier = Modifier.align(Alignment.CenterVertically)){

                                Text(
                                    text = "Hello",
                                    style = MaterialTheme.typography.headlineSmall,
                                    color = Color.Gray,
                                    fontSize = 14.sp,
                                    fontWeight = FontWeight(400),
                                )
                                Text(
                                    text =
                                    studentState?.firstName.toString() +
                                            " " +
                                            studentState?.lastName.toString(),
                                    style = MaterialTheme.typography.titleLarge,
                                    fontSize = 20.sp,
                                    fontWeight = FontWeight(700),
                                )
                            }
                        }
                    }
                },
                actions = {
                    IconButton(
                        onClick = {
                            // this will be for notification
                        }
                    ) {
                        Icon(
                            imageVector = Icons.Rounded.Notifications,
                            contentDescription = "notification icon",
                        )
                    }
                },
            )
        },
        bottomBar = { BottomNavBar(selectedItemIndex, navController) },
    ) { innerPadding ->
        Box(
            Modifier
                .fillMaxSize()
                .consumeWindowInsets(innerPadding)
                .padding(
                    paddingValues = PaddingValues(
                        top = screenHeight * 12 / 100,
                        bottom = innerPadding.calculateBottomPadding()
                    )
                )

//                .navigationBarsPadding()
//                .windowInsetsBottomHeight(WindowInsets.systemBars)
        ) {
            Column(
                Modifier
                    .padding(Constant.paddingComponentFromScreen)
                    .fillMaxSize()
                    .verticalScroll(scrollState)
            ) {
                Row(modifier = Modifier.fillMaxWidth()) {
                    TextField(
                        modifier =
                        Modifier.border(
                            width = 3.dp,
                            color = Color.Black,
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
//                    Button(
//                        onClick = { navController.navigate(Screens.MentorFilterScreen.route) },
//                        colors = ButtonDefaults.buttonColors(containerColor = Color.Black),
//                    ) {
//                        Image(
//                            painter = painterResource(id = R.drawable.move_to_filter),
//                            contentDescription = null,
//                        )
//                    }
                }
                Spacer(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(10.dp)
                )

                // we will make pager(slider)
                Box(
                    modifier =
                    Modifier
                        .clip(shape = RoundedCornerShape(32.dp))
                        // we will make black shadow with opacity 0.25
                        // .shadow(elevation = 15.dp, ambientColor = Color.Black)
                        .width(screenWidth * 91 / 100)
                        .height(screenHeight * 21 / 100)
                        .background(Color.Black)
                ) {
                    Image(
                        modifier =
                        Modifier
                            .width(screenWidth * 91 / 100)
                            .height(screenHeight * 21 / 100),
                        painter = painterResource(id = R.drawable.pager_background),
                        contentDescription = null,
                    )
                    Row(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(20.dp)
                    ) {
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
                            Modifier
                                .width(screenWidth * 36 / 100)
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
                        fontSize = 20.sp,
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight(600),
                        modifier = Modifier.padding(top = Constant.paddingComponentFromScreen),
                    )
                    Button(
                        modifier = Modifier
                            .align(Alignment.CenterEnd)
                            .padding(
                                top = Constant.smallPadding
                            ),
                        onClick = {
                            // here we write code to navigate to all subject
                            navController.navigate(Screens.TrendingCourseScreen.route)
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent),
                    ) {

                        Text(
                            text = "All subject",
                            style = MaterialTheme.typography.titleMedium,
                            fontSize = 14.sp,
                            color = darkerGrayColor
                        )
                        // here we put image for a right arrow

                    }
                }

                LazyRow(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 5.dp, bottom = 5.dp)
                ) {
                    // we will change this subject list with another list we will get from api
                    // we suppose to modify this size to match all device
                    topNewCourseState
                        ?.onSuccess {
                            it?.sortedBy { course -> course.averageRating }?.let { sortedCourse ->
                                items(sortedCourse) { course ->
                                    CourseRaw(course, context) {
                                        // navigate
                                        navController.navigate(
                                            Screens.CourseDetailScreen.route + "/${it.id}"
                                        )
                                    }
                                }
                            }
                        }
                        ?.onFailure {
                            Toast.makeText(context, "Failed to load data", Toast.LENGTH_SHORT)
                                .show()
                        }
                }
                Box(modifier = Modifier.fillMaxWidth()) {
                    Text(
                        text = "Weekly TopLive Tutors ",
                        fontSize = 20.sp,
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight(600),
                        modifier = Modifier.padding(top = Constant.paddingComponentFromScreen),
                    )

                    Button(
                        modifier = Modifier
                            .align(Alignment.CenterEnd)
                            .padding(top = Constant.smallPadding),
                        onClick = {
                            // here we write code to navigate to all mentor
                            navController.navigate(Screens.TrendingCourseScreen.route)
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent),
                    ) {

                        Text(
                            text = "All mentor",
                            style = MaterialTheme.typography.titleMedium,
                            fontSize = 14.sp,
                            color = darkerGrayColor
                        )
                        // here we put image for a right arrow

                    }
                }

                //here there are button for show all instructor
                LazyRow(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 5.dp, bottom = 5.dp)
                ) {
                    // we will change this subject list with another list we will get from api
                    // we suppose to modify this size to match all device
                    instructorState?.onSuccess { instructor ->
                        instructor?.let {
                            items(it) { mentor ->
                                InstructorRow(instructor = mentor) {
                                    navController.navigate(Screens.MentorDetailsScreen.route + "/${mentor.id}")
                                }
                            }
                        }


                    }?.onFailure {
                        Toast.makeText(context, "failed to load mentor data", Toast.LENGTH_SHORT)
                            .show()
                    }
                }
                Box(modifier = Modifier.fillMaxWidth()) {
                    Text(
                        text = "Top New Courses",
                        fontSize = 20.sp,
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight(600),
                        modifier = Modifier.padding(top = Constant.paddingComponentFromScreen),
                    )
                    Button(
                        modifier = Modifier
                            .align(Alignment.CenterEnd)
                            .padding(top = Constant.smallPadding),
                        onClick = {
                            // here we write code to navigate to all subject
                            navController.navigate(Screens.AllCourseScreen.route)
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent),
                    ) {

                        Text(
                            text = "All subject",
                            style = MaterialTheme.typography.titleMedium,
                            fontSize = 14.sp,
                            color = darkerGrayColor
                        )
                        // here we put image for a right arrow

                    }
                }

                LazyRow(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 5.dp, bottom = 5.dp)
                ) {
                    // we will change this subject list with another list we will get from api
                    // we suppose to modify this size to match all device
                    //                    itemsIndexed(state.value.courses) { index, course ->
                    //                        CourseRaw(course) {
                    //                            navController.navigate(
                    //                                Screens.CourseDetailScreen.route +
                    // "/${it._id}"
                    //                            )
                    //                        }
                    //                    }

                    topNewCourseState
                        ?.onSuccess {
                            it?.let {
                                items(it) { course ->
                                    CourseRaw(course, context) {
                                        // navigate
                                        navController.navigate(
                                            Screens.CourseDetailScreen.route + "/${it.id}"
                                        )
                                    }
                                }
                            }
                        }
                        ?.onFailure {
                            Toast.makeText(context, "Failed to load data", Toast.LENGTH_SHORT)
                                .show()
                        }
                }
            }
        }
    }
}

// i should move those 2 function from here may be i make another file for them
@OptIn(ExperimentalMaterial3Api::class)
//@Composable
//fun ScaffoldTopAppBar() {
//    val context = LocalContext.current
//}

@Composable
fun BottomNavBar(selectedState: Int, navController: NavController) {
    val imageVector = ImageVector.vectorResource(id = R.drawable.selected_courses_nav)
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
                selectedIcon = imageVector,
                unselectedIcon = ImageVector.vectorResource(id = R.drawable.courses_nav),
                label = "Coursaty",
            ),
            BottomNavItem(
                route = "your_ai_screen",
                selectedIcon = Icons.Filled.Person,
                unselectedIcon = Icons.Outlined.Person,
                label = "Your AI"
            ),
            BottomNavItem(
                route = "inbox_screen",
                selectedIcon = Icons.Filled.Person,
                unselectedIcon = Icons.Outlined.Person,
                label = "Inbox"
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
                        contentDescription = "bottom nav icon",
                    )
                },
            )
        }
    }
}

// here we will make lazy row for courses
// here we make it clickable and send a list of courses to the next screen
@Composable
fun CourseRaw(
    course: Course,
    context: Context,
    onCLickListener: (Course) -> Unit
) {
    val configuration = LocalConfiguration.current
    val screenHeight = configuration.screenHeightDp.dp
    val screenWidth = configuration.screenWidthDp.dp
    Column(
        modifier =
        Modifier
            .height(220.dp)
            .width(230.dp)
            .padding(end = Constant.normalPadding)
            .clip(RoundedCornerShape(15.dp))
            .clickable { onCLickListener(course) }
    ) {
        Card(
            modifier = Modifier
                .width(230.dp)
                .height(120.dp)
                .padding(bottom = Constant.smallPadding)
        ) {

            AsyncImage(
                model = ImageRequest.Builder(context).crossfade(true).data(course.thumbnail)
                    .build(),
                contentDescription = "course image",
                modifier = Modifier,
                contentScale = ContentScale.Crop
            )

        }


        Text(
            text = course.courseName,
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight(600),
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            fontSize = 16.sp,
            modifier = Modifier

        )
        Text(
            modifier = Modifier.padding(
                top = Constant.smallPadding,
                bottom = Constant.mediumPadding
            ),
            text = course.instructor.firstName + " ${course.instructor.lastName}",
            style = MaterialTheme.typography.titleMedium,
            color = Color(0xFF334155),
        )
        HorizontalDivider()

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = Constant.smallPadding)
        ) {

            Text(
                modifier = Modifier.padding(),
                text =
                AnnotatedString(
                    "EGP ", spanStyle = SpanStyle(color = Color(0xFF334155), fontSize = 16.sp),
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

            Row(modifier = Modifier.align(Alignment.CenterEnd)) {

                Icon(
                    imageVector = Icons.Filled.Star,
                    tint = starFillingColor,
                    contentDescription = "Avg rating",
                )

                Text(
                    // we need to change this
                    //to make number 2 point
                    text = ("%.2f".format(course.averageRating)),
                    fontSize = 12.sp,
                    modifier = Modifier.padding(start = Constant.smallPadding, top = 1.dp),
                )
            }
        }

    }
}

@Composable
fun InstructorRow(instructor: Instructor, onClick: (String) -> (Unit)) {
    val configuration = LocalConfiguration.current
    val screenHeight = configuration.screenHeightDp.dp
    val screenWidth = configuration.screenWidthDp.dp
    Card(colors = CardDefaults.cardColors(containerColor = Color.White), modifier =
    Modifier
        .width(140.dp)
        .height(136.dp)
        .padding(end = Constant.normalPadding)
        .clip(RoundedCornerShape(15.dp))
        .border(
            width = 2.dp,
            color = lightGray,
            shape = RoundedCornerShape(14.dp),
        ), onClick = { onClick(instructor.id) }) {

        Box(
            modifier = Modifier.fillMaxSize()
        ) {
            AsyncImage(
                model =
                ImageRequest.Builder(LocalContext.current)
                    .data(instructor.image)
                    .crossfade(true)
                    .transformations(CircleCropTransformation())
                    .build(),
                contentDescription = "mentor image",
                modifier =
                Modifier
                    .width(70.dp)
                    .height(70.dp)
                    .padding(
                        top = Constant.paddingComponentFromScreen,
                        bottom = Constant.normalPadding
                    )
                    .align(alignment = Alignment.TopCenter),
            )
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .align(Alignment.BottomCenter)
                    .padding(bottom = Constant.paddingComponentFromScreen),
            ) {


                Text(
                    text = instructor.firstName + " " + instructor.lastName,
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight(600),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    fontSize = 16.sp,
                    modifier =
                    Modifier.align(Alignment.CenterHorizontally)

                )
                Text(
                    modifier =
                    Modifier.align(Alignment.CenterHorizontally),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    text = instructor.additionalDetails.about.toString(),
                    style = MaterialTheme.typography.titleMedium,
                    color = Color.Gray,
                )
            }
        }
    }
}

//@Composable fun PagerPageItem(offer: String, offerTitle: String, offerDescription: String) {}
