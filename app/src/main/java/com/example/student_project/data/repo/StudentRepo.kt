package com.example.student_project.data.repo

import coil.network.HttpException
import com.example.student_project.data.db.StudentDatabaseDao
import com.example.student_project.data.model.Student
import com.example.student_project.data.model.User
import com.example.student_project.data.network.ApiClientFactory.apiClientForAuth
import com.example.student_project.data.network.request.StudentLogin
import java.util.UUID
import javax.inject.Inject

class StudentRepo @Inject constructor(private val studentDatabaseDao: StudentDatabaseDao) {

    suspend fun checkUser(studentLogin: StudentLogin): Result<User?> {
        //wrapper class to handle error
        val result = Result.runCatching { apiClientForAuth.login(studentLogin).data }
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

    suspend fun addStudent(student: User) {
    studentDatabaseDao.addStudent(student)
    }
    suspend fun getAllStudents():User{
        return  studentDatabaseDao.getAllStudents()
    }
    suspend fun getStudentById(id:String):User{
      return  studentDatabaseDao.getStudent(id)
    }
    suspend fun updateStudent(student: User){
        studentDatabaseDao.updateStudent(student)
    }

    suspend fun addStudent(student: Student) {
        apiClientForAuth.addStudent(student)
    }
}
