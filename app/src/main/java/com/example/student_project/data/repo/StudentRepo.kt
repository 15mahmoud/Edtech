package com.example.student_project.data.repo

import com.example.student_project.data.model.LoginResponse
import com.example.student_project.data.model.Student
import com.example.student_project.data.model.StudentLogin
import com.example.student_project.data.network.ApiClientFactory.apiClient
import retrofit2.Response

class StudentRepo {

    suspend fun checkUser(studentLogin: StudentLogin): Response<LoginResponse> {
        return apiClient.checkUser(studentLogin)
    }

    suspend fun addStudent(student: Student) {
        apiClient.addStudent(student)
    }
}
