package com.example.student_project.ui.screen.log.forgetpassword

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController

@Composable
fun OtpTokenScreen(navController: NavController, userEmail: String?) {
    Text(text = userEmail.toString(), fontSize = 121.sp)
}
