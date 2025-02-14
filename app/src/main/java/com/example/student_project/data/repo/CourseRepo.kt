package com.example.student_project.data.repo

import com.example.student_project.data.model.Category
import com.example.student_project.data.model.Course
import com.example.student_project.data.network.ApiClient
import com.example.student_project.data.network.request.CapturePayment
import com.example.student_project.data.network.request.CreateRatingReq
import com.example.student_project.data.network.request.GetFullDetailsRequest
import javax.inject.Inject

class CourseRepo @Inject constructor(private val apiClient: ApiClient) {

    suspend fun getAllCourses(): Result<List<Course>?> {
        return Result.runCatching { apiClient.getAllCourses().data }
    }

    suspend fun getFullCourseDetails(courseId: String): Result<Course?> {
        return Result.runCatching {
            apiClient.getFullCourseDetails(GetFullDetailsRequest(courseId)).data
        }
    }

    suspend fun capturePayment(courseId: List<String>): Result<String> {
        return Result.runCatching { apiClient.capturePayment(CapturePayment(courseId)).data }
    }

    suspend fun verifyPayment(courseId: List<String>): Result<Boolean> {
        return Result.runCatching { apiClient.verifyPayment(CapturePayment(courseId)).data }
    }

    suspend fun createRating(ratingReq: CreateRatingReq) {
        apiClient.createRating(ratingReq)
    }

    suspend fun showAllCategories():Result<List<Category>?>{
        return Result.runCatching { apiClient.showAllCategories().data }
    }

    //    suspend fun getCourseList(): List<Course> {
    //        return courseList
    //    }
    // we need to modify this function to sort data based on rating
    //    suspend fun getTrendingCourse(): List<Course> {
    //        return trendingCourse
    //    }
}
