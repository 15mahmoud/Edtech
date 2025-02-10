package com.example.student_project.data.network

import com.example.student_project.data.model.Course
import com.example.student_project.data.model.Student
import com.example.student_project.data.model.User
import com.example.student_project.data.network.request.GetFullDetailsRequest
import com.example.student_project.data.network.request.StudentLogin
import com.example.student_project.data.network.request.StudentUpdateRequest
import com.example.student_project.data.network.response.BaseResponse
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT

interface ApiClient {
    // we will this response to make default attribute for user
    // user can change it later
    // by using put Request
    @POST("auth/login") suspend fun login(@Body studentLogin: StudentLogin): BaseResponse<User>

    @POST("auth/signup") suspend fun addStudent(@Body student: Student)

    @GET("course/getAllCourses") suspend fun getAllCourses(): BaseResponse<List<Course>>

    // i need to add these new feature
    @POST("course/getFullCourseDetails")
    suspend fun getFullCourseDetails(@Body courseId: GetFullDetailsRequest): BaseResponse<Course>

    // we need to test this first
    @PUT("profile/updateProfile")
    suspend fun updateProfile(@Body student: StudentUpdateRequest): BaseResponse<User>
}
