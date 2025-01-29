package com.example.student_project.data.component

import com.google.gson.annotations.SerializedName

data class Student(
    @SerializedName("firstName") val fullName: String,
    @SerializedName("lastName") val nickName: String,
    val email: String,
    val password: String,
    val confirmPassword: String,
    val accountType: String = "student",
    // val phoneNumber: String,
    // val dateOfBirth: String,
    // val image: String,
)
