package com.example.student_project.data.model

import com.google.gson.annotations.SerializedName


data class Categories(
    @SerializedName("_id")
    val id: String,
    val description: String,
    val name: String
)