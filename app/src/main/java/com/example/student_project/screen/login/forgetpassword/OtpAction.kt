package com.example.student_project.screen.login.forgetpassword

sealed interface OtpAction {
    //we use data class if we need to take value
    data class OnEnterNumber(val number: Int?,val index:Int) : OtpAction
    data class OnChangeFieldFocus(val index: Int) : OtpAction
    //we use data object if we just need to show things
    data object OnKeyboardBack : OtpAction

}

