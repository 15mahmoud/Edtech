package com.example.student_project.data.model

import com.google.gson.annotations.SerializedName

data class Allin(
    @SerializedName("tutorial_files")
    val tutorialFiles: List<AllinContent>,
    @SerializedName("repo_url")
    val repoUrl:String
)

data class AllinContent(
    @SerializedName("filename")
    val fileName: String,
    val content: String
)



data class AiChatHistoryResponse(
    val role:String,
    val content: String,
    val timestamp:String
)