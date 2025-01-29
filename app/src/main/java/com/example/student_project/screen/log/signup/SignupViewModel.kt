package com.example.student_project.screen.log.signup

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.student_project.data.component.Student
import com.example.student_project.data.repository.StudentRepo
import kotlinx.coroutines.launch

class SignupViewModel : ViewModel() {
    private val studentRepo: StudentRepo = StudentRepo()

    fun addStudent(student: Student) {
        viewModelScope.launch { studentRepo.addStudent(student) }
    }
}
