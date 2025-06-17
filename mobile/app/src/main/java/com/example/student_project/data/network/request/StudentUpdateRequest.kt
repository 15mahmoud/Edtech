package com.example.student_project.data.network.request

data class StudentUpdateRequest(
    var about: String?,
    var contactNumber: String?,
    val dateOfBirth: String?,
    var firstName: String,
    var gender: String?,
    var lastName: String,
)
