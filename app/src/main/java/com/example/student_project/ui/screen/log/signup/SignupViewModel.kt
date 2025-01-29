package com.example.student_project.ui.screen.log.signup

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.student_project.data.model.Student
import com.example.student_project.data.repo.StudentRepo
import kotlinx.coroutines.launch

class SignupViewModel : ViewModel() {
    private val studentRepo: StudentRepo = StudentRepo()

    fun addStudent(student: Student) {
        viewModelScope.launch { studentRepo.addStudent(student) }
    }
}
