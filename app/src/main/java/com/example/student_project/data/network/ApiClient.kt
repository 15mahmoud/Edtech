package com.example.student_project.data.network

import com.example.student_project.data.model.Student
import com.example.student_project.data.network.request.StudentLogin
import com.example.student_project.data.network.response.LoginResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface ApiClient {
    //we will this response to make default attribute for user
    //user can change it later
    //by using put Request
    @POST("login") suspend fun checkUser(@Body studentLogin: StudentLogin): LoginResponse

    @POST("signup") suspend fun addStudent(@Body student: Student)
}
