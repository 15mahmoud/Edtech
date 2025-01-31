package com.example.student_project.ui.screen.log.login

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.student_project.data.model.User
import com.example.student_project.data.network.request.StudentLogin
import com.example.student_project.data.network.response.LoginResponse
import com.example.student_project.data.repo.StudentRepo
import kotlinx.coroutines.launch
import retrofit2.Response


//i need to change place of that
data class LoginResponseState(
    //    var studentLogin: StudentLogin = StudentLogin("", ""),
    //  val user: User? = null,
    val loginResponse: Response<LoginResponse>? = null
)

class LoginViewModel : ViewModel() {
    private val repository = StudentRepo()
    var loginResponseState by mutableStateOf(LoginResponseState())


    fun checkUser(studentLogin: StudentLogin) {
        viewModelScope.launch {
            val loginResponse = repository.checkUser(studentLogin)
         //   loginResponseState = loginResponseState.copy(loginResponse)
        }
    }
}
