package com.example.student_project.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.student_project.screen.login.AdditionalInfoScreen
import com.example.student_project.screen.home.HomeScreen
import com.example.student_project.screen.LearningScreen
import com.example.student_project.screen.login.LoginScreen
import com.example.student_project.screen.login.NameAndEmailScreen
import com.example.student_project.screen.ProfileScreen
import com.example.student_project.screen.Screens
import com.example.student_project.screen.home.filtering.MentorFilterResultScreen
import com.example.student_project.screen.login.SplashScreen
import com.example.student_project.screen.home.filtering.MentorFilterScreen
import com.example.student_project.screen.login.SignUpScreen

@Composable
fun Navigation() {
    val navController = rememberNavController()
    //start from splash
    NavHost(navController = navController, startDestination = Screens.SplashScreen.route) {
        composable(Screens.SplashScreen.route) {
            SplashScreen(navController)
        }
        composable(Screens.SignupScreen.route) {
            SignUpScreen(navController)
        }
        composable(Screens.LoginScreen.route) {
            LoginScreen(navController)
        }
        composable(Screens.NameAndEmailScreen.route) {
            NameAndEmailScreen(navController)
        }
        composable(Screens.AdditionalInfoScreen.route) {
            AdditionalInfoScreen(navController = navController)
        }
        composable(Screens.HomeScreen.route) {
            HomeScreen(navController = navController)
        }
        composable(Screens.LearningScreen.route) {
            LearningScreen(navController = navController)
        }
        composable(Screens.ProfileScreen.route) {
            ProfileScreen(navController = navController)
        }
        composable(Screens.MentorFilterScreen.route) {
            //filled parcel object "FilterationResult" and send it to search result screen
            MentorFilterScreen(navController)
        }
        composable(Screens.MentorFilterResultScreen.route) {
            MentorFilterResultScreen(navController = navController)
        }

    }
}