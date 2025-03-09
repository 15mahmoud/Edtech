package com.example.student_project.data.network

import com.example.student_project.data.model.Category
import com.example.student_project.data.model.ChattingRoom
import com.example.student_project.data.model.Course
import com.example.student_project.data.model.InboxChat
import com.example.student_project.data.model.Instructor
import com.example.student_project.data.model.Meeting
import com.example.student_project.data.model.Message
import com.example.student_project.data.model.Student
import com.example.student_project.data.model.User
import com.example.student_project.data.network.request.ApiBodyForResetPassword
import com.example.student_project.data.network.request.ApiReqForChat
import com.example.student_project.data.network.request.ApiReqForMessageInChat
import com.example.student_project.data.network.request.ApiReqForSendingMessage
import com.example.student_project.data.network.request.CapturePayment
import com.example.student_project.data.network.request.CreateRatingReq
import com.example.student_project.data.network.request.ApiRequestWithCourseId
import com.example.student_project.data.network.request.ApiRequestWithInstructorId
import com.example.student_project.data.network.request.StudentLogin
import com.example.student_project.data.network.request.StudentUpdateRequest
import com.example.student_project.data.network.request.TokenReq
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

    @POST("course/getInstructorDetails")
    suspend fun getInstructorDetails(@Body instructorId: ApiRequestWithInstructorId): BaseResponse<Instructor>


    @POST("course/createChat")
    suspend fun createChat(@Body participantId: ApiReqForChat): BaseResponse<ChattingRoom>

    @POST("course/sendMessage")
    suspend fun sendMessage(@Body content:ApiReqForSendingMessage):BaseResponse<Message>

    @GET("course/getAllChats")
    suspend fun getAllChat(): BaseResponse<List<InboxChat>>

    @POST("course/getMessages")
    suspend fun getMessages(@Body chatId:ApiReqForMessageInChat): BaseResponse<List<Message>>

    @GET("meeting/all")
    suspend fun getAllMeeting(): BaseResponse<List<Meeting>>

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

    // this one need to change
        @POST("auth/reset-password-token")
        suspend fun resetPasswordToken(@Body email: TokenReq): BaseResponse<String>

        //here we need some change too
        @POST("auth/reset-password")
        suspend fun resetPassword(@Body apiBodyForResetPassword: ApiBodyForResetPassword): BaseResponse<String>
}
