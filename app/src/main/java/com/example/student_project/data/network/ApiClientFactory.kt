package com.example.student_project.data.network

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object ApiClientFactory {
    const val BASE_URL = "https://terrific-swamp-tilapia.glitch.me/api/v1/auth/"

    val apiClient: ApiClient by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ApiClient::class.java)
    }
}
