package com.example.student_project.data.model

import com.google.gson.annotations.SerializedName

data class Instructor(
    @SerializedName("_id") val id: String,
    val accountType: String,
    val active: Boolean,
    val approved: Boolean,
    val courses: List<String>,
    val createdAt: String,
    val email: String,
    val firstName: String,
    val image: String,
    val lastName: String,
    val password: String,
    val updatedAt: String,
    val courseProgress: List<Any>,
    val additionalDetails: AdditionalDetails,


    )
