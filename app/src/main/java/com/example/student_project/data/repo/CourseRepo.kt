package com.example.student_project.data.repo

import com.example.student_project.data.model.Course
import com.example.student_project.data.network.ApiClientFactory.apiClient
import javax.inject.Inject

class CourseRepo @Inject constructor(){

    suspend fun getAllCourses(): Result<List<Course>?> {
        return Result.runCatching { apiClient.getAllCourses().data }
    }
    suspend fun getFullCourseDetails(courseId: String): Result<Course?> {
        return Result.runCatching { apiClient.getFullCourseDetails(courseId).data }
    }

    //    suspend fun getCourseList(): List<Course> {
    //        return courseList
    //    }
    // we need to modify this function to sort data based on rating
    //    suspend fun getTrendingCourse(): List<Course> {
    //        return trendingCourse
    //    }
}
