package com.example.student_project.data.model

data class Course(
    val id: String,
    val title: String,
    val difficulty: String,
    val category: String,
    val imgPath: String, // url img
    val rating: Double,
    val mentorName: String,
    val hourlyRate: Double,
    val releasedDate: String,
)
