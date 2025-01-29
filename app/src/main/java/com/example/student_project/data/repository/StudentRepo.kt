package com.example.student_project.data.repository

import com.example.student_project.data.component.LoginResponse
import com.example.student_project.data.component.Student
import com.example.student_project.data.component.StudentLogin
import com.example.student_project.utils.RetrofitInstance.apiRequest
import retrofit2.Response

class StudentRepo {

    suspend fun checkUser(studentLogin: StudentLogin): Response<LoginResponse> {
        return apiRequest.checkUser(studentLogin)
    }

    suspend fun addStudent(student: Student) {
        apiRequest.addStudent(student)
    }
}
