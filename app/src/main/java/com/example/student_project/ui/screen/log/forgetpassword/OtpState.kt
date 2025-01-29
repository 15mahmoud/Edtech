package com.example.student_project.ui.screen.log.forgetpassword

data class OtpState(
    val code: List<Int?> = (1..4).map { null },
    val focusedIndex: Int? = null,
    val isValid: Boolean? = null,
)
