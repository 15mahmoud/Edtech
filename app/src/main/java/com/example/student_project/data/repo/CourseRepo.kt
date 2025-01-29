package com.example.student_project.data.repo

import com.example.student_project.data.model.Course

class CourseRepo {
    companion object {
        val courseList =
            listOf(
                Course(
                    "1",
                    "Math 116",
                    "beginner",
                    "programming",
                    "https://i.redd.it/spgt1hclj2cd1.jpeg",
                    4.5,
                    "mezo",
                    12.0,
                    "inLessThan3Months",
                ),
                Course(
                    "2",
                    "Arch 116",
                    "advanced",
                    "programming",
                    "https://i.redd.it/spgt1hclj2cd1.jpeg",
                    4.5,
                    "mezo",
                    12.0,
                    "inLessThan6Months",
                ),
            )
        val trendingCourse =
            listOf(
                Course(
                    "1",
                    "Advanced Front_End Programing Techniques",
                    "beginner",
                    "programming",
                    "https://i.redd.it/spgt1hclj2cd1.jpeg",
                    4.5,
                    "mezo",
                    12.99,
                    "inLessThan1year",
                ),
                Course(
                    "2",
                    "Ultimate CyberSecurity For Beginner",
                    "beginner",
                    "programming",
                    "https://i.redd.it/spgt1hclj2cd1.jpeg",
                    4.7,
                    "mezo",
                    12.99,
                    "inLessThan1year",
                ),
                Course(
                    "3",
                    "Intro to PhotoGraphy and Editing",
                    "advanced",
                    "programming",
                    "https://alteoxstreamimages.b-cdn.net/movie-cover/vc/9/45084999-cover.jpg",
                    4.5,
                    "mezo",
                    12.99,
                    "inLessThan1year",
                ),
            )
    }

    suspend fun getCourseList(): List<Course> {
        return courseList
    }

    suspend fun getTrendingCourse(): List<Course> {
        return trendingCourse
    }
}
