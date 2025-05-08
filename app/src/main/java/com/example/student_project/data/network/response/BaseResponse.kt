package com.example.student_project.data.network.response

import java.lang.Error

data class BaseResponse<T>(val data: T,val error: T)

// this will done after OtpScreen
// data class BaseResponse1<T>(val token: T)
