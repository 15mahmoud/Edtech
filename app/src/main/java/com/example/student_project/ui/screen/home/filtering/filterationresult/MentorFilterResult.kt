package com.example.student_project.ui.screen.home.filtering.filterationresult

import android.widget.Toast
import androidx.compose.animation.AnimatedVisibility
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
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.student_project.data.model.Instructor
import com.example.student_project.data.repo.InstructorRepo
import com.example.student_project.data.repo.StudentRepo
import com.example.student_project.ui.screen.widgets.MentorColumn


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MentorFilterResultScreen(
    navController: NavController,
    studentRepo: StudentRepo,
    jopTitle: String?,
    rating: Float?,
    hourlyRate: Float?,
    instructorRepo: InstructorRepo
) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()

    var instructorState by remember { mutableStateOf<Result<List<Instructor>?>?>(null) }
    LaunchedEffect(scope) {
        instructorState = instructorRepo.getAllInstructor()
    }
    // for showing search Text field
    var togel by remember { mutableStateOf(false) }
//    val newMentorList =
//        state.value.mentor.filter { mentor ->
//            mentor.jopTitle == jopTitle &&
//                mentor.rating >= rating!! &&
//                mentor.hourlyRate <= hourlyRate!!
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
                instructorState?.onSuccess { instructor ->
                    instructor?.let { mentor ->
                        items(mentor) {
                            MentorColumn(
                                navController,
                                instructor = it,
                                studentRepo,
                                context,
                                onClickListener = { string ->
                                    // here we will navigate to details screen based on id
                                },
                            )
                        }

                    }
                }?.onFailure {
                    Toast.makeText(context, "failed to load data", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
}

