package com.example.student_project.data.network

import java.util.concurrent.TimeUnit
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object ApiClientFactory {
    private const val BASE_URL = "https://terrific-swamp-tilapia.glitch.me/api/v1/"

    private val client = OkHttpClient.Builder().readTimeout(10, TimeUnit.SECONDS).build()

    val apiClient: ApiClient by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ApiClient::class.java)
    }


}
