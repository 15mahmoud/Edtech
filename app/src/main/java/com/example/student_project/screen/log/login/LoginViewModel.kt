package com.example.student_project.screen.log.login

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.student_project.data.component.LoginResponse
import com.example.student_project.data.component.StudentLogin
import com.example.student_project.data.repository.StudentRepo
import kotlinx.coroutines.launch
import retrofit2.Response

data class LoginResponseState(
    //    var studentLogin: StudentLogin = StudentLogin("", ""),
    val loginResponse: Response<LoginResponse>? = null
)

class LoginViewModel : ViewModel() {
    private val repository = StudentRepo()
    var loginResponseState by mutableStateOf(LoginResponseState())

    fun checkUser(studentLogin: StudentLogin) {
        viewModelScope.launch {
            val loginResponse = repository.checkUser(studentLogin)
            loginResponseState = loginResponseState.copy(loginResponse = loginResponse)
        }
    }
}
