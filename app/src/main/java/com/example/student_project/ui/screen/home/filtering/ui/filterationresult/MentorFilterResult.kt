package com.example.student_project.ui.screen.home.filtering.ui.filterationresult

import androidx.compose.animation.AnimatedVisibility
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
import androidx.compose.foundation.lazy.items
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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import com.example.student_project.data.model.Mentor
import com.example.student_project.screen.home.mentorRepo
import com.example.student_project.ui.theme.buttonColor
import com.example.student_project.ui.theme.jopTitleColor

data class MentorFilterResultScreenState(val mentor: List<Mentor>)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MentorFilterResultScreen(
    navController: NavController,
    jopTitle: String?,
    rating: Float?,
    hourlyRate: Float?,
) {
    val scope = rememberCoroutineScope()

    val state = remember { mutableStateOf(MentorFilterResultScreenState(emptyList())) }
    LaunchedEffect(scope) {
        val mentorList = mentorRepo.getMentorList()
        state.value = MentorFilterResultScreenState(mentorList)
    }
    // for showing search Text field
    var togel by remember { mutableStateOf(false) }
    val newMentorList =
        state.value.mentor.filter { mentor ->
            mentor.jopTitle == jopTitle &&
                mentor.rating >= rating!! &&
                mentor.hourlyRate <= hourlyRate!!
        }
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
                            text = "Top Mentors",
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
            LazyColumn() {
                items(newMentorList) {
                    MentorResult(
                        mentor = it,
                        onClickListener = { string ->
                            // here we will navigate to details screen based on id
                        },
                    )
                }
            }
        }
    }
}

// this string may change to list of strings
// may be we modify this and make it for course and mentor result
@Composable
fun MentorResult(mentor: Mentor, onClickListener: (String) -> Unit) {
    val configuration = LocalConfiguration.current
    val screenHeight = configuration.screenHeightDp.dp
    val screenWidth = configuration.screenWidthDp.dp
    Card(
        modifier = Modifier.fillMaxWidth().clickable { onClickListener(mentor.mentorName) },
        contentColor = Color.White,
    ) {
        Row(modifier = Modifier.fillMaxWidth()) {
            Image(
                painter = rememberAsyncImagePainter(model = mentor.image),
                contentDescription = "mentor image",
                modifier =
                    Modifier.padding(10.dp)
                        .height(screenHeight * 6 / 100)
                        .width(screenWidth * 16 / 100),
            )
            Column(modifier = Modifier.align(Alignment.CenterVertically)) {
                Text(
                    text = mentor.mentorName,
                    style = MaterialTheme.typography.headlineLarge,
                    fontSize = 18.sp,
                    color = buttonColor,
                )
                Text(
                    text = mentor.jopTitle,
                    style = MaterialTheme.typography.headlineMedium,
                    fontSize = 14.sp,
                    color = jopTitleColor,
                )
            }
        }
    }
}
