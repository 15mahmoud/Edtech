package com.example.student_project.screen.home.filtering.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.Card
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.student_project.R
import com.example.student_project.data.SearchResult
import com.example.student_project.ui.theme.buttonColor
import com.example.student_project.ui.theme.jopTitleColor

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MentorFilterResultScreen(
    navController: NavController,
    jopTitle: String?,
    rating: Float?,
    hourlyRate: Float?,
) {
    val searchList =
        listOf(
            SearchResult(
                imgId = R.drawable.search_result_img1,
                mentorName = "Eleanor Pena",
                jopTitle = "MATH 116",
                university = "Kobenhavens",
                rating = 5.0,
                availability = listOf("Saturday", "Sunday", "Friday"),
                degreeAndCertificate = "Master's in Applied Mathematics",
                timeSlots = listOf("Morning"),
                experience = "1-3 Years",
                hourlyRate = 30.0,
            ),
            SearchResult(
                imgId = R.drawable.search_result_img2,
                mentorName = "Robert Fox",
                jopTitle = "MATH 116",
                university = "Oxford",
                rating = 4.5,
                availability = listOf("Saturday", "Wednesday", "Friday"),
                degreeAndCertificate = "Bachelor's in Applied Mathematics",
                timeSlots = listOf("Evening"),
                experience = "3-6 Years",
                hourlyRate = 30.0,
            ),
            SearchResult(
                imgId = R.drawable.profile_pics,
                mentorName = "Ihab",
                jopTitle = "ARCH 117",
                university = "Oxford",
                rating = 4.5,
                availability = listOf("Saturday", "Wednesday", "Friday"),
                degreeAndCertificate = "Bachelor's in Applied Mathematics",
                timeSlots = listOf("Evening"),
                experience = "3-6 Years",
                hourlyRate = 10.0,
            ),
            SearchResult(
                imgId = R.drawable.profile,
                mentorName = "Ramadan",
                jopTitle = "ARCH 117",
                university = "Oxford",
                rating = 4.5,
                availability = listOf("Saturday", "Wednesday", "Friday"),
                degreeAndCertificate = "Bachelor's in Applied Mathematics",
                timeSlots = listOf("Evening"),
                experience = "3-6 Years",
                hourlyRate = 20.0,
            ),
        )
    // for showing search Text field
    var togel by remember { mutableStateOf(false) }
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
                            text = "Filters",
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
            if (togel) {
                Text(text = "this will be search")
            }
            LazyColumn() {
                itemsIndexed(searchList) { index, item ->
                    if (
                        item.jopTitle == jopTitle &&
                            item.rating >= rating!! &&
                            item.hourlyRate <= hourlyRate!!
                    ) {
                        MentorResult(
                            name = item.mentorName,
                            title = item.jopTitle,
                            imgId = item.imgId,
                            onClickListener = { string ->
                                // here we will navigate to details screen
                            },
                        )
                    }
                }
            }
        }

        //            //take object from MentorFilter and show it here
    }
}

// this string may change to list of strings
// may be we modify this and make it for course and mentor result
@Composable
fun MentorResult(name: String, title: String, imgId: Int, onClickListener: (String) -> Unit) {
    val configuration = LocalConfiguration.current
    val screenHeight = configuration.screenHeightDp.dp
    val screenWidth = configuration.screenWidthDp.dp
    Card(
        modifier = Modifier.fillMaxWidth().clickable { onClickListener(name) },
        contentColor = Color.White,
    ) {
        Row(modifier = Modifier.fillMaxWidth()) {
            Image(
                painter = painterResource(id = imgId),
                contentDescription = null,
                modifier =
                    Modifier.padding(10.dp)
                        .height(screenHeight * 6 / 100)
                        .width(screenWidth * 16 / 100),
            )
            Column(modifier = Modifier.align(Alignment.CenterVertically)) {
                Text(
                    text = name,
                    style = MaterialTheme.typography.headlineLarge,
                    fontSize = 18.sp,
                    color = buttonColor,
                )
                Text(
                    text = title,
                    style = MaterialTheme.typography.headlineMedium,
                    fontSize = 14.sp,
                    color = jopTitleColor,
                )
            }
        }
    }
}
