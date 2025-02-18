package com.example.student_project.data.model

import androidx.room.Entity
import com.google.gson.annotations.SerializedName

data class Instructor(
    @SerializedName("_id") val id: String,
    val accountType: String,
    val active: Boolean,
    val approved: Boolean,
    val courses: List<Course>,
    val createdAt: String,
    val email: String,
    val firstName: String,
    val image: String,
    val savedCourses: List<Any>,
    val lastName: String,
    val password: String,
    val updatedAt: String,
    val courseProgress: List<Any>,
    val additionalDetails: AdditionalDetails,
    val students:List<User>
)



