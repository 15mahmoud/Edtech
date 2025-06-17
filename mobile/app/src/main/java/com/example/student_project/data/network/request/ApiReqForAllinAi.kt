package com.example.student_project.data.network.request

import com.google.gson.annotations.SerializedName

data class ApiReqForAllinAi(
    @SerializedName("repo_url")
    private val repoURL: String
)
data class ApiReqForChatBootAi(
    private val message: String
)
