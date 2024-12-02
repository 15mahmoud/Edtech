package com.example.student_project.screen.home.filtering

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import com.example.student_project.R
import com.example.student_project.data.SearchResult
import com.example.student_project.screen.Screens

@Composable
fun MentorFilterResultScreen(navController: NavController) {
    val searchList = listOf(
        SearchResult(
            imgId = R.drawable.search_result_img1,
            mentorName = "Eleanor Pena",
            subject = "MAT 116",
            university = "Kobenhavens",
            rating = 5.0,
            availability = listOf("Saterday", "Sunday", "Friday"),
            degreeAndCertificate = "Master's in Applied Mathematics",
            timeSlots = listOf("Morning"),
            experience = "1-3 Years",
            hourlyRate = 30.0
        ),
        SearchResult(
            imgId = R.drawable.search_result_img2,
            mentorName = "Robert Fox",
            subject = "ARCH 117",
            university = "Oxford",
            rating = 4.5,
            availability = listOf("Saterday", "Wednesday", "Friday"),
            degreeAndCertificate = "Bachelor's in Applied Mathematics",
            timeSlots = listOf("Evening"),
            experience = "3-6 Years",
            hourlyRate = 30.0
        )
    )
    Scaffold(
        topBar = {
            ScaffoldFilterScreenTopBar(
                navController = navController,
                route = Screens.MentorFilterScreen.route
            )
        }
    ) { innerPadding ->
        LazyColumn(
            Modifier
                .fillMaxSize()
                .padding(innerPadding)) {
            //take object from MentorFilter and show it here
        }

    }
}