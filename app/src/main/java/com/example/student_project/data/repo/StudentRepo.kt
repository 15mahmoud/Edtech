package com.example.student_project.data.repo

import coil.network.HttpException
import com.example.student_project.data.db.StudentDatabaseDao
import com.example.student_project.data.model.Student
import com.example.student_project.data.model.User
import com.example.student_project.data.network.ApiClient
import com.example.student_project.data.network.request.StudentLogin
import com.example.student_project.data.network.request.StudentUpdateRequest
import com.example.student_project.data.network.request.TokenReq
import javax.inject.Inject

class StudentRepo
@Inject
constructor(private val studentDatabaseDao: StudentDatabaseDao, private val apiClient: ApiClient) {

    suspend fun checkUser(studentLogin: StudentLogin): Result<User?> {
        // wrapper class to handle error
        val result = Result.runCatching { apiClient.login(studentLogin).data }
        return if (result.isSuccess) {
            result
        } else {
            Result.failure(
                if (result.exceptionOrNull() is HttpException) {
                    HttpException((result.exceptionOrNull() as HttpException).response)
                } else {
                    result.exceptionOrNull() ?: Exception("Unknown error")
                }
            )
        }
    }

    suspend fun addUser(student: Student) {
        apiClient.addStudent(student)
    }

    suspend fun updateProfile(student: StudentUpdateRequest): Result<User?> {
        // we will change this later
        val result = Result.runCatching { apiClient.updateProfile(student).data }
        return if (result.isSuccess) {
            val updatedStudent = result.getOrThrow()
            updatedStudent.token = studentDatabaseDao.getCurrentStudent()?.token
            studentDatabaseDao.updateStudent(updatedStudent)
            result
        } else {
            Result.failure(
                if (result.exceptionOrNull() is HttpException) {
                    HttpException((result.exceptionOrNull() as HttpException).response)
                } else {
                    result.exceptionOrNull() ?: Exception("Unknown error")
                }
            )
        }
    }

    //this one not used yet
    suspend fun resetPasswordToken(studentEmail: String): Result<String> {
        return Result.runCatching { apiClient.resetPasswordToken(TokenReq(studentEmail)).token }
    }

    suspend fun addUser(student: User) {
        studentDatabaseDao.addStudent(student)
    }

    suspend fun getAllStudents(): User? {
        return studentDatabaseDao.getCurrentStudent()
    }

    suspend fun getStudentById(id: String): User {
        return studentDatabaseDao.getStudent(id)
    }

    suspend fun updateStudent(student: User) {
        studentDatabaseDao.updateStudent(student)
    }
}
