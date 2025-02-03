package com.example.student_project.data.repo

import coil.network.HttpException
import com.example.student_project.data.model.Student
import com.example.student_project.data.model.User
import com.example.student_project.data.network.ApiClientFactory.apiClientForAuth
import com.example.student_project.data.network.request.StudentLogin

class StudentRepo {

    suspend fun checkUser(studentLogin: StudentLogin): Result<User?> {
        //wraper class to handle error
        val result = Result.runCatching {apiClientForAuth.login(studentLogin).data }
        return if (result.isSuccess){
            result
        }else{
            Result.failure(
                if (result.exceptionOrNull() is HttpException){
                    HttpException((result.exceptionOrNull() as HttpException).response)
                }else{
                    result.exceptionOrNull() ?: Exception("Unknown error")
                }
            )
        }
    }

    suspend fun addStudent(student: Student) {
        apiClientForAuth.addStudent(student)
    }
}
