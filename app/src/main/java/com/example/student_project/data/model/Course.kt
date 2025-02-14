package com.example.student_project.data.model

import com.google.gson.annotations.SerializedName


data class Course(
    @SerializedName("_id") val id: String,
    val category: Category,
    val courseContent: List<CourseContent>,
    val courseDescription: String,
    val courseName: String,
    val createdAt: String,
    val instructions: List<String>,
    val instructor: Instructor,
    val price: Double,
    val ratingAndReviews: List<RatingAndReview>,
    val status: String,
    val studentsEnrolled: List<String>,
    val tag: List<String>,
    val whatYouWillLearn: String,
    val thumbnail: String,
    val totalDuration: String,
    val completedVideos: List<Any>,
    val averageRating:Double?
)

data class Category(
    @SerializedName("_id") val id: String,
    val courses: List<String>,
    val description: String,
    val name: String,
)

data class CourseContent(
    @SerializedName("_id") val id: String,
    val sectionName: String,
    val subSection: List<SubSection>,
)

data class RatingAndReview(
    @SerializedName("_id") val id: String,
    val course: String,
    val rating: String,
    val review: String,
    // val user: User,
    val user: User,
)

data class SubSection(
    @SerializedName("_id") val id: String,
    val description: String,
    val timeDuration: String,
    val title: String,
    val videoUrl: String,
)
