package com.example.student_project.data

import com.example.student_project.data.component.Course

class CourseRepo {
    companion object {
        val courseList =
            listOf(
                Course(1, "Math 116", "www.vkmvk.com", 4.5, "mezo", 12.0),
                Course(2, "Arch 116", "www.vkmvk.com", 4.5, "mezo", 12.0),
            )
        val trendingCourse =
            listOf(
                Course(
                    1,
                    "Advanced Front_End Programing Techniques",
                    "www.vkmvk.com",
                    4.5,
                    "mezo",
                    12.99,
                ),
                Course(
                    2,
                    "Ultimate CyberSecurity For Beginner",
                    "www.vkmvk.com",
                    4.7,
                    "mezo",
                    12.99,
                ),
                Course(3, "Intro to PhotoGraphy and Editing", "www.vkmvk.com", 4.5, "mezo", 12.99),
            )
    }

    suspend fun getCourseList(): List<Course> {
        return courseList
    }

    suspend fun getTrendingCourse(): List<Course> {
        return trendingCourse
    }
}
