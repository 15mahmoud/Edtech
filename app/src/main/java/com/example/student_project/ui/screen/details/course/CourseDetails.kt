package com.example.student_project.ui.screen.details.course

import android.content.Intent
import android.net.Uri
import android.util.Log
import android.widget.Toast
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.sharp.Star
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextFieldDefaults
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
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.navigation.NavController
import coil.compose.AsyncImage
import coil.request.ImageRequest
import coil.transform.CircleCropTransformation
import com.example.student_project.R
import com.example.student_project.data.model.Course
import com.example.student_project.data.network.request.CapturePayment
import com.example.student_project.data.network.request.CreateRatingReq
import com.example.student_project.data.repo.CourseRepo
import com.example.student_project.ui.navigation.Screens
import com.example.student_project.ui.screen.widgets.LessonsColumn
import com.example.student_project.ui.screen.widgets.ReviewColumn
import com.example.student_project.ui.theme.addReviewTextColor
import com.example.student_project.ui.theme.ambientShadowColor
import com.example.student_project.ui.theme.anotherColorForFillingStar
import com.example.student_project.ui.theme.buttonColor
import com.example.student_project.ui.theme.cardContainerColor
import com.example.student_project.ui.theme.editProfileTextColor
import com.example.student_project.ui.theme.headLineColor
import com.example.student_project.ui.theme.jopTitleColor
import com.example.student_project.ui.theme.spotShadowColor
import com.example.student_project.ui.theme.starFillingColor
import com.example.student_project.ui.theme.unselectedButton
import com.example.student_project.util.Constant
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.net.URLEncoder
import java.nio.charset.StandardCharsets

