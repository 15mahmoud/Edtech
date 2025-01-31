package com.example.student_project.data.repo

import com.example.student_project.data.model.Student
import com.example.student_project.data.model.User
import com.example.student_project.data.network.ApiClientFactory.apiClient
import com.example.student_project.data.network.request.StudentLogin
import com.example.student_project.data.network.response.LoginResponse
import retrofit2.Response

class StudentRepo {

    suspend fun checkUser(studentLogin: StudentLogin): Result<User?> {
        //wraper class to handle error
        return Result.runCatching { apiClient.checkUser(studentLogin).user }
    }

    suspend fun addStudent(student: Student) {
        apiClient.addStudent(student)
    }
}
