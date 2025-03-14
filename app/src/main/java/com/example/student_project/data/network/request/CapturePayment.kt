package com.example.student_project.data.network.request


data class CapturePayment(
    val amount:Double,
    val courseId: String
)
data class VerifyPayment(
    val courseId: String
)
