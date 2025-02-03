package com.example.student_project.data.network

import com.example.student_project.data.model.Course
import com.example.student_project.data.model.Student
import com.example.student_project.data.model.User
import com.example.student_project.data.network.request.StudentLogin
import com.example.student_project.data.network.response.BaseResponse
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface ApiClient {
    //we will this response to make default attribute for user
    //user can change it later
    //by using put Request
    @POST("login")
    suspend fun login(@Body studentLogin: StudentLogin): BaseResponse<User>

    @POST("signup")
    suspend fun addStudent(@Body student: Student)

    @GET("getAllCourses")
    suspend fun getAllCourses(): BaseResponse<List<Course>>

    @POST("getFullCourseDetails")
    suspend fun getFullCourseDetails(@Body courseId: String): BaseResponse<Course>
}
