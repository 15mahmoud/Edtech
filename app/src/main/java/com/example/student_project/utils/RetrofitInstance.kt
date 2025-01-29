package com.example.student_project.utils

import com.example.student_project.network.LogApiRequest
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitInstance {
    const val BASE_URL = "https://terrific-swamp-tilapia.glitch.me/api/v1/auth/"

    val apiRequest: LogApiRequest by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(LogApiRequest::class.java)
    }
}
