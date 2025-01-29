package com.example.student_project.data.network

import com.example.student_project.data.model.LoginResponse
import com.example.student_project.data.model.Student
import com.example.student_project.data.model.StudentLogin
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface ApiClient {
    @POST("login") suspend fun checkUser(@Body studentLogin: StudentLogin): Response<LoginResponse>

    @POST("signup") suspend fun addStudent(@Body student: Student)
}
