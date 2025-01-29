package com.example.student_project.network

import com.example.student_project.data.component.LoginResponse
import com.example.student_project.data.component.Student
import com.example.student_project.data.component.StudentLogin
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface LogApiRequest {
    @POST("login") suspend fun checkUser(@Body studentLogin: StudentLogin): Response<LoginResponse>

    @POST("signup") suspend fun addStudent(@Body student: Student)
}
