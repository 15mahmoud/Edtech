package com.example.student_project.data.network

import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

object ApiClientFactory {
    private const val BASE_URL_FOR_AUTH = "https://terrific-swamp-tilapia.glitch.me/api/v1/auth/"

    private const val BASE_URL_FOR_COURSES =
        "https://terrific-swamp-tilapia.glitch.me/api/v1/course/"

    private val client = OkHttpClient.Builder()
        .readTimeout(10, TimeUnit.SECONDS)
        .build()

    val apiClientForAuth: ApiClient by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL_FOR_AUTH)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ApiClient::class.java)
    }
    val apiClientForCourses: ApiClient by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL_FOR_COURSES)
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ApiClient::class.java)
    }
}
