package com.example.student_project.data.network

import com.example.student_project.data.model.Category
import com.example.student_project.data.model.Course
import com.example.student_project.data.model.Instructor
import com.example.student_project.data.model.Student
import com.example.student_project.data.model.User
import com.example.student_project.data.network.request.CapturePayment
import com.example.student_project.data.network.request.CreateRatingReq
import com.example.student_project.data.network.request.ApiRequestWithCourseId
import com.example.student_project.data.network.request.ApiRequestWithInstructorId
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
    @POST("auth/login")
    suspend fun login(@Body studentLogin: StudentLogin): BaseResponse<User>

    @POST("auth/signup")
    suspend fun addStudent(@Body student: Student)

    @GET("course/getAllCourses")
    suspend fun getAllCourses(): BaseResponse<List<Course>>

    // i need to add these new feature
    @POST("course/getFullCourseDetails")
    suspend fun getFullCourseDetails(@Body courseId: ApiRequestWithCourseId): BaseResponse<Course>

    // we need to test this first
    @PUT("profile/updateProfile")
    suspend fun updateProfile(@Body student: StudentUpdateRequest): BaseResponse<User>

    @POST("payment/capturePayment")
    suspend fun capturePayment(@Body courseId: CapturePayment): BaseResponse<String>


    @POST("payment/verifyPayment")
    suspend fun verifyPayment(@Body courseId: CapturePayment): BaseResponse<Boolean>

    @POST("course/createRating")
    suspend fun createRating(@Body ratingReq: CreateRatingReq)

    //we will made it
    @POST("profile/saveCourse")
    suspend fun saveCourse(@Body courseId: ApiRequestWithCourseId)

    //this too
    @POST("course/getInstructorDetails")
    suspend fun getInstructorDetails(@Body instructorId: ApiRequestWithInstructorId): BaseResponse<Instructor>

    //we will do it too
    @GET("profile/getSavedCourses")
    suspend fun getSavedCourses(): BaseResponse<List<Course>>

    //this one will remove and we will add get all course progress
    @GET("profile/getEnrolledCourses")
    suspend fun getEnrolledCourses(): BaseResponse<List<Course>>

    @GET("course/getAllCoursesProgress")
    suspend fun getAllCourseProgress(): BaseResponse<List<Course>>


    @GET("auth/all-instructors")
    suspend fun allInstructors(): BaseResponse<List<Instructor>>

    @GET("course/showAllCategories")
    suspend fun showAllCategories(): BaseResponse<List<Category>>

    // this one not used yet
    //    @POST("auth/reset-password-token")
    //    suspend fun resetPasswordToken(@Body email:TokenReq): BaseResponse1<String>
}
