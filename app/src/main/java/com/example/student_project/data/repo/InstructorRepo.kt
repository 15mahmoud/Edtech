package com.example.student_project.data.repo

import com.example.student_project.data.model.Instructor
import com.example.student_project.data.network.ApiClient
import javax.inject.Inject

class InstructorRepo @Inject constructor(private val apiClient: ApiClient){

suspend fun getAllInstructor():Result<List<Instructor>>{
    return Result.runCatching { apiClient.allInstructors().data }
}
    // we will add this to home screen later

}
