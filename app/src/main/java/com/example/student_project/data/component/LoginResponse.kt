package com.example.student_project.data.component

data class LoginResponse(
    val message: String,
    val success: Boolean,
    val token: String,
    val user: User,
)

data class User(
    val accountType: String,
    val active: Boolean,
    val additionalDetails: AdditionalDetails,
    val approved: Boolean,
    val courseProgress: List<Any>,
    val courses: List<Any>,
    val createdAt: String,
    val email: String,
    val firstName: String,
    val image: String,
    val lastName: String,
    val resetPasswordTokenExpires: String,
    val token: String,
    val updatedAt: String,
)

data class AdditionalDetails(
    val about: Any,
    val contactNumber: Any,
    val dateOfBirth: Any,
    val gender: Any,
)
