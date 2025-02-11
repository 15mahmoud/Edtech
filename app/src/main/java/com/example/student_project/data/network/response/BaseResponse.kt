package com.example.student_project.data.network.response

data class BaseResponse<T>(val data: T)


//this will done after OtpScreen
data class BaseResponse1<T>(val token: T)
