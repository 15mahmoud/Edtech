package com.example.student_project.data.repo

import com.example.student_project.data.model.Category
import com.example.student_project.data.model.Course
import com.example.student_project.data.network.ApiClient
import com.example.student_project.data.network.request.CapturePayment
import com.example.student_project.data.network.request.CreateRatingReq
import com.example.student_project.data.network.request.ApiRequestWithCourseId
import com.example.student_project.data.network.request.VerifyPayment
import javax.inject.Inject

class CourseRepo @Inject constructor(private val apiClient: ApiClient) {

    suspend fun getAllCourses(): Result<List<Course>?> {
        return Result.runCatching { apiClient.getAllCourses().data }
    }

    suspend fun getFullCourseDetails(courseId: String): Result<Course?> {
        return Result.runCatching {
            apiClient.getFullCourseDetails(ApiRequestWithCourseId(courseId)).data
        }
    }

    suspend fun initiatePayment(capturePayment: CapturePayment): Result<String> {
        return Result.runCatching { apiClient.initiatePayment(capturePayment).data }
    }

    suspend fun getTransactionState(courseId: String): Result<String> {
        return Result.runCatching { apiClient.getTransactionState(VerifyPayment(courseId)).data }
    }

    suspend fun createRating(ratingReq: CreateRatingReq) {
        Result.runCatching {
            apiClient.createRating(ratingReq)
        }
    }

    suspend fun showAllCategories():Result<List<Category>?>{
        return Result.runCatching { apiClient.showAllCategories().data }
    }

    //we will make all those end point
    suspend fun getSavedCourses():Result<List<Course>?>{
        return Result.runCatching { apiClient.getSavedCourses().data }
    }
    suspend fun getEnrolledCourses():Result<List<Course>?>{
        return Result.runCatching { apiClient.getEnrolledCourses().data }
    }
    suspend fun savedCourse(courseId:String):Result<String>{
       return  Result.runCatching { apiClient.saveCourse(ApiRequestWithCourseId(courseId)).data }
    }

    suspend fun getAllCourseProgress():Result<List<Course>?>{
        return Result.runCatching { apiClient.getAllCourseProgress().data }
    }


}