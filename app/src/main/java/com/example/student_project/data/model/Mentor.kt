package com.example.student_project.data.model

data class Mentor(
    val id: String,
    val image: String,
    val mentorName: String,
    val jopTitle: String,
    val university: String,
    val rating: Double,
    val availability: List<String>,
    val degreeAndCertificate: String,
    val timeSlots: List<String>,
    val experience: String,
    val hourlyRate: Double,
)
