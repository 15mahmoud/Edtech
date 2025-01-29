package com.example.student_project.data.network.response

import com.example.student_project.data.model.User

data class LoginResponse(
    val message: String,
    val success: Boolean,
    val token: String,
    val user: User,
)