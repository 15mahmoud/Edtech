package com.example.student_project.data.component

data class Course(
    val id: String,
    val title: String,
    val difficulty: String,
    val category: String,
    val imgPath: String, // url img
    val rating: Double,
    val mentorName: String,
    val hourlyRate: Double,
    val releasedDate:String
)

// data class SearchResult(
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
// )
