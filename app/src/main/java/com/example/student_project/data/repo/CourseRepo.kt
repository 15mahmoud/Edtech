package com.example.student_project.data.repo

import com.example.student_project.data.model.Course
import com.example.student_project.data.network.ApiClientFactory.apiClientForCourses

class CourseRepo {


    suspend fun getAllCourses(): Result<List<Course>?> {
    return Result.runCatching { apiClientForCourses.getAllCourses().data }
    }

//    suspend fun getCourseList(): List<Course> {
//        return courseList
//    }
//we need to modify this function to sort data based on rating
//    suspend fun getTrendingCourse(): List<Course> {
//        return trendingCourse
//    }
}
