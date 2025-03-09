package com.example.student_project.data.network.request

data class ApiBodyForResetPassword(
    val token:String,
    val password:String,
    val confirmPassword:String
)
