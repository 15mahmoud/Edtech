package com.example.student_project.data.network.request

data class CreateRatingReq(
    val courseId: String,
    val rating: Int,
    val review: String
)