package com.example.student_project.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.student_project.screen.login.AdditionalInfoScreen
import com.example.student_project.screen.home.HomeScreen
import com.example.student_project.screen.LearningScreen
import com.example.student_project.screen.login.LoginScreen
import com.example.student_project.screen.ProfileScreen
import com.example.student_project.screen.home.CourseDetailsScreen
import com.example.student_project.screen.home.filtering.ui.CourseFilterScreen
import com.example.student_project.screen.home.filtering.ui.MentorFilterResultScreen
import com.example.student_project.screen.home.filtering.ui.MentorFilterScreen
import com.example.student_project.screen.login.SplashScreen
import com.example.student_project.screen.login.SignUpScreen
import com.example.student_project.screen.login.forgetpassword.EmailAndPhoneScreen
import com.example.student_project.screen.login.forgetpassword.NewPasswordScreen
import com.example.student_project.screen.login.forgetpassword.OtpScreen

@Composable
fun Navigation() {
    val navController = rememberNavController()
    //start from splash
    NavHost(navController = navController, startDestination = Screens.HomeScreen.route) {
        composable(Screens.SplashScreen.route) {
            SplashScreen(navController)
        }
        composable(Screens.SignupScreen.route) {
            SignUpScreen(navController)
        }
        composable(Screens.LoginScreen.route) {
            LoginScreen(navController)
        }
        composable(Screens.AdditionalInfoScreen.route) {
            AdditionalInfoScreen(navController = navController)
        }
        composable(Screens.HomeScreen.route) {
            HomeScreen(navController = navController)
        }
        composable(Screens.CourseDetailScreen.route+"/{course_title}",
            arguments = (listOf(
                navArgument(name = "course_title"){
                    type=NavType.StringType
                }
            ))) {backStackEntry ->
            CourseDetailsScreen(navController =navController,
                backStackEntry.arguments?.getString("course_title"))
        }
        composable(Screens.LearningScreen.route) {
            LearningScreen(navController = navController)
        }
        composable(Screens.ProfileScreen.route) {
            ProfileScreen(navController = navController)
        }
        composable(Screens.MentorFilterScreen.route) {
            //filled parcel object "FiltrationResult" and send it to search result screen
            MentorFilterScreen(navController)
        }
        composable(
            Screens.MentorFilterResultScreen.route+"/{jop_title}"+"/{rating}"+"/{hourly_rate}",
            arguments = (listOf(
                navArgument(name = "jop_title") {
                    type = NavType.StringType
                },
                navArgument(name = "rating") {
                    type = NavType.FloatType
                },
                navArgument(name = "hourly_rate") {
                    type = NavType.FloatType
                })
                    )
        ){backStackEntry ->
            MentorFilterResultScreen(navController = navController,
                backStackEntry.arguments?.getString("jop_title"),
                backStackEntry.arguments?.getFloat("rating"),
                backStackEntry.arguments?.getFloat("hourly_rate")
            )
        }
        //when we click
        //we will send this data to detailsScreen
        //here will be course details screen

        composable(Screens.CourseFilterScreen.route) {
            //filled parcel object "FiltrationResult" and send it to search result screen
            CourseFilterScreen(navController)
        }
        //here we will send this info to course result screen
        //and then we will send this info to course details screen
        //there will be 2 fun one for courseResultScreen
        //and other one will be for courseDetailsScreen
        //will activate when we click on a course

        composable(Screens.EmailAndPhoneScreen.route) {
            EmailAndPhoneScreen(navController = navController)
        }
        composable(Screens.NewPasswordScreen.route) {
            NewPasswordScreen(navController = navController)
        }
        composable(Screens.OtpScreen.route) {
            //we should take a phone or email from previous screen
            //then send it to backend and get otp code
            //then pass it if true move to next screen
            OtpScreen(navController = navController)
        }
    }
}