@Composable
fun CourseDetailsScreen(navController: NavController, courseId: String?, courseRepo: CourseRepo) {
    var courseDetailsState by remember { mutableStateOf<Result<Course?>?>(null) }
    val scope = rememberCoroutineScope()
    val context = LocalContext.current
    val configuration = LocalConfiguration.current
    val screenHeight = configuration.screenHeightDp.dp
    val screenWidth = configuration.screenWidthDp.dp

    var aboutVisibilityState by remember { mutableStateOf(true) }
    var lessonVisibilityState by remember { mutableStateOf(false) }
    var reviewsVisibilityState by remember { mutableStateOf(false) }

    var rateNumber by remember {
        mutableStateOf(0.0)
    }

    var paymentState by remember {
        mutableStateOf<Result<String?>?>(null)
    }
    var dialogKeyForPayment by remember {
        mutableStateOf(false)
    }
    var dialogKeyForReview by remember {
        mutableStateOf(false)
    }

    var rateTextState by remember {
        mutableStateOf("")
    }
    var unLock by remember { mutableStateOf<Result<Boolean?>?>(null) }

    var getTransaction by remember { mutableStateOf<Result<String?>?>(null) }

    var lock by remember { mutableStateOf(false) }

    LaunchedEffect(scope) {
        val courseDetails = courseRepo.getFullCourseDetails(courseId.toString())
        courseDetailsState = courseDetails
    }

    courseDetailsState
        ?.onSuccess { course ->
            CoroutineScope(Dispatchers.IO).launch {
                //there is a problem here
                getTransaction = courseRepo.getTransactionState(course?.id.toString())
            }
            getTransaction?.onSuccess { it ->

                it?.let { unLockState ->
                    lock = unLockState == "paid"
                    Scaffold(
                        bottomBar = {
                            AnimatedVisibility(visible = lock) {
                                BottomAppBar(containerColor = Color.White) {
                                    Button(
                                        onClick = {
                                            dialogKeyForReview = true
                                        },
                                        shape = RoundedCornerShape(Constant.buttonRadios),
                                        modifier =
                                        Modifier
                                            .fillMaxWidth()
                                            .padding(
                                                start = Constant.normalPadding,
                                                end = Constant.normalPadding
                                            )
                                            // .clip(RoundedCornerShape(100.dp))
                                            // .border(width = 1.dp, color = buttonColor, shape =
                                            // RoundedCornerShape(99.dp))
                                            .shadow(
                                                elevation = 10.dp,
                                                RoundedCornerShape(Constant.buttonRadios),
                                                spotColor = spotShadowColor.copy(alpha = 0.4f),
                                                ambientColor = ambientShadowColor.copy(alpha = 0.35f),
                                            ),
                                        colors = ButtonDefaults.buttonColors(containerColor = buttonColor),
                                    ) {
                                        Text(
                                            text = "Add Review",
                                            color = Color.White,
                                            style = MaterialTheme.typography.titleLarge,
                                            fontSize = 20.sp,
                                            fontWeight = FontWeight(700),
                                        )
                                    }
                                }
                            }
                            AnimatedVisibility(visible = !lock) {
                                BottomAppBar(containerColor = Color.White) {
                                    Button(
                                        onClick = {
                                            dialogKeyForPayment = true
                                        },
                                        shape = RoundedCornerShape(Constant.buttonRadios),
                                        modifier =
                                        Modifier
                                            .fillMaxWidth()
                                            .padding(
                                                start = Constant.normalPadding,
                                                end = Constant.normalPadding
                                            )
                                            .shadow(
                                                elevation = 10.dp,
                                                RoundedCornerShape(Constant.buttonRadios),
                                                spotColor = spotShadowColor.copy(alpha = 0.4f),
                                                ambientColor = ambientShadowColor.copy(alpha = 0.35f),
                                            ),
                                        colors = ButtonDefaults.buttonColors(containerColor = buttonColor),
                                    ) {
                                        Text(
                                            text = "Enroll Course - $ ${course?.price.toString()}",
                                            color = Color.White,
                                            style = MaterialTheme.typography.titleLarge,
                                            fontSize = 20.sp,
                                            fontWeight = FontWeight(700),
                                        )
                                    }
                                }
                            }
                        },
                    ) { innerPadding ->

                        Column {
                            Box() {

                                AsyncImage(
                                    modifier = Modifier
                                        .width(screenWidth)
                                        .height(screenHeight * 40 / 100),
                                    //this one is so important
                                    contentScale = ContentScale.Crop,
                                    // this will change course?.thumbnail
                                    model = ImageRequest.Builder(context).crossfade(true)
                                        .data(course?.thumbnail.toString()).build(),
                                    contentDescription = "course image",
                                )
                                IconButton(
                                    onClick = { navController.popBackStack() },
                                    modifier = Modifier
                                        .align(Alignment.TopStart)
                                        .padding(
                                            top = Constant.veryLargePadding,
                                            start = Constant.normalPadding
                                        ), colors = IconButtonDefaults.iconButtonColors(
                                        containerColor = buttonColor
                                    )
                                ) {
                                    Icon(
                                        imageVector = Icons.AutoMirrored.Default.ArrowBack,
                                        modifier = Modifier
                                            .width(screenWidth * 8 / 100)
                                            .height(screenHeight * 8 / 100),
                                        tint = Color.White,
                                        contentDescription = "Arrow Back",
                                    )
                                }
                            }
                            Row(Modifier.fillMaxWidth()) {
                                Text(
                                    modifier =
                                    Modifier.padding(
                                        top = Constant.normalPadding,
                                        bottom = Constant.paddingComponentFromScreen,
                                        start = Constant.normalPadding,
                                        end = Constant.normalPadding
                                    ),
                                    text = course?.courseName.toString(),
                                    fontSize = 26.sp,
                                    color = headLineColor,
                                    style = MaterialTheme.typography.headlineLarge,
                                    fontWeight = FontWeight(700),
                                )
                                Spacer(modifier = Modifier.width(20.dp))
                                //here we will put icon
                            }
                            HorizontalDivider(
                                modifier = Modifier.padding()
                            )
                            Column(
                                modifier = Modifier
                                    .consumeWindowInsets(innerPadding)
                                    .padding(
                                        paddingValues = PaddingValues(
                                            bottom = innerPadding.calculateBottomPadding()
                                        )
                                    )
                                    .fillMaxSize()
                                    .verticalScroll(rememberScrollState())
                            ) {
                                if (dialogKeyForReview) {
                                    Dialog(onDismissRequest = { dialogKeyForReview = false }) {
                                        Card(
                                            modifier = Modifier.fillMaxSize(),
                                            colors = CardDefaults.cardColors(
                                                containerColor = Color.White
                                            )
                                        ) {

                                            Column(
                                                modifier = Modifier
                                                    .fillMaxSize()
                                                    .padding(top = Constant.paddingComponentFromScreen),
                                                horizontalAlignment = Alignment.CenterHorizontally,
                                                verticalArrangement = Arrangement.Center
                                            ) {

                                                Image(
                                                    painter = painterResource(id = R.drawable.add_review),
                                                    contentDescription = "add_review_img",
                                                    modifier = Modifier
                                                        .padding(top = Constant.paddingComponentFromScreen)
                                                        .width(screenWidth * 44 / 100)
                                                        .height(screenHeight * 21 / 100)
                                                )
                                                Text(
                                                    text = "Course Completed!",
                                                    style = MaterialTheme.typography.titleMedium,
                                                    fontWeight = FontWeight(700),
                                                    fontSize = 27.sp,
                                                    color = buttonColor,
                                                    modifier = Modifier.padding(
                                                        top = Constant.normalPadding,
                                                        bottom = Constant.normalPadding
                                                    )
                                                )
                                                Text(
                                                    text = "please leave a review for your course",
                                                    style = MaterialTheme.typography.titleMedium,
                                                    fontWeight = FontWeight(400),
                                                    fontSize = 20.sp,
                                                    color = headLineColor,
                                                    modifier = Modifier.padding(
                                                        bottom = Constant.normalPadding
                                                    )
                                                )
                                                Row(
                                                    modifier = Modifier
                                                        .fillMaxWidth()
                                                        .padding(
                                                            start = 25.dp,
                                                            end = Constant.paddingComponentFromScreen
                                                        )
                                                ) {
                                                    (1..5).forEach { number ->
                                                        IconButton(onClick = {
                                                            rateNumber = number.toDouble()
                                                        }) {
                                                            Icon(
                                                                //here we need to add green border
                                                                imageVector = if (number.toDouble() <= rateNumber) Icons.Sharp.Star else Icons.Default.Star,
                                                                tint = anotherColorForFillingStar,
                                                                contentDescription = "rating icon"
                                                            )
                                                        }
                                                    }
                                                }
                                                OutlinedTextField(
                                                    value = rateTextState,
                                                    onValueChange = {
                                                        rateTextState = it
                                                    },
                                                    colors = TextFieldDefaults.colors(
                                                        focusedContainerColor = addReviewTextColor,
                                                        unfocusedContainerColor = addReviewTextColor
                                                    ),
                                                    textStyle = MaterialTheme.typography.titleMedium,
                                                    shape = RoundedCornerShape(Constant.buttonRadios),
                                                    modifier = Modifier
                                                        .padding(
                                                            top = Constant.normalPadding,
                                                            start = Constant.normalPadding,
                                                            end = Constant.normalPadding,
                                                            bottom = Constant.normalPadding
                                                        )
                                                        .border(
                                                            2.dp,
                                                            color = buttonColor,
                                                            RoundedCornerShape(Constant.buttonRadios)
                                                        )
                                                )
                                                Button(
                                                    shape = RoundedCornerShape(Constant.buttonRadios),
                                                    colors = ButtonDefaults.buttonColors(
                                                        containerColor = buttonColor
                                                    ),
                                                    modifier = Modifier
                                                        .fillMaxWidth()
                                                        .padding(
                                                            start = Constant.normalPadding,
                                                            end = Constant.normalPadding,
                                                            bottom = Constant.normalPadding
                                                        )
                                                        .shadow(
                                                            elevation = 4.dp,
                                                            RoundedCornerShape(Constant.buttonRadios)
                                                        ),
                                                    onClick = {
                                                        val studentReview = CreateRatingReq(
                                                            course?.id.toString(),
                                                            rateNumber.toInt(),
                                                            rateTextState
                                                        )
                                                        CoroutineScope(Dispatchers.IO).launch {
                                                            courseRepo.createRating(studentReview)
                                                            //here we need to handle exception
                                                            //to save app from crashing
                                                        }
                                                        dialogKeyForReview = false
                                                    }) {
                                                    Text(
                                                        text = "Write Review",
                                                        style = MaterialTheme.typography.titleLarge,
                                                        fontWeight = FontWeight(700),
                                                        fontSize = 16.sp,
                                                        color = Color.White
                                                    )
                                                }

                                                Button(shape = RoundedCornerShape(Constant.buttonRadios),
                                                    colors = ButtonDefaults.buttonColors(
                                                        containerColor = addReviewTextColor
                                                    ),
                                                    modifier = Modifier
                                                        .fillMaxWidth()
                                                        .padding(
                                                            start = Constant.normalPadding,
                                                            end = Constant.normalPadding,
                                                            bottom = Constant.normalPadding
                                                        )
                                                        .shadow(
                                                            elevation = 4.dp,
                                                            RoundedCornerShape(Constant.buttonRadios)
                                                        ),
                                                    onClick = {
                                                        dialogKeyForReview = false
                                                    }) {
                                                    Text(
                                                        text = "Cancel",
                                                        style = MaterialTheme.typography.titleLarge,
                                                        fontWeight = FontWeight(700),
                                                        fontSize = 16.sp,
                                                        color = buttonColor
                                                    )
                                                }

                                            }
                                        }
                                    }
                                }
                                if (dialogKeyForPayment) {
                                   AlertDialog(
                                       title = {   Text(
                                           text = "continue payment",
                                           style = MaterialTheme.typography.titleLarge,
                                           fontSize = 20.sp,
                                           fontWeight = FontWeight(600),
                                           color = buttonColor
                                       )},
                                       text = {

                                               Text(
                                                   text = "click here to end payment process",
                                                   style = MaterialTheme.typography.titleMedium,
                                                   fontSize = 15.sp,
                                                   fontWeight = FontWeight(500)
                                               )

                                       },
                                       onDismissRequest = { dialogKeyForPayment = false },

                                       confirmButton = {
                                           Button(
                                           shape = RoundedCornerShape(Constant.buttonRadios),
                                           modifier = Modifier
                                               .align(Alignment.CenterHorizontally)
                                               .padding(Constant.mediumPadding)
                                               .shadow(
                                                   elevation = 4.dp,
                                                   RoundedCornerShape(Constant.buttonRadios)
                                               ),
                                           colors = ButtonDefaults.buttonColors(
                                               containerColor = buttonColor
                                           ),
                                           onClick = {

                                               val capturePayment = CapturePayment(
                                                   course?.price!!,
                                                   courseId.toString()
                                               )
                                               CoroutineScope(Dispatchers.IO).launch {
                                                   paymentState = courseRepo.initiatePayment(
                                                       capturePayment
                                                   )
                                               }
                                               paymentState?.onSuccess {
                                                   //intent to payment screen
                                                   //and when he return to this screen
                                                   //we will make get Transaction
                                                   val intent =
                                                       Intent(
                                                           Intent.ACTION_VIEW,
                                                           Uri.parse(it)
                                                       )
                                                   context.startActivity(intent)

                                                   CoroutineScope(Dispatchers.IO).launch {
                                                       getTransaction =
                                                           courseRepo.getTransactionState(
                                                               course.id
                                                           )
                                                   }
                                                   getTransaction?.onSuccess { transaction ->
                                                       if (transaction == "paid") {
                                                           lock = true
                                                           dialogKeyForPayment = false
                                                       } else {
                                                           Toast.makeText(
                                                               context,
                                                               "your payment faild",
                                                               Toast.LENGTH_SHORT
                                                           ).show()
                                                       }
                                                   }?.onFailure {
                                                       Toast.makeText(
                                                           context,
                                                           "can't verify payment",
                                                           Toast.LENGTH_SHORT
                                                       ).show()
                                                   }
                                               }?.onFailure {
                                                   Toast.makeText(
                                                       context,
                                                       "failed on your data",
                                                       Toast.LENGTH_SHORT
                                                   ).show()
                                               }

                                           }
                                       ) {
                                           Text(
                                               text = "Submit",
                                               fontSize = 22.sp,
                                               fontWeight = FontWeight(600),
                                               style = MaterialTheme.typography.titleMedium,
                                               color = Color.White
                                           )
                                       }

                                        })
                                }

                                Column(modifier = Modifier.padding()) {


                                    LazyRow(modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(top = Constant.normalPadding)) {
                                        course?.let {
                                            items(it.tag) { item ->
                                                Card(
                                                    modifier = Modifier
//                                                    .height(screenHeight * 6/100)
                                                        .padding(
                                                            bottom = Constant.mediumPadding,
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
                                                        text = item,
                                                        color = buttonColor,
                                                        fontSize = 10.sp,
                                                        style = MaterialTheme.typography.titleLarge,
                                                        fontWeight = FontWeight(600),
                                                    )
                                                }
                                            }
                                        }
                                    }
                                    Row(modifier = Modifier.fillMaxWidth()) {
                                        Text(
                                            //this will change later
                                            text = "EGP " + course?.price.toString(),
                                            style = MaterialTheme.typography.headlineLarge,
                                            fontSize = 32.sp,
                                            color = buttonColor,
                                            fontWeight = FontWeight(700),
                                            modifier =
                                            Modifier.padding(
//                                            top = Constant.mediumPadding,
//                                            bottom = Constant.mediumPadding,
                                                start = Constant.normalPadding,
                                                end = Constant.normalPadding,
                                            ),
                                        )
                                        Spacer(modifier = Modifier.width(30.dp))
                                        Icon(
                                            modifier =
                                            Modifier
                                                .size(30.dp)
                                                .padding(
                                                    start = Constant.normalPadding,
//                                            bottom = Constant.mediumPadding,
                                                    top = Constant.smallPadding
                                                ),
                                            imageVector = Icons.Filled.Star,
                                            tint = starFillingColor,
                                            contentDescription = "rating icon",
                                        )
                                        Text(
                                            modifier =
                                            Modifier.padding(
                                                start = Constant.mediumPadding,
//                                            bottom = Constant.paddingComponentFromScreen,
                                                top = Constant.normalPadding
                                            ),
                                            text = (course?.averageRating ?: 0.0).toString(),
                                            color = editProfileTextColor,
                                            style = MaterialTheme.typography.titleMedium,
                                            fontSize = 16.sp,
                                        )
                                    }
                                    Row(modifier = Modifier.fillMaxWidth()) {
                                        Icon(
                                            modifier =
                                            Modifier.padding(
                                                start = Constant.normalPadding,
                                                bottom = Constant.normalPadding,
                                                end = Constant.mediumPadding
                                            ),
                                            imageVector =
                                            ImageVector.vectorResource(id = R.drawable.add_friends),
                                            tint = buttonColor,
                                            contentDescription = "participant",
                                        )
                                        Text(
                                            text = course?.studentsEnrolled?.size.toString() + " students",
                                            style = MaterialTheme.typography.titleMedium,
                                            color = editProfileTextColor,
                                            fontSize = 16.sp,
                                            fontWeight = FontWeight(500),
                                            modifier = Modifier.padding(
                                                top = Constant.smallPadding,
                                                bottom = Constant.paddingComponentFromScreen,
                                                end = Constant.normalPadding
                                            ),
                                        )
                                        Icon(
                                            modifier = Modifier.padding(
                                                start = Constant.paddingComponentFromScreen,
                                                top = Constant.smallPadding,
                                                bottom = Constant.mediumPadding,
                                                end = Constant.mediumPadding
                                            ),
                                            imageVector =
                                            ImageVector.vectorResource(id = R.drawable.watch),
                                            tint = buttonColor,
                                            contentDescription = "time icon",
                                        )
                                        Text(
                                            text = course?.totalDuration.toString(),
                                            style = MaterialTheme.typography.titleMedium,
                                            color = editProfileTextColor,
                                            fontSize = 16.sp,
                                            fontWeight = FontWeight(500),
                                            modifier = Modifier.padding(
                                                top = Constant.smallPadding,
                                                bottom = Constant.paddingComponentFromScreen,
                                                end = Constant.normalPadding
                                            ),
                                        )
                                    }
                                    HorizontalDivider()
                                    Row(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(
//                                            bottom = 12.dp
                                            ),
                                        horizontalArrangement =
                                        Arrangement.spacedBy(
                                            Constant.normalPadding,
                                            alignment = Alignment.CenterHorizontally,
                                        ),
                                    ) {
                                        Button(
                                            onClick = {
                                                if (!aboutVisibilityState) {
                                                    aboutVisibilityState = true
                                                    lessonVisibilityState = false
                                                    reviewsVisibilityState = false
                                                } else {
                                                    lessonVisibilityState = false
                                                    reviewsVisibilityState = false
                                                }
                                            },
                                            colors =
                                            ButtonDefaults.buttonColors(containerColor = Color.Transparent),
                                        ) {
                                            Text(
                                                text = "About",
                                                fontSize = 18.sp,
                                                style = MaterialTheme.typography.titleMedium,
                                                color =
                                                if (aboutVisibilityState) buttonColor else unselectedButton,
                                            )
                                        }
                                        Button(
                                            onClick = {
                                                if (!lessonVisibilityState) {
                                                    aboutVisibilityState = false
                                                    lessonVisibilityState = true
                                                    reviewsVisibilityState = false
                                                } else {
                                                    aboutVisibilityState = false
                                                    reviewsVisibilityState = false
                                                }
                                            },
                                            colors =
                                            ButtonDefaults.buttonColors(containerColor = Color.Transparent),
                                        ) {
                                            Text(
                                                text = "Lessons",
                                                fontSize = 18.sp,
                                                style = MaterialTheme.typography.titleMedium,
                                                color =
                                                if (lessonVisibilityState) buttonColor else unselectedButton,
                                            )
                                        }
                                        Button(
                                            onClick = {

                                                if ((course?.averageRating
                                                        ?: 0.0).toString() != "0.0"
                                                ) {

                                                    if (!reviewsVisibilityState) {
                                                        aboutVisibilityState = false
                                                        lessonVisibilityState = false
                                                        reviewsVisibilityState = true
                                                    } else {
                                                        lessonVisibilityState = false
                                                        aboutVisibilityState = false
                                                    }
                                                } else {
                                                    Toast.makeText(
                                                        context,
                                                        "no reviews",
                                                        Toast.LENGTH_SHORT
                                                    ).show()
                                                }
                                            },
                                            colors =
                                            ButtonDefaults.buttonColors(containerColor = Color.Transparent),
                                        ) {
                                            Text(
                                                text = "Reviews",
                                                fontSize = 18.sp,
                                                style = MaterialTheme.typography.titleMedium,
                                                color =
                                                if (reviewsVisibilityState) buttonColor
                                                else unselectedButton,
                                            )
                                        }
                                    }
                                    HorizontalDivider(
                                        modifier = Modifier.padding(
                                            bottom = Constant.normalPadding
                                        )
                                    )
                                    AnimatedVisibility(visible = aboutVisibilityState) {
                                        Column(modifier = Modifier) {
                                            Text(
                                                modifier = Modifier.padding(
                                                    start = Constant.normalPadding
                                                ),
                                                text = "Mentor",
                                                style = MaterialTheme.typography.titleMedium,
                                                fontSize = 20.sp,
                                                fontWeight = FontWeight(700),
                                            )
                                            TextButton(
                                                colors =
                                                ButtonDefaults.buttonColors(
                                                    containerColor = Color.Transparent
                                                ),
                                                onClick = {
                                                    navController.navigate(Screens.MentorDetailsScreen.route + "/${course?.instructor?.id}")
                                                },
                                            ) {
                                                Row(modifier = Modifier.fillMaxWidth()) {
                                                    AsyncImage(
                                                        model =
                                                        ImageRequest.Builder(context)
                                                            .crossfade(true)
                                                            // this will be photo
                                                            // course?.instructor?.image.toString()
                                                            .data(course?.instructor?.image.toString())
                                                            .transformations(
                                                                CircleCropTransformation()
                                                            )
                                                            .build(),
                                                        contentDescription = "mentor image",
                                                        modifier =
                                                        Modifier
                                                            .padding(end = Constant.mediumPadding)
                                                            .align(Alignment.CenterVertically)
                                                            .width(screenWidth * 14 / 100)
                                                            .height(screenHeight * 7 / 100),
                                                    )
                                                    Column(modifier = Modifier) {
                                                        Text(
                                                            text =
                                                            course?.instructor?.firstName.toString() +
                                                                    " " +
                                                                    course?.instructor?.lastName.toString(),
                                                            style = MaterialTheme.typography.titleMedium,
                                                            color = headLineColor,
                                                            fontWeight = FontWeight(700),
                                                            fontSize = 18.sp,
                                                            modifier =
                                                            Modifier.padding(
                                                                start = Constant.verySmallPadding,
                                                                bottom = Constant.smallPadding
                                                            ),
                                                        )
                                                        Text(
                                                            text =
                                                            course
                                                                ?.instructor
                                                                ?.additionalDetails
                                                                ?.about
                                                                .toString(),
                                                            color = jopTitleColor,
                                                            style = MaterialTheme.typography.titleMedium,
                                                            fontSize = 14.sp,
                                                            fontWeight = FontWeight(500),
                                                            //                                          maxLines
                                                            // = 2,
                                                            //                                          overflow
                                                            // = TextOverflow.Ellipsis,
                                                            //  softWrap = true,
                                                            modifier = Modifier.padding(start = Constant.verySmallPadding),
                                                        )
                                                    }
                                                }
                                            }
                                            Text(
                                                modifier = Modifier.padding(
                                                    start = Constant.normalPadding,
                                                    bottom = Constant.mediumPadding
                                                ),
                                                text = "About Course",
                                                style = MaterialTheme.typography.titleMedium,
                                                fontSize = 20.sp,
                                                fontWeight = FontWeight(700),
                                            )
                                            Text(
                                                text = course?.courseDescription.toString(),
                                                style = MaterialTheme.typography.headlineSmall,
                                                fontSize = 14.sp,
                                                modifier = Modifier.padding(
                                                    start = Constant.normalPadding,
                                                    end = Constant.paddingComponentFromScreen,
                                                    bottom = Constant.paddingComponentFromScreen
                                                ),
                                            )
                                        }
                                    }
                                    AnimatedVisibility(visible = lessonVisibilityState) {
                                        Column {
                                            // size -> no of section
                                            Text(
                                                text =
                                                course?.courseContent?.size.toString() + " " + "Sections",
                                                style = MaterialTheme.typography.titleMedium,
                                                color = headLineColor,
                                                fontWeight = FontWeight(700),
                                                fontSize = 20.sp,
                                                modifier = Modifier.padding(
                                                    top = Constant.paddingComponentFromScreen,
                                                    bottom = Constant.paddingComponentFromScreen,
                                                    start = Constant.normalPadding,
                                                    end = Constant.normalPadding
                                                ),
                                            )

                                            LazyColumn(modifier = Modifier.height(500.dp)) {
                                                course?.let { courses ->
                                                    itemsIndexed(courses.courseContent) { index, item ->
                                                        Text(
                                                            text =
                                                            "Section" +
                                                                    " " +
                                                                    (index + 1).toString() +
                                                                    " " +
                                                                    item.sectionName,
                                                            maxLines = 1,
                                                            overflow = TextOverflow.Ellipsis,
                                                            style = MaterialTheme.typography.titleMedium,
                                                            color = jopTitleColor,
                                                            fontWeight = FontWeight(700),
                                                            fontSize = 18.sp,
                                                            modifier =
                                                            Modifier.padding(
                                                                start = Constant.normalPadding,
                                                                bottom = Constant.moreNormalPadding,
                                                            ),
                                                        )
                                                        LazyColumn(modifier = Modifier.height(250.dp)) {
                                                            itemsIndexed(item.subSection) { subsectionIndex,
                                                                                            subSection ->
                                                                Card() {
                                                                    LessonsColumn(
                                                                        subSection = subSection,
                                                                        index = subsectionIndex,
                                                                        lock = !lock,
                                                                        context = context,
                                                                    ) {url,id->
                                                                        val encodedUrl =
                                                                            URLEncoder.encode(
                                                                                url,
                                                                                StandardCharsets.UTF_8
                                                                                    .toString(),
                                                                            )
                                                                        navController.navigate(
                                                                            Screens.CourseLessonScreen.route +
                                                                                    "/${encodedUrl}" + "/${courseId.toString()}" + "/${id}"
                                                                        )
                                                                    }
                                                                }
                                                            }
                                                        }
                                                    }
                                                }
                                            }
                                        }
                                    }

                                    AnimatedVisibility(visible = reviewsVisibilityState) {
                                        Column {
                                            Row(
                                                modifier = Modifier
                                                    .fillMaxWidth()
                                                    .padding(
                                                        top = Constant.paddingComponentFromScreen,
                                                        bottom = Constant.paddingComponentFromScreen,
                                                        start = Constant.normalPadding,
                                                        end = Constant.normalPadding
                                                    )
                                            ) {
                                                Icon(
                                                    imageVector = Icons.Default.Star,
                                                    tint = starFillingColor,
                                                    contentDescription = "rating icon",
                                                    modifier = Modifier.padding(end = Constant.smallPadding),
                                                )
                                                Text(
                                                    text = (course?.averageRating
                                                        ?: 0.0).toString(),
                                                    style = MaterialTheme.typography.titleLarge,
                                                    fontWeight = FontWeight(700),
                                                    color = buttonColor,
                                                )
                                            }
                                            LazyColumn(modifier = Modifier.height(500.dp)) {
                                                course?.let { item ->
                                                    items(item.ratingAndReviews) { rate ->
                                                        ReviewColumn(
                                                            ratingAndReview = rate,
                                                            context = context
                                                        )
                                                        HorizontalDivider(
                                                            modifier =
                                                            Modifier.padding(
                                                                start = Constant.normalPadding,
                                                                end = Constant.normalPadding
                                                            )
                                                        )
                                                    }
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }?.onFailure {
                Toast.makeText(context, "failed to verify payment", Toast.LENGTH_SHORT).show()
                Log.d("details", it.message.toString())
            }
        }
        ?.onFailure {
            Log.d("details", it.message.toString())
            Toast.makeText(context, "failed to load data", Toast.LENGTH_SHORT).show()
        }
}

