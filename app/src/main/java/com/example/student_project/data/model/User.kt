package com.example.student_project.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

@Entity(tableName = "student")
data class User(
    @PrimaryKey @SerializedName("_id") val id: String,
    val accountType: String,
    val active: Boolean,
    val approved: Boolean,
    val courses: List<String>,
    val createdAt: String,
    val email: String,
    var firstName: String,
    val image: String,
    var lastName: String,
    val token: String,
    val updatedAt: String,
)
