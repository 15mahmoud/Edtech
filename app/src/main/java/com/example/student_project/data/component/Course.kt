package com.example.student_project.data.component

import androidx.compose.ui.graphics.Color

data class Course(
    val id: Int,
    val title: String,
    val img:String, //url img
    val rating: Double,
    val mentorName:String,
    val price:Double
)

//data class SearchResult(
//    val imgId: Int,
//    val mentorName: String,
//    val jopTitle: String,
//    val university: String,
//    val rating: Double,
//    val availability: List<String>,
//    val degreeAndCertificate: String,
//    val timeSlots: List<String>,
//    val experience: String,
//    val hourlyRate: Double
//)
