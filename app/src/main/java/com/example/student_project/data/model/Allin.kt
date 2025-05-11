package com.example.student_project.data.model

import com.google.gson.annotations.SerializedName

data class Allin(
    @SerializedName("tutorial_files")
    val tutorialFiles: List<AllinContent>
)

data class AllinContent(
    @SerializedName("filename")
    val fileName: String,
    val content: String
)