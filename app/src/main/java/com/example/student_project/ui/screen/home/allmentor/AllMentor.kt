package com.example.student_project.ui.screen.home.allmentor

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.IconButton
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.student_project.R
import com.example.student_project.data.model.Instructor
import com.example.student_project.data.repo.InstructorRepo
import com.example.student_project.data.repo.StudentRepo
import com.example.student_project.ui.navigation.Screens
import com.example.student_project.ui.screen.widgets.MentorColumn
import com.example.student_project.util.Constant

@Composable
fun AllMentorScreen(navController: NavHostController,studentRepo: StudentRepo,instructorRepo: InstructorRepo) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()

    var instructorState by remember { mutableStateOf<Result<List<Instructor>?>?>(null) }
    LaunchedEffect(scope) {
        instructorState = instructorRepo.getAllInstructor()
    }
    // for showing search Text field
//    var togel by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = Constant.paddingWithoutScaffold)
        ) {
            IconButton(onClick = { navController.popBackStack() }) {
                Image(
                    painter = painterResource(id = R.drawable.baseline_arrow_back_24),
                    contentDescription = null,
                )
            }
            Text(
                text = "Top Mentors",
                modifier = Modifier.padding(top = Constant.mediumPadding),
                style = MaterialTheme.typography.headlineLarge,
                fontSize = 24.sp,
                fontWeight = FontWeight(700),
            )
        }
        LazyColumn(modifier = Modifier.background(color = Color.White)){
            instructorState?.onSuccess {nullableInstructor->
                nullableInstructor?.let {notNullableInstructor->
                    items(notNullableInstructor){
                        MentorColumn(navController,instructor = it,studentRepo,context, onClickListener = { id ->
                            navController.navigate(Screens.MentorDetailsScreen.route + "/$id")
                        })
                    }
                }
            }
        }
    }
}