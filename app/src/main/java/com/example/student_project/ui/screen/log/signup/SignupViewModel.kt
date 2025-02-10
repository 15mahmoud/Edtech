package com.example.student_project.ui.screen.log.signup

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.student_project.data.model.Student
import com.example.student_project.data.repo.StudentRepo
import javax.inject.Inject
import kotlinx.coroutines.launch

class SignupViewModel @Inject constructor(private val studentRepo: StudentRepo) : ViewModel() {

    fun addStudent(student: Student) {
        viewModelScope.launch { studentRepo.addUser(student) }
    }
}